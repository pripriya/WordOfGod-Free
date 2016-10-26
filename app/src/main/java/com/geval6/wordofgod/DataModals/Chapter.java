package com.geval6.wordofgod.DataModals;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Chapter {
    public int id;
    public String title = "";
    public String image = "";
    public String audio = "";
    public String html = "";
    public String smil = "";
    public Gospel gospel;


    public Chapter(HashMap hashMap) {

        if (hashMap.containsKey("id") && hashMap.get("id") != null && hashMap.get("id") instanceof Integer) {
            this.id = Integer.valueOf(hashMap.get("id").toString());
        } else if (hashMap.containsKey("id") && hashMap.get("id") != null && hashMap.get("id") instanceof String) {
            this.id =Integer.valueOf(hashMap.get("id").toString());
        }
        if (hashMap.containsKey("title") && hashMap.get("title") != null) {
            this.title = hashMap.get("title").toString();
        }
        if (hashMap.containsKey("image") && hashMap.get("image") != null) {
            this.image = hashMap.get("image").toString();
        }

        if (hashMap.containsKey("audio") && hashMap.get("audio") != null) {
            this.audio = hashMap.get("audio").toString();
        }
        if (hashMap.containsKey("html") && hashMap.get("html") != null) {
            this.html = hashMap.get("html").toString();
        }
        if (hashMap.containsKey("smil") && hashMap.get("smil") != null) {
            this.smil = hashMap.get("smil").toString();
        }

        if (hashMap.containsKey("gospel") && hashMap.get("gospel") != null && hashMap.get("gospel") instanceof HashMap) {
            this.gospel = new Gospel((HashMap) hashMap.get("gospel")) ;
        }
        if (hashMap.containsKey("artist") && hashMap.get("artist") != null && hashMap.get("artist") instanceof HashMap) {
            this.gospel.artist = new Artist((HashMap) hashMap.get("artist"));
        }
    }

    public static ArrayList<Chapter> getObjects(ArrayList<HashMap> arrayList) {
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        for (HashMap item : arrayList) {
            chapters.add(new Chapter(item));
        }
        return chapters;
    }
}
