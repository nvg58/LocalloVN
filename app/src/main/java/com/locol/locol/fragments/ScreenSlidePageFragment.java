package com.locol.locol.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.locol.locol.activities.LoginActivity;
import com.locol.locol.R;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSlidePageFragment extends Fragment {

    int page;

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    public static ScreenSlidePageFragment newInstance(int index) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rv = null;
        switch (getShownIndex()) {
            case 0:
                rv = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page_0, container, false);
                break;
            case 1:
                rv = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page_1, container, false);
                break;
            case 2:
                rv = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page_2, container, false);
                Button btn_fb_sign_up = (Button) rv.findViewById(R.id.btn_fb_sign_up);
                btn_fb_sign_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Determine whether the current user is an anonymous user
                        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                            // If user is anonymous, send the user to LoginActivity.class
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                break;
        }

        return rv;
    }

}
