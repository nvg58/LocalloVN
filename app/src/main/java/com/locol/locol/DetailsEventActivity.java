package com.locol.locol;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.locol.locol.networks.VolleySingleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DetailsEventActivity extends ActionBarActivity {
    Toolbar toolbar;
    Bundle extras;

    private final static String TAG = "DetailsEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);

        extras = getIntent().getExtras();
        final String title = extras.getString("EXTRA_FEED_TITLE");
        final String startDate = extras.getString("EXTRA_FEED_START_DATE");
        final String endDate = extras.getString("EXTRA_FEED_END_DATE");
        final String place = extras.getString("EXTRA_FEED_PLACE");
        String latitude = extras.getString("EXTRA_FEED_LATITUDE");
        String longitude = extras.getString("EXTRA_FEED_LONGITUDE");
        String organizer = extras.getString("EXTRA_FEED_ORGANIZER");
        String description = extras.getString("EXTRA_FEED_DESCRIPTION");
        String urlThumbnail = extras.getString("EXTRA_FEED_URL_THUMBNAIL");

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (extras != null) {
            NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.eventThumbnail);
            thumbnail.setDefaultImageResId(R.drawable.placeholder);
            thumbnail.setImageUrl(urlThumbnail, VolleySingleton.getInstance().getImageLoader());

            final TextView tvTitle = (TextView) findViewById(R.id.eventTitle);
            tvTitle.setText(title);

            TextView tvPlace = (TextView) findViewById(R.id.eventPlace);
            tvPlace.setText(place);

            TextView tvDate = (TextView) findViewById(R.id.eventDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat newDateFormat = new SimpleDateFormat("EEE, d MMM yyyy '\n'hh:mm aaa");
            try {
                Date date = dateFormat.parse(startDate);
                tvDate.setText(newDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TextView tvDetails = (TextView) findViewById(R.id.eventDescription);
            tvDetails.setText(Html.fromHtml(
                    description,
                    new URLImageParser(tvDetails, this),
                    null));

            TextView tvOrganizer = (TextView) findViewById(R.id.organizer);
            tvOrganizer.setText(organizer);

            Button btnAddToCalendar = (Button) findViewById(R.id.btnAddToCalendar);
            btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        Calendar start = DateToCalendar(dateFormat.parse(startDate));
                        Calendar end = DateToCalendar(dateFormat.parse(endDate));
                        addEvent(DetailsEventActivity.this, title, place, start, end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void addEvent(Context ctx, String title, String place, Calendar start, Calendar end) {
        Log.d(TAG, "AddUsingIntent.addEvent()");
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", title);
        intent.putExtra("eventLocation", place);
        intent.putExtra("beginTime", start.getTimeInMillis());
        intent.putExtra("endTime", end.getTimeInMillis());
        intent.putExtra("allDay", false);
        ctx.startActivity(intent);
    }

    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
