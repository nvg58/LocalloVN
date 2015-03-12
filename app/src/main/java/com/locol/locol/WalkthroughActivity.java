package com.locol.locol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.locol.locol.networks.VolleySingleton;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class WalkthroughActivity extends ActionBarActivity {

    public static final String PREF_FILE_NAME = "user_info";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_AVATAR = "avatar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(WalkthroughActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to MyNavigationDrawer.class
                Account.setUserFBId(Preferences.readFromPreferences(WalkthroughActivity.this, PREF_FILE_NAME, KEY_USER_ID, null));
                Account.setUserEmail(Preferences.readFromPreferences(WalkthroughActivity.this, PREF_FILE_NAME, KEY_USER_EMAIL, null));
                Account.setUserName(Preferences.readFromPreferences(WalkthroughActivity.this, PREF_FILE_NAME, KEY_USER_NAME, null));

                String url = Preferences.readFromPreferences(WalkthroughActivity.this, PREF_FILE_NAME, KEY_USER_AVATAR, null);
                VolleySingleton.getInstance().getImageLoader().get(url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Account.setUserAvatar(response.getBitmap());

                        startActivity(new Intent(WalkthroughActivity.this, MyNavigationDrawer.class));
                        finish();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(WalkthroughActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}
