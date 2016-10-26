package com.geval6.wordofgod.DataModals;

import java.util.ArrayList;
import java.util.HashMap;

public class Gospel {
    public int id;
    public String name = "";
    public String image = "";
    public Artist artist;

    public Gospel(HashMap hashMap) {
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
        if (hashMap.containsKey("artist")) {
            if(hashMap.get("artist") != null && hashMap.get("artist") instanceof HashMap) {
                this.artist = new Artist((HashMap)hashMap.get("artist"));
        }
        }
    }

    public static ArrayList<Gospel> getObjects(ArrayList<HashMap> arrayList) {
        ArrayList<Gospel> gospels = new ArrayList<Gospel>();
        for (HashMap item : arrayList) {
            gospels.add(new Gospel(item));
        }
        return gospels;
    }
}
