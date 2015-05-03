package com.locol.locol.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.locol.locol.R;
import com.locol.locol.adapters.SourceAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourceFragment extends Fragment {


    public SourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_source, container, false);

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Source");
                return query;
            }
        };
        final SourceAdapter adapter = new SourceAdapter(getActivity(), parseQuery);
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
        gridview.setAdapter(adapter);

        return v;
    }


}
