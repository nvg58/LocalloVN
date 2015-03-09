package com.locol.locol;

import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

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

        Session session = Session.getActiveSession();
        new Request(
                session,
                "/me",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        /* handle the result */
                        Log.d(TAG, response.toString());
                    }
                }
        ).executeAsync();

        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(), "NeoKree", "neokree@gmail.com", R.drawable.photo, R.drawable.bamboo);
        this.addAccount(account);

        this.addSection(newSection("Section 1", new FeedFragment()));
    }
}
