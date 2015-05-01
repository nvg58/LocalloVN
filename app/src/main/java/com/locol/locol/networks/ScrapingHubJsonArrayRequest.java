package com.locol.locol.networks;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GiapNV on 4/16/15.
 * Project LocoL
 */
public class ScrapingHubJsonArrayRequest extends JsonArrayRequest {
    public ScrapingHubJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic MGEzMzE1Mzk5NzRjNDk0NDgzMGVlZTQ4MGJhODIxYWE6");
        headers.put("Accept", "application/json");
        headers.put("Cache-Control", "no-cache");
        return headers;
    }
}
