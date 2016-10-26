package com.geval6.wordofgod.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geval6.wordofgod.DataModals.Collection;
import com.geval6.wordofgod.R;

import java.util.ArrayList;

public class ChaptersAdapter extends BaseAdapter {
    Context context;
    ArrayList<Collection> collections;


    public ChaptersAdapter(Context context, ArrayList<Collection> collections) {
        this.context = context;
        this.collections = collections;

    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "WorkSans-Medium.otf");
        convertView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview_item, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setTypeface(typeface);
        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (collections.get(position).type.equals("gospels")) {
            textView.setText(collections.get(position).label);
            recyclerView.setAdapter(new GospelsAdapter(context, collections.get(position).items));
        } else if (collections.get(position).type.equals("chapters")) {
            textView.setText(collections.get(position).label);
            recyclerView.setAdapter(new ChapterListAdapter(context, collections.get(position).items));
        }

        return convertView;
    }
}
