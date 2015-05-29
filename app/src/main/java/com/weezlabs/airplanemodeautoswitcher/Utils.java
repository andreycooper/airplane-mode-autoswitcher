package com.weezlabs.airplanemodeautoswitcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Andrey Bondarenko on 29.05.15.
 */
public class Utils {
    private static final String SIGNAL_RECEIVER_KEY = "signal_receiver_key";

    private Utils() {
    }

    public static void setSignalReceiverWork(Context context, boolean isOn) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SIGNAL_RECEIVER_KEY, isOn);
        editor.apply();
    }

    public static boolean isSignalReceiverWork(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getBoolean(SIGNAL_RECEIVER_KEY, false);
    }
}
