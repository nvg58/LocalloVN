package com.locol.locol;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.locol.locol.networks.VolleySingleton;


public class DetailsEventActivity extends ActionBarActivity {
    Toolbar toolbar;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);

        extras = getIntent().getExtras();

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (extras != null) {
            NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.eventThumbnail);
            thumbnail.setDefaultImageResId(R.drawable.placeholder);
            thumbnail.setImageUrl(extras.getString("EXTRA_FEED_URL_THUMBNAIL"), VolleySingleton.getInstance().getImageLoader());

            TextView title = (TextView) findViewById(R.id.eventTitle);
            title.setText(extras.getString("EXTRA_FEED_TITLE"));

            TextView place = (TextView) findViewById(R.id.eventPlace);
            place.setText(extras.getString("EXTRA_FEED_PLACE"));

            TextView date = (TextView) findViewById(R.id.eventDate);
            date.setText(extras.getString("EXTRA_FEED_DATE"));

            TextView details = (TextView) findViewById(R.id.eventDescription);
            details.setText(Html.fromHtml(
                    extras.getString("EXTRA_FEED_DESCRIPTION"),
                    new URLImageParser(details, this),
                    null));
        }
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
