package com.locol.locol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.locol.locol.R;
import com.locol.locol.helpers.Preferences;
import com.locol.locol.pojo.Account;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class SplashScreenActivity extends ActionBarActivity {

    public static final String PREF_FILE_NAME = "user_info";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_AVATAR = "avatar";

    private static final int TIME = 3 * 1000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pre_loader);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    // If current user is NOT anonymous user
                    // Get current user data from Parse.com
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // Send logged in users to MyNavigationDrawer.class
                        Account.setUserFBId(Preferences.readFromPreferences(SplashScreenActivity.this, PREF_FILE_NAME, KEY_USER_ID, null));
                        Account.setUserEmail(Preferences.readFromPreferences(SplashScreenActivity.this, PREF_FILE_NAME, KEY_USER_EMAIL, null));
                        Account.setUserName(Preferences.readFromPreferences(SplashScreenActivity.this, PREF_FILE_NAME, KEY_USER_NAME, null));

                        startActivity(new Intent(SplashScreenActivity.this, MyNavigationDrawer.class));
                        finish();
                    } else {
                        // Send user to LoginActivity.class
                        Intent intent = new Intent(SplashScreenActivity.this, WalkthroughActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, WalkthroughActivity.class);
                    startActivity(intent);
                }

                SplashScreenActivity.this.finish();

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }, TIME);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, TIME);

    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
