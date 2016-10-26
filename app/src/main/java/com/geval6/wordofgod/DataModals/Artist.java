package com.geval6.wordofgod.DataModals;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Artist {
    public Integer id;
    public String name = "";
    public String image = "";

    public Artist(HashMap hashMap) {

        if (hashMap.containsKey("id") && hashMap.get("id") != null && hashMap.get("id") instanceof Integer) {
            this.id = Integer.valueOf(hashMap.get("id").toString());
        } else if (hashMap.containsKey("id") && hashMap.get("id") != null && hashMap.get("id") instanceof String) {
            String id = hashMap.get("id").toString();
            this.id = Integer.valueOf(id);
        }
        if (hashMap.containsKey("name") && hashMap.get("name") != null) {
            this.name = hashMap.get("name").toString();
        }
        if (hashMap.containsKey("image") && hashMap.get("image") != null) {
            this.image = hashMap.get("image").toString();
        }
    }

    public static ArrayList<Artist> getObjects(ArrayList<HashMap> arrayList) {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        for (HashMap item : arrayList) {
            artists.add(new Artist(item));
        }
        return artists;
    }
}
