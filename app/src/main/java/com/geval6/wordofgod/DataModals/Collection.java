package com.geval6.wordofgod.DataModals;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Collection {
    public String type = "";
    public String label = "";
    public ArrayList items = new ArrayList();


    public Collection(HashMap hashMap) {

        if (hashMap.containsKey("type") && hashMap.get("type") != null) {
            this.type = hashMap.get("type").toString();
        }
        if (hashMap.containsKey("label") && hashMap.get("label") != null) {
            this.label = hashMap.get("label").toString();
        }
        if (type.equals("gospels")) {
            items = Gospel.getObjects((ArrayList<HashMap>) hashMap.get("items"));

        } else if (type.equals("chapters")) {

            items = Chapter.getObjects((ArrayList<HashMap>) hashMap.get("items"));
        }
    }

    public static ArrayList<Collection> getObjects(ArrayList<HashMap> arrayList) {
        ArrayList<Collection> collections = new ArrayList<Collection>();
        for (HashMap item : arrayList) {
            collections.add(new Collection(item));
        }
        return collections;
    }
}
