package com.locol.locol;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.FeedItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    private ArrayList<FeedItem> feedItemList = new ArrayList<>();
    private Context mContext;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private static final int ANIMATED_ITEMS_COUNT = 2;

    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

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

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    private void updateHeartButton(final FeedListRowHolder holder, final boolean animated) {
        if (!likeAnimations.containsKey(holder)) {
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(holder, animatorSet);

            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLove, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLove, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLove, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (animated) {
                        holder.btnLove.setImageResource(R.drawable.ic_heart_red);
                        Log.wtf("likedPositions", "Loved!");
                    } else {
                        holder.btnLove.setImageResource(R.drawable.ic_heart_outline_grey);
                        Log.wtf("likedPositions", "Un-Loved!");
                    }
                }
            });

            animatorSet.play(rotationAnim);
            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    resetLikeAnimationState(holder);
                }
            });

            animatorSet.start();
        }
    }

    public void setFeedItems(ArrayList<FeedItem> feedItems) {
        this.feedItemList = feedItems;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final FeedListRowHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        updateHeartButton(holder, holder.isLoved);
        final FeedItem feedItem = feedItemList.get(position);

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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DateFormat newDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date sDate = dateFormat.parse(feedItem.getStartDate());
            Date eDate = dateFormat.parse(feedItem.getEndDate());
            holder.date.setText("Từ " + newDateFormat.format(sDate) + " đến " + newDateFormat.format(eDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.place.setText(feedItem.getLocation());
        holder.category.setText(feedItem.getCategory());

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(feedItem);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(feedItem);
            }
        });

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(feedItem);
            }
        });

        holder.btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.isLoved = !holder.isLoved;
                updateHeartButton(holder, holder.isLoved);
            }
        });

//        if (likeAnimations.containsKey(holder)) {
//            likeAnimations.get(holder).cancel();
//        }
//        resetLikeAnimationState(holder);
    }

    private void resetLikeAnimationState(FeedListRowHolder holder) {
        likeAnimations.remove(holder);
    }

    private void startActivityOnClick(FeedItem feedItem) {
        //Disable enter transition for new Acitvity
        Intent intent = new Intent(MainApplication.getAppContext(), DetailsEventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_FEED_TITLE", feedItem.getTitle());
        extras.putString("EXTRA_FEED_START_DATE", feedItem.getStartDate());
        extras.putString("EXTRA_FEED_END_DATE", feedItem.getEndDate());
        extras.putString("EXTRA_FEED_PLACE", feedItem.getLocation());
        extras.putString("EXTRA_FEED_CATEGORY", feedItem.getCategory());
        extras.putString("EXTRA_FEED_MAX_PARTICIPANTS", feedItem.getMaxParticipants());
        extras.putString("EXTRA_FEED_ORGANIZER", feedItem.getOrganizer());
        extras.putString("EXTRA_FEED_DESCRIPTION", feedItem.getDescription());
        extras.putString("EXTRA_FEED_URL_THUMBNAIL", feedItem.getUrlThumbnail());
        intent.putExtras(extras);

        MainApplication.getAppContext().startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

}
