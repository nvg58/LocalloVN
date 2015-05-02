package com.locol.locol.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.locol.locol.R;
import com.locol.locol.db.DBFeedItems;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by GiapNV on 3/9/15.
 * Project ${PROJECT_NAME}
 */
public class MainApplication extends Application {
    private static MainApplication sInstance;
    private static DBFeedItems mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable local data store
        Parse.enableLocalDatastore(this);

        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseFacebookUtils.initialize(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        sInstance = this;
    }

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DBFeedItems getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBFeedItems(getAppContext());
        }
        return mDatabase;
    }
}

