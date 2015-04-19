package com.locol.locol;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by GiapNV on 3/13/15.
 * Project LocoL
 */
public class Requestor {
    public static JSONArray sendRequestFeedItems(RequestQueue requestQueue, String url, final Activity activity) {
        JSONArray response = null;

        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        ScrapingHubJsonArrayRequest request = new ScrapingHubJsonArrayRequest(url, requestFuture, requestFuture);
        requestQueue.add(request);

        try {
            response = requestFuture.get(600000, TimeUnit.MILLISECONDS);
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
