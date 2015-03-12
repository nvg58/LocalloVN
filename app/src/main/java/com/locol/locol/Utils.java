package com.locol.locol;

import org.json.JSONObject;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key);
    }
}