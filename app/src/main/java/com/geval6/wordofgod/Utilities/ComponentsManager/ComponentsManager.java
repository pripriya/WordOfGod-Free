package com.geval6.wordofgod.Utilities.ComponentsManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.DataModals.Gospel;
import com.geval6.wordofgod.R;

import java.util.HashMap;

public class ComponentsManager {

    public static String stringByReplacingValues(String string, HashMap<String, String> hashMap) {

        for (HashMap.Entry<String, String> val : hashMap.entrySet()) {
            string = string.replace("{" + val.getKey() + "}", val.getValue());
        }
        return string;

    }
    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;

        }
        return false;
    }

    public static Gospel gospel;
    public static Chapter chapter;
}

