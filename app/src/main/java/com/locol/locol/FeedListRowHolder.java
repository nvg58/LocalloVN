package com.locol.locol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected TextView date;
    protected TextView place;
    protected TextView description;

    public FeedListRowHolder(View itemView) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.eventThumbnail);
        this.title = (TextView) itemView.findViewById(R.id.eventTitle);
        this.date = (TextView) itemView.findViewById(R.id.eventDate);
        this.place = (TextView) itemView.findViewById(R.id.eventPlace);
        this.description = (TextView) itemView.findViewById(R.id.eventDescription);
    }
}
