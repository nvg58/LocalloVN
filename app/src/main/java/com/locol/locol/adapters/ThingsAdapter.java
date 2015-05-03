package com.locol.locol.adapters;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.locol.locol.R;
import com.locol.locol.activities.DetailsEventActivity;
import com.locol.locol.application.MainApplication;
import com.locol.locol.helpers.Preferences;
import com.locol.locol.helpers.Utils;
import com.locol.locol.networks.VolleySingleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThingsAdapter extends ParseQueryAdapter<ParseObject> {

    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();

    public ThingsAdapter(Context context, ParseQueryAdapter.QueryFactory<ParseObject> parseQuery) {
        super(context, parseQuery);
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_row, parent, false);
        }
        super.getItemView(object, v, parent);

        runEnterAnimation(v, 0);

        NetworkImageView thumbnail = (NetworkImageView) v.findViewById(R.id.event_thumbnail);
        thumbnail.setDefaultImageResId(R.drawable.placeholder);
        thumbnail.setImageUrl(object.getString("thumbnail_url"), VolleySingleton.getInstance().getImageLoader());
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(object);
            }
        });

        TextView title = (TextView) v.findViewById(R.id.event_title);
        title.setText(object.getString("title"));

        TextView date = (TextView) v.findViewById(R.id.event_date);
        DateFormat newDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date sDate = object.getDate("start_date");
        Date eDate = object.getDate("end_date");
        date.setText("From " + newDateFormat.format(sDate) + "\nto" + newDateFormat.format(eDate));

        TextView place = (TextView) v.findViewById(R.id.event_place);
        place.setText(object.getString("location"));

        TextView category = (TextView) v.findViewById(R.id.event_category);
        category.setText(object.getString("category"));

        TextView btnDetails = (Button) v.findViewById(R.id.btnDetails);
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(object);
            }
        });

        LinearLayout body = (LinearLayout) v.findViewById(R.id.eventBody);
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityOnClick(object);
            }
        });

        final ImageButton btnLove = (ImageButton) v.findViewById(R.id.btnLove);

        object.pinInBackground();

        final ParseUser user = ParseUser.getCurrentUser();
        final ParseRelation<ParseObject> relation = user.getRelation("love");
        final ParseQuery query = relation.getQuery();
        query.fromLocalDatastore();

        btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.whereEqualTo("objectId", object.getObjectId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list.isEmpty()) {
                            btnLove.setImageResource(R.drawable.ic_heart_red);

                            relation.add(object);
                            user.saveEventually();

                            object.increment("loved");
                            object.saveEventually();

                            Set<String> ids = Preferences.readFromPreferencesToSet(getContext(), "_user_love", "id", new HashSet<String>());
                            ids.add(object.getObjectId());
                            Preferences.saveToPreferences(getContext(), "_user_love", "id", ids);
                        } else {
                            btnLove.setImageResource(R.drawable.ic_heart_outline_grey);

                            relation.remove(object);
                            user.saveEventually();

                            object.put("loved", Math.max(0, object.getInt("loved") - 1));
                            object.saveEventually();

                            Set<String> ids = Preferences.readFromPreferencesToSet(getContext(), "_user_love", "id", new HashSet<String>());
                            ids.remove(object.getObjectId());
                            Preferences.saveToPreferences(getContext(), "_user_love", "id", ids);
                        }
                    }
                });

                Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                btnLove.startAnimation(bounce);
            }
        });

        if (Preferences.readFromPreferencesToSet(getContext(), "_user_love", "id", new HashSet<String>()).contains(object.getObjectId())) {
            btnLove.setImageResource(R.drawable.ic_heart_red);
        } else {
            btnLove.setImageResource(R.drawable.ic_heart_outline_grey);
        }

        return v;
    }


    private void startActivityOnClick(ParseObject object) {
        //Disable enter transition for new Acitvity
        Intent intent = new Intent(MainApplication.getAppContext(), DetailsEventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_FEED_TITLE", object.getString("title"));
        extras.putLong("EXTRA_FEED_START_DATE", object.getDate("start_date").getTime());
        extras.putLong("EXTRA_FEED_END_DATE", object.getDate("end_date").getTime());
        extras.putString("EXTRA_FEED_PLACE", object.getString("location"));
        extras.putString("EXTRA_FEED_CATEGORY", object.getString("category"));
        extras.putString("EXTRA_FEED_MAX_PARTICIPANTS", object.getString("max_participants"));
        extras.putString("EXTRA_FEED_ORGANIZER", object.getString("organizer"));
        extras.putString("EXTRA_FEED_DESCRIPTION", object.getString("description"));
        extras.putString("EXTRA_FEED_URL_THUMBNAIL", object.getString("thumbnail_url"));
        extras.putString("EXTRA_EVENT_ID", object.getObjectId());
        intent.putExtras(extras);

        MainApplication.getAppContext().startActivity(intent);
    }

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(getContext()));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


}