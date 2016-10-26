package com.geval6.wordofgod.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geval6.wordofgod.Core.PlayerActivity;
import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.DataModals.Gospel;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.SettingsManager.SettingsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GosepelChapterListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Chapter> chapter;
    Gospel gospel;
    private Typeface medium, regular;
    private TextView titleTextview, nameTextview;

    public GosepelChapterListAdapter(Context context, ArrayList<Chapter> chapter) {
        this.context=context;
        this.chapter=chapter;
        gospel=ComponentsManager.gospel;
    }
    @Override
    public int getCount() {
        return chapter.size();
    }

    @Override
    public Object getItem(int position) {
        return chapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chapter_list, parent, false);
        convertView.setPadding(10, 10,10,10);

        titleTextview= (TextView) convertView.findViewById(R.id.title);
        nameTextview = (TextView) convertView.findViewById(R.id.name);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
        prepareFont();
        titleTextview.setText(chapter.get(position).title);
        nameTextview.setText(gospel.name);

        if(chapter.get(position).image.isEmpty()){
            Picasso.with(context).load(R.drawable.imageforempty).into(imageView);
        }
        else{
            Picasso.with(context).load(SettingsManager.hostRoot+SettingsManager.content+ chapter.get(position).image).into(imageView);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GosepelChapterListAdapter.this.context, PlayerActivity.class);
                ComponentsManager.chapter=chapter.get(position);
                ComponentsManager.gospel=gospel;
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private void prepareFont(){
        medium= Typeface.createFromAsset(context.getAssets(),"WorkSans-Medium.otf");
        regular= Typeface.createFromAsset(context.getAssets(),"WorkSans-Regular.otf");
        titleTextview.setTypeface(medium);
        nameTextview.setTypeface(regular);
    }
}
