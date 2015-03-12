package com.locol.locol;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GiapNV on 3/12/15.
 * Project LocoL
 */
public class Preferences {

    public static void saveToPreferences(Context context, String preferenceFileName, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceFileName, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
