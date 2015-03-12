package com.locol.locol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.internal.Utility;
import com.locol.locol.networks.VolleySingleton;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.Arrays;

public class LoginActivity extends ActionBarActivity {
    private String TAG = "LoginActivity";
    public static final String PREF_FILE_NAME = "user_info";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_AVATAR = "avatar";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();


        // TODO should move `rsvp_event`, `rsvp_event` and `user_events` permission to other activity (when user actually need them)

        ParseFacebookUtils.logIn(Arrays.asList("public_profile", "email", "user_friends", "user_events"), this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    new Request(
                            Session.getActiveSession(),
                            "/me",
                            null,
                            HttpMethod.GET,
                            new Request.Callback() {
                                public void onCompleted(Response response) {
                                    try {
                                        Log.d(TAG, response.toString());
                                        Account.setUserFBId(response.getGraphObject().getInnerJSONObject().get(KEY_USER_ID).toString());
                                        Account.setUserEmail(response.getGraphObject().getInnerJSONObject().get(KEY_USER_EMAIL).toString());
                                        Account.setUserName(response.getGraphObject().getInnerJSONObject().get(KEY_USER_NAME).toString());

                                        Preferences.saveToPreferences(LoginActivity.this, PREF_FILE_NAME, KEY_USER_ID, Account.getUserFBId());
                                        Preferences.saveToPreferences(LoginActivity.this, KEY_USER_EMAIL, PREF_FILE_NAME, Account.getUserEmail());
                                        Preferences.saveToPreferences(LoginActivity.this, KEY_USER_NAME, PREF_FILE_NAME, Account.getUserName());

                                        final Bundle params = new Bundle();
                                        params.putBoolean("redirect", false);
                                        params.putInt("height", 200);
                                        params.putInt("width", 200);
                                        params.putString("type", "normal");
                                        new Request(
                                                Session.getActiveSession(),
                                                "/me/picture",
                                                params,
                                                HttpMethod.GET,
                                                new Request.Callback() {
                                                    public void onCompleted(Response response) {
                                                        try {
                                                            Log.d(TAG, response.toString());
                                                            final String url = response.getGraphObject().getInnerJSONObject().getJSONObject("data").get("url").toString();
                                                            VolleySingleton.getInstance().getImageLoader().get(url, new ImageLoader.ImageListener() {
                                                                @Override
                                                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                                                    Account.setUserAvatar(response.getBitmap());
                                                                    Preferences.saveToPreferences(LoginActivity.this, PREF_FILE_NAME, KEY_USER_AVATAR, url);

                                                                    startActivity(new Intent(LoginActivity.this, MyNavigationDrawer.class));
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {

                                                                }
                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                        ).executeAsync();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
