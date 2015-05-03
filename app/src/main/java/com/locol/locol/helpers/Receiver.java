package com.locol.locol.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.locol.locol.activities.DetailsEventActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GiapNV on 5/4/15.
 * Project LocoL
 */
public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(final Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String data = bundle.getString("com.parse.Data");
            Log.wtf("com.parse.Data", data);
            try {
                JSONObject object = new JSONObject(data);

                final Intent i = new Intent(context, DetailsEventActivity.class);

                i.putExtras(intent.getExtras());
                Log.wtf("intent.getExtras()", intent.getExtras().toString());

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                query.getInBackground(object.getString("event_id"), new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // object will be your event
                            Bundle extras = new Bundle();
                            extras.putString("EXTRA_FEED_TITLE", object.getString("title"));
                            extras.putLong("EXTRA_FEED_START_DATE", object.getLong("start_date"));
                            extras.putLong("EXTRA_FEED_END_DATE", object.getLong("end_date"));
                            extras.putString("EXTRA_FEED_PLACE", object.getString("location"));
                            extras.putString("EXTRA_FEED_CATEGORY", object.getString("category"));
                            extras.putString("EXTRA_FEED_MAX_PARTICIPANTS", object.getString("max_participants"));
                            extras.putString("EXTRA_FEED_ORGANIZER", object.getString("organizer"));
                            extras.putString("EXTRA_FEED_DESCRIPTION", object.getString("description"));
                            extras.putString("EXTRA_FEED_URL_THUMBNAIL", object.getString("thumbnail_url"));
                            extras.putString("EXTRA_EVENT_ID", object.getString("event_id"));

                            i.putExtras(extras);

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        } else {
                            // something went wrong
                            e.printStackTrace();
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
