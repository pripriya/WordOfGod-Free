package com.geval6.wordofgod.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geval6.wordofgod.Core.PlayerActivity;
import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.Listener.Listener;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.SettingsManager.SettingsManager;
import com.geval6.wordofgod.ViewHolder.RecyclerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChapterListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    ArrayList<Chapter> chapters;

    public ChapterListAdapter(Context context, ArrayList<Chapter> chapters) {
        this.context = context;
        this.chapters = chapters;
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.title.setText(chapters.get(position).title);
        holder.name.setText(chapters.get(position).gospel.name);

        if(chapters.get(position).image.isEmpty()){
            Picasso.with(context).load(R.drawable.imageforempty).into(holder.imageView);
        }
        else{
            Picasso.with(context).load(SettingsManager.hostRoot+SettingsManager.content+ chapters.get(position).image).into(holder.imageView);
        }

        holder.setClickListener(new Listener() {
            @Override
            public void recyclerViewListClicked(View view, int i) {
                Intent intent = new Intent(ChapterListAdapter.this.context, PlayerActivity.class);
                ComponentsManager.gospel=chapters.get(position).gospel;
                ComponentsManager.chapter=chapters.get(position);

                context.startActivity(intent);
            }
        });
    }
}
