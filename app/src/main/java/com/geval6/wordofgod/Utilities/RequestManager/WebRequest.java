package com.geval6.wordofgod.Utilities.RequestManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.DataModals.Collection;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.SettingsManager.SettingsManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WebRequest extends AsyncTask<Void, Void, HashMap> {

    RequestListener listener;
    HashMap parameters;
    RequestIdentifier requestIdentifier;
    Context context;

    public WebRequest(RequestIdentifier requestIdentifier, HashMap parameters, RequestListener requestListener, Context context) {
        this.requestIdentifier = requestIdentifier;
        this.parameters = parameters;
        this.listener = requestListener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        listener.onBeginRequest();
        super.onPreExecute();
    }

    @Override
    protected HashMap doInBackground(Void... params) {
        URL url = null;
        try {
            if (requestIdentifier == RequestIdentifier.getSpan) {
                url = new URL(urlForResource());
            } else {
                url = new URL(urlForIdentifier(requestIdentifier, parameters));
            }
            Log.i("url", url.toString());


            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(methodForIdentifier(requestIdentifier));
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return (HashMap) RequestFunctions.objectFromJson(response.toString());

            } else {
                Log.i("Error", "error");
            }
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(HashMap hashMap) {

        try{
            if (requestIdentifier == RequestIdentifier.dashboardContent) {

                if (hashMap.containsKey("status") && hashMap.get("status").equals(true)) {
                    HashMap content = (HashMap) hashMap.get("content");
                    ArrayList<Collection> collection = new ArrayList<Collection>(Collection.getObjects((ArrayList<HashMap>) content.get("collections")));
                    listener.onRequestCompleted(requestIdentifier,collection);

                    HashMap user = (HashMap) content.get("user");
                    Integer id = (Integer) user.get("id");
                    SharedPreferences.Editor editor = context.getSharedPreferences("user_preference", Context.MODE_PRIVATE).edit();
                    editor.putInt("id", id);
                    editor.commit();
                }
            } else if (requestIdentifier == RequestIdentifier.gospelChapters) {
                if (hashMap.containsKey("status") && hashMap.get("status").equals(true)) {
                    HashMap content = (HashMap) hashMap.get("content");
                    ArrayList<Chapter> chapter = new ArrayList<Chapter>(Chapter.getObjects((ArrayList<HashMap>) content.get("chapters")));
                    listener.onRequestCompleted(requestIdentifier,chapter);
                }
            } else if (requestIdentifier == RequestIdentifier.markChapterAccess) {
                Log.i("status", "true");
            } else if (requestIdentifier == RequestIdentifier.getSpan) {
                listener.onRequestCompleted(requestIdentifier,hashMap);
            }

            super.onPostExecute(hashMap);
        }
        catch (Exception e){
            Log.i("Exception", "Error");
        }

    }

    private String methodForIdentifier(RequestIdentifier requestIdentifier) {
        switch (requestIdentifier) {
            case markChapterAccess:
                return "POST";
            default:
                return "GET";
        }
    }

    private String nameForIdentifier(RequestIdentifier requestIdentifier) {
        switch (requestIdentifier) {
            case dashboardContent:
                return "dashboard/{device_id}";
            case gospelChapters:
                return "chapters/{gospel_id}";
            case markChapterAccess:
                return "chapters?user_id={user_id}&chapter_id={chapter_id}";
            case getSpan:
                return "{smil}";

            default:
                return "";
        }
    }

    private String urlForIdentifier(RequestIdentifier requestIdentifier, HashMap parameters) {
        return (SettingsManager.hostRoot + SettingsManager.service + ComponentsManager.stringByReplacingValues(nameForIdentifier(requestIdentifier), parameters));
    }

    private String urlForResource() {
        return (SettingsManager.hostRoot + SettingsManager.content + ComponentsManager.stringByReplacingValues(nameForIdentifier(requestIdentifier), parameters));
    }

}
