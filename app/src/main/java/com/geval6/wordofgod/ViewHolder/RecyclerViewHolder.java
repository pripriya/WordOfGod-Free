package com.geval6.wordofgod.ViewHolder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.geval6.wordofgod.Listener.Listener;
import com.geval6.wordofgod.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    public TextView name, title;
    public ImageView imageView;
    Listener listener;
    Typeface medium, regular;

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        medium= Typeface.createFromAsset(itemView.getContext().getAssets(),"WorkSans-Medium.otf");
        regular= Typeface.createFromAsset(itemView.getContext().getAssets(),"WorkSans-Regular.otf");


        this.title = (TextView) itemView.findViewById(R.id.title);
        this.title.setTypeface(medium);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.name.setTypeface(regular);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView);


        itemView.setOnClickListener(this);
    }

    public void setClickListener(Listener itemClickListener) {
        this.listener = itemClickListener;
    }

    public void onClick(View view) {
        this.listener.recyclerViewListClicked(view, getPosition());
    }
}
