package com.locol.locol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.FeedItem;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements FeedItemsLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "FeedFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String STATE_FEED_ITEMS = "state_feed_items";


    private String mParam1;
    private String mParam2;

    private ArrayList<FeedItem> feedItemList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private MyRecyclerAdapter mAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MyRecyclerAdapter(getActivity(), feedItemList);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            feedItemList = savedInstanceState.getParcelableArrayList(STATE_FEED_ITEMS);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            feedItemList = MainApplication.getWritableDatabase().getAllFeedItems();
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (feedItemList.isEmpty()) {
                new TaskLoadFeedItems(this).execute();
            }
        }

        mAdapter.setFeedItems(feedItemList);

        return view;
    }


    private void parseResult(JSONArray response) {
        feedItemList = Parser.parseJSONResponse(response);
        Log.wtf(TAG, feedItemList.toString());
    }

    @Override
    public void onFeedItemsLoaded(ArrayList<FeedItem> feedItems) {
        //update the Adapter to contain the new movies downloaded from the web
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.setFeedItems(feedItems);
    }

    @Override
    public void onRefresh() {
        new TaskLoadFeedItems(this).execute();
    }

    private class TaskLoadFeedItems extends AsyncTask<Void, Void, ArrayList<FeedItem>> {
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
        }

        @Override
        protected ArrayList<FeedItem> doInBackground(Void... params) {
//            String url = "https://www.eventbriteapi.com/v3/events/search/?venue.city=hanoi&token=DBEK5SF2SVBCTIV52X3L";
//            String url = "http://104.236.40.66:27080/locoldb/events/_find?batch_size=100";
            String url = "https://storage.scrapinghub.com/items/13882";
            JSONArray response = Requestor.sendRequestFeedItems(requestQueue, url, getActivity());

            ArrayList<FeedItem> feedItems = Parser.parseJSONResponse(response);
            MainApplication.getWritableDatabase().insertFeedItems(feedItems, true);
            return feedItems;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedItem> feedItems) {
            if (myComponent != null) {
                myComponent.onFeedItemsLoaded(feedItems);
            }
        }
    }
}
