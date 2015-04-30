package com.locol.locol;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.FeedItem;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class ComingSoonActivity extends ActionBarActivity implements FeedItemsLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private ArrayList<FeedItem> feedItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_explore_primary)));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.color_explore_primary
        );

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                // do something...
                Log.wtf("onLoadMore", currentPage + "");
                loadFeedItems(currentPage);
            }
        });

        adapter = new MyRecyclerAdapter(this, feedItemList);
        recyclerView.setAdapter(adapter);

        // load first 10 items
        loadFeedItems(0);
        adapter.setFeedItems(feedItemList);
    }

    private void loadFeedItems(int page) {
        new TaskLoadFeedItems(this).execute(page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coming_soon, menu);
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


    @Override
    public void onFeedItemsLoaded(ArrayList<FeedItem> feedItems) {
        adapter.setFeedItems(feedItems);
    }

    @Override
    public void onRefresh() {
//        loadFeedItems(0);
    }

    private class TaskLoadFeedItems extends AsyncTask<Integer, Void, ArrayList<FeedItem>> {
        private FeedItemsLoadedListener myComponent;
        private VolleySingleton volleySingleton;
        private RequestQueue requestQueue;

        public TaskLoadFeedItems(FeedItemsLoadedListener myComponent) {
            this.myComponent = myComponent;
            volleySingleton = VolleySingleton.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            if (progressBar.getVisibility() != View.GONE)
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<FeedItem> doInBackground(Integer... params) {
            // Eventbrite API
            // String url = "https://www.eventbriteapi.com/v3/events/search/?venue.city=hanoi&token=DBEK5SF2SVBCTIV52X3L";

            // Self hosting API
            // String url = "http://104.236.40.66:27080/locoldb/events/_find?batch_size=100";

            // Scrapinghub API
            String latestJob = "https://storage.scrapinghub.com/jobq/13882/list?count=1";
            JSONArray jobList = Requestor.sendRequestFeedItems(requestQueue, latestJob, ComingSoonActivity.this);
            String job = Parser.parseLatestJobID(jobList);

            Log.wtf("job id", job);

            String url = "https://storage.scrapinghub.com/items/13882?start=" + job + "/" + 10 * params[0] + "&count=10";
            JSONArray response = Requestor.sendRequestFeedItems(requestQueue, url, ComingSoonActivity.this);

            ArrayList<FeedItem> feedItems = Parser.parseJSONResponse(response);

            // sort feed items by start_date
            // Collections.sort(feedItems);

            // coming soon
            ArrayList<FeedItem> comingItems = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (FeedItem item : feedItems) {
                try {
                    Date sDate;
                    sDate = dateFormat.parse(item.getStartDate());
                    Long dT = sDate.getTime() - Calendar.getInstance().getTime().getTime();
                    if (0 < dT && dT <= 1000 * 60 * 60 * 48) {
                        Log.wtf("comingItems.add", sDate.toString());
                        comingItems.add(item);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            feedItemList.addAll(comingItems);
//            Collections.sort(feedItemList);

            return feedItemList;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedItem> feedItems) {
            progressBar.setVisibility(View.GONE);

            if (feedItems.isEmpty()) {
                Toast.makeText(ComingSoonActivity.this, "There are no coming soon events! Check back later!", Toast.LENGTH_SHORT).show();
            }

            if (myComponent != null) {
                myComponent.onFeedItemsLoaded(feedItems);
            }
        }
    }
}
