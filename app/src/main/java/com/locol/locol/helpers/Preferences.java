package com.locol.locol.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

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

    public static void saveToPreferences(Context context, String preferenceFileName, String preferenceName, Set<String> preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceFileName, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static Set<String> readFromPreferencesToSet(Context context, String preferenceFileName, String preferenceName, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(preferenceName, defaultValue);
    }

    public static void clearAll(Context context, String preferenceFileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
