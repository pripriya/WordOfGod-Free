package com.geval6.wordofgod.Core;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geval6.wordofgod.Adapter.ChaptersAdapter;
import com.geval6.wordofgod.DataModals.Collection;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.RequestManager.RequestIdentifier;
import com.geval6.wordofgod.Utilities.RequestManager.RequestListener;
import com.geval6.wordofgod.Utilities.RequestManager.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements RequestListener {

    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout progressCircle;
    private String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInternetConnectivity();
    }
    private void checkInternetConnectivity() {

        if (ComponentsManager.getConnectivityStatus(this) == true) {
            prepareActionBar();
            prepareLayoutViews();
        } else if (ComponentsManager.getConnectivityStatus(this) == false) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void prepareActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.actionbar_layout, null);
        actionBar.setCustomView(view);
    }

    private void prepareLayoutViews() {
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.listview);
        progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        getDeviceId();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeRequest();
            }
        });

        executeRequest();
    }
    private void getDeviceId() {
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private void executeRequest() {
        HashMap parameters = new HashMap();
        parameters.put("device_id", device_id);
        WebRequest webRequest = new WebRequest(RequestIdentifier.dashboardContent, parameters, HomeActivity.this,HomeActivity.this);
        webRequest.execute();
    }


    @Override
    public void onBeginRequest() {
        if (swipeRefreshLayout.isRefreshing()) {
            progressCircle.setVisibility(View.INVISIBLE);
        } else {
            progressCircle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestCompleted(RequestIdentifier requestIdentifier,Object... object) {

        if (object != null) {

            ArrayList<Collection> collections = (ArrayList<Collection>) object[0];

            ChaptersAdapter chaptersAdapter = new ChaptersAdapter(HomeActivity.this, collections);
            listView.setAdapter(chaptersAdapter);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            progressCircle.setVisibility(View.INVISIBLE);
        } else {
            Log.i("error", "error");
        }
    }
}
