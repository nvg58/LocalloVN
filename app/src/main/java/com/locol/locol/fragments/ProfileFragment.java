package com.locol.locol.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.locol.locol.R;
import com.locol.locol.pojo.Account;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    private static final String KEY_TITLE = "title";
    public static ProfileFragment newInstance(String title) {
        ProfileFragment f = new ProfileFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        CircleImageView userPhoto = (CircleImageView) v.findViewById(R.id.user_photo);
        Picasso.with(getActivity())
                .load(Account.getUserAvatarUrl())
                .placeholder(R.drawable.profile)
                .into(userPhoto);

        return v;
    }


}
