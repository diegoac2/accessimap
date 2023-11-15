package edu.illinois.cs465.accessimap;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String FIRST_TIME_KEY = "firstTime";

    public static boolean isFirstTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(FIRST_TIME_KEY, true);
    }

    public static void setFirstTime(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_TIME_KEY, value);
        editor.apply();
    }
}
