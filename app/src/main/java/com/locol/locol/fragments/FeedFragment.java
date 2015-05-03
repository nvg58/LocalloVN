package com.locol.locol.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.locol.locol.R;
import com.locol.locol.adapters.MyRecyclerAdapter;
import com.locol.locol.adapters.ThingsAdapter;
import com.locol.locol.helpers.Parser;
import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.FeedItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

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
    private ListView listView;
    private ThingsAdapter adapter;

    private ProgressBar progressBar;

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
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final List<String> listCats = new ArrayList<>();

        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = user.getRelation("interest");
        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject o : list) {
                    listCats.add(o.getString("name"));
                }

                ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery("Event");
                        query.whereContainedIn("category", listCats);
                        query.whereGreaterThanOrEqualTo("start_date", Calendar.getInstance().getTime());
                        query.orderByAscending("start_date");
                        return query;
                    }
                };
                adapter = new ThingsAdapter(getActivity(), parseQuery);
                listView = (ListView) view.findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                adapter.loadObjects();
                adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
                    @Override
                    public void onLoading() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoaded(List<ParseObject> list, Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        return view;
    }

    private void parseResult(JSONArray response) {
        feedItemList = Parser.parseJSONResponse(response);
    }

}
