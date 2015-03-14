package com.locol.locol;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected NetworkImageView thumbnail;
    protected TextView title;
    protected TextView date;
    protected TextView place;
    protected TextView description;
    protected Button btnDetails;

    public FeedListRowHolder(View itemView) {
        super(itemView);
        this.thumbnail = (NetworkImageView) itemView.findViewById(R.id.eventThumbnail);
        this.title = (TextView) itemView.findViewById(R.id.eventTitle);
        this.date = (TextView) itemView.findViewById(R.id.eventDate);
        this.place = (TextView) itemView.findViewById(R.id.eventPlace);
        this.description = (TextView) itemView.findViewById(R.id.eventDescription);
        this.btnDetails= (Button) itemView.findViewById(R.id.btnDetails);
    }
}
