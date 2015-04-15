package com.locol.locol;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by GiapNV on 3/13/15.
 * Project LocoL
 */
public class Requestor {
    public static JSONObject sendRequestFeedItems(RequestQueue requestQueue, String url, final Activity activity) {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        ScrapingHubJsonObjectRequest request = new ScrapingHubJsonObjectRequest(
                Request.Method.GET,
                url,
                null, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Network error. Please reload!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return response;
    }
}
