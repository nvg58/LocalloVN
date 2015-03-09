package com.locol.locol;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

/**
 * Created by GiapNV on 3/9/15.
 * Project ${PROJECT_NAME}
 */
public class MainApplication extends Application {
    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        sInstance = this;
    }

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}

