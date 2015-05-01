package com.locol.locol.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locol.locol.R;
import com.locol.locol.activities.ComingSoonActivity;
import com.locol.locol.activities.MostFavouriteActivity;
import com.locol.locol.activities.NewEventsActivity;
import com.locol.locol.activities.TrendingActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {


    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        TextView btn_trending = (TextView) v.findViewById(R.id.btn_trending);
        btn_trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TrendingActivity.class));
            }
        });

        TextView btn_new = (TextView) v.findViewById(R.id.btn_new);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventsActivity.class));
            }
        });

        TextView btn_coming_soon = (TextView) v.findViewById(R.id.btn_coming_soon);
        btn_coming_soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ComingSoonActivity.class));
            }
        });

        TextView btn_most_favourite = (TextView) v.findViewById(R.id.btn_most_favourite);
        btn_most_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MostFavouriteActivity.class));
            }
        });

        return v;
    }


}
