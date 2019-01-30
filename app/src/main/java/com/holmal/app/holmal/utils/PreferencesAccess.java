package com.holmal.app.holmal.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Models Preferences of the app
 *
 * @author manuela
 */
public class PreferencesAccess {
    private String filename = "holmal";

    /**
     * Writes data to the {@link SharedPreferences}
     *
     * @param context The {@link Context}
     * @param key     On which key the data has to be written
     * @param value   The data that has to be written
     */
    public void storePreferences(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Reads data from the {@link SharedPreferences}
     *
     * @param context The {@link Context}
     * @param key     From which key the data has to be read
     * @return The data/value of the key
     */
    public String readPreferences(Context context, String key) {
        String value;
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        value = preferences.getString(key, null);
        return value;
    }
}
