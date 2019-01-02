package com.holmal.app.holmal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesAccess {
    String filename = "holmal";

    public void storePreferences(Context context, String key, String value){
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readPreferences(Context context, String key){
        String value;
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        value = preferences.getString(key, null);
        return value;
    }
}
