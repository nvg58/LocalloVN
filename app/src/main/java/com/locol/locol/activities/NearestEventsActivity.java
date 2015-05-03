package com.locol.locol.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.locol.locol.R;
import com.locol.locol.adapters.ThingsAdapter;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;


public class NearestEventsActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView listView;
    private ThingsAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_events);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_explore_primary)));

        Bundle extras = getIntent().getExtras();
        Double lat = extras.getDouble("LAT");
        Double lng = extras.getDouble("LNG");
        final ParseGeoPoint userLocation = new ParseGeoPoint(lat, lng);

        ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Event");
                query.whereNear("geo_point", userLocation);
                return query;
            }
        };
        adapter = new ThingsAdapter(this, parseQuery);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        adapter.loadObjects();
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<ParseObject> list, Exception e) {
                if (list.isEmpty())
                    Toast.makeText(NearestEventsActivity.this, "There are no near events! Check back later!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            user.put("location", userLocation);
                            user.saveInBackground();

//                            if (adapter.getItem(0).getDate("start_date").getTime() <= Calendar.getInstance().getTime().getTime()
//                                    && Calendar.getInstance().getTime().getTime() <= adapter.getItem(0).getDate("end_date").getTime()) {

                            HashMap<String, Object> params = new HashMap<>();
                            final ParseObject event = adapter.getItem(0);
                            params.put("targetLocation", event.getParseGeoPoint("geo_point"));
                            params.put("distance", 11.0);
                            params.put("message", "Checkout the nearest event for you!");

                            params.put("title", event.getString("title"));
//                            params.put("start_date", event.getDate("start_date").getTime());
//                            params.put("end_date", event.getDate("end_date").getTime());
//                            params.put("location", event.getString("location"));
//                            params.put("category", event.getString("category"));
//                            params.put("max_participants", event.getString("max_participants"));
//                            params.put("organizer", event.getString("organizer"));
//                            params.put("description", event.getString("description"));
//                            params.put("thumbnail_url", event.getString("thumbnail_url"));
                            params.put("event_id", event.getObjectId());

                            ParseCloud.callFunctionInBackground("pushNearestWhenOpenApp", params, new FunctionCallback<String>() {
                                @Override
                                public void done(String s, ParseException e) {
                                    Toast.makeText(NearestEventsActivity.this, s + event.getParseGeoPoint("geo_point").distanceInKilometersTo(userLocation), Toast.LENGTH_SHORT).show();
                                }
                            });

//                            }

                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearest_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}
