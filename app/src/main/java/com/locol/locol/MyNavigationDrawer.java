package com.locol.locol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.locol.locol.networks.VolleySingleton;

import java.net.Inet4Address;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by GiapNV on 3/9/15.
 * Project LocoL
 */
public class MyNavigationDrawer extends MaterialNavigationDrawer {

    public final static String TAG = "MyNavigationDrawer";

    static final String STATE_AVATAR = "avatar";
    static final String STATE_COVER = "cover";
    static final String STATE_PATH = "objPath";

    public static final String PREF_FILE_NAME = "user_info";
    public static final String KEY_USER_AVATAR = "avatar";

    @Override
    public void init(Bundle savedInstanceState) {

        // add accounts
        final MaterialAccount account = new MaterialAccount(
                MyNavigationDrawer.this.getResources(),
                Account.getUserName(),
                Account.getUserEmail(),
                Account.getUserAvatar(),
//                R.drawable.photo,
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
                new ExploreFragment()).setSectionColor(getResources().getColor(R.color.colorExplorePrimary),
                getResources().getColor(R.color.colorExplorePrimaryDark)));

        this.addSection(newSection(
                "Profile",
                R.mipmap.ic_account_box_grey600_24dp,
                new ProfileFragment()).setSectionColor(getResources().getColor(R.color.colorAccountPrimary),
                getResources().getColor(R.color.colorAccountPrimaryDark)));

        this.addBottomSection(newSection(
                "Settings",
                new Intent(MyNavigationDrawer.this, SettingActivity.class)));

        this.addBottomSection(newSection(
                "Help & Feedback",
                new FeedFragment()));

        this.addBottomSection(newSection(
                "About Us",
                new FeedFragment()));
    }
}
