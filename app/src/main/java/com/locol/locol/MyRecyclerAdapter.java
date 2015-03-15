package com.locol.locol;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.FeedItem;

import java.util.ArrayList;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    private ArrayList<FeedItem> feedItemList = new ArrayList<>();
    private Context mContext;

    public MyRecyclerAdapter(Context context, ArrayList<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
//        notifyDataSetChanged();
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_row, parent, false);
        return new FeedListRowHolder(v);
    }

    public void setFeedItems(ArrayList<FeedItem> feedItems) {
        this.feedItemList = feedItems;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder holder, int position) {
        FeedItem feedItem = feedItemList.get(position);

//        Picasso.with(mContext).load(feedItem.getUrlThumbnail())
//                .error(R.drawable.jewels)
//                .placeholder(R.drawable.jewels)
//                .into(holder.thumbnail);

        holder.thumbnail.setDefaultImageResId(R.drawable.placeholder);
        holder.thumbnail.setImageUrl(feedItem.getUrlThumbnail(), VolleySingleton.getInstance().getImageLoader());

//        ColorMatrix colorMatrix = new ColorMatrix();
//        colorMatrix.setSaturation(0);
//        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
//        holder.thumbnail.setColorFilter(filter);

        holder.title.setText(feedItem.getTitle());
        holder.date.setText(feedItem.getDate());
        holder.place.setText(feedItem.getPlace());
        holder.description.setText(feedItem.getDescription());

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainApplication.getAppContext(), DetailsEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainApplication.getAppContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }
}
