package com.geval6.wordofgod.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geval6.wordofgod.Core.ChapterListActivity;
import com.geval6.wordofgod.DataModals.Gospel;
import com.geval6.wordofgod.Listener.Listener;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.SettingsManager.SettingsManager;
import com.geval6.wordofgod.ViewHolder.RecyclerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GospelsAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    ArrayList<Gospel>gospels;


    public GospelsAdapter(Context context, ArrayList<Gospel> gospels) {
        this.context = context;
        this.gospels=gospels;
    }

    @Override
    public int getItemCount() {
        return gospels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gospel_recycler_item, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.title.setText(gospels.get(position).name);

        if(gospels.get(position).artist!=null){
            holder.name.setText(gospels.get(position).artist.name);
        }

        if(gospels.get(position).image.isEmpty()){
            Picasso.with(context).load(R.drawable.imageforempty).into(holder.imageView);
        }
        else{
            Picasso.with(context).load(SettingsManager.hostRoot+SettingsManager.content+ gospels.get(position).image).into(holder.imageView);
        }

        holder.setClickListener(new Listener() {
            @Override
            public void recyclerViewListClicked(View view, int position) {
                Intent intent = new Intent(GospelsAdapter.this.context, ChapterListActivity.class);
                intent.putExtra("id", String.valueOf(gospels.get(position).id));

                ComponentsManager.gospel=gospels.get(position);
                context.startActivity(intent);
            }
        });
    }
}
