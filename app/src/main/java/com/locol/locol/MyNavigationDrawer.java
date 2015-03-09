package com.locol.locol;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.locol.locol.networks.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by GiapNV on 3/9/15.
 * Project LocoL
 */
public class MyNavigationDrawer extends MaterialNavigationDrawer {

    public final static String TAG = "MyNavigationDrawer";

    @Override
    public void init(Bundle bundle) {
        // add accounts

        final MaterialAccount account = new MaterialAccount(MyNavigationDrawer.this.getResources(),
                Account.getUserName(),
                Account.getUserEmail(),
                Account.getUserAvatar(),
                R.drawable.bamboo);
        this.addAccount(account);

        this.addSection(newSection(
                "Feed",
                R.mipmap.ic_event_grey600_24dp,
                new FeedFragment()).setSectionColor(getResources().getColor(R.color.colorFeedPrimary),
                getResources().getColor(R.color.colorFeedPrimaryDark)));

        this.addSection(newSection(
                "Explore",
                R.mipmap.ic_explore_grey600_24dp,
                new FeedFragment()).setSectionColor(getResources().getColor(R.color.colorExplorePrimary),
                getResources().getColor(R.color.colorExplorePrimaryDark)));

        this.addSection(newSection(
                "Profile",
                R.mipmap.ic_account_box_grey600_24dp,
                new FeedFragment()).setSectionColor(getResources().getColor(R.color.colorAccountPrimary),
                getResources().getColor(R.color.colorAccountPrimaryDark)));

        this.addBottomSection(newSection(
                "Settings",
                new FeedFragment()));

        this.addBottomSection(newSection(
                "Help & Feedback",
                new FeedFragment()));

        this.addBottomSection(newSection(
                "About Us",
                new FeedFragment()));
    }
}
