package com.weezlabs.airplanemodeautoswitcher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Andrey Bondarenko on 29.05.15.
 */
public final class PreferenceUtils {
    private static final int INCORRECT_VALUE = -1;

    private static final String SIGNAL_RECEIVER_KEY = "signal_receiver_key";
    private static final String OUT_OF_SERVICE_TIME = "out_of_service_time";
    private static final String PHONE_STATE = "phone_state";

    private PreferenceUtils() {
    }

    public static void setPhoneStateReceiverWork(Context context, boolean isOn) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext()).edit();
        editor.putBoolean(SIGNAL_RECEIVER_KEY, isOn);
        editor.apply();
    }

    public static boolean isPhoneStateReceiverWork(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getBoolean(SIGNAL_RECEIVER_KEY, false);
    }

    public static void setTimeOutOfService(Context context, long time) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext()).edit();
        editor.putLong(OUT_OF_SERVICE_TIME, time);
        editor.apply();
    }

    public static long getTimeOutOfService(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getLong(OUT_OF_SERVICE_TIME, INCORRECT_VALUE);
    }

    public static void setPhoneState(Context context, int state) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext()).edit();
        editor.putInt(PHONE_STATE, state);
        editor.apply();
    }

    public static int getPhoneState(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getInt(PHONE_STATE, INCORRECT_VALUE);
    }
}
