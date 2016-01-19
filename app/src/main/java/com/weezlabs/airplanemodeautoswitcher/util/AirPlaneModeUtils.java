package com.weezlabs.airplanemodeautoswitcher.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;


public final class AirPlaneModeUtils {
    private AirPlaneModeUtils() {

    }

    private static void notifyAboutAirPlaneMode(Context context) {
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", isAirplaneModeOn(context));
        context.sendBroadcast(intent);
    }

    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static void setAirPlaneMode(Context context, boolean value) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, value ? 1 : 0);
            notifyAboutAirPlaneMode(context);
        } else {
            // it's not working because Settings.Global is read only
            // for non system apps!
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value ? 1 : 0);
            notifyAboutAirPlaneMode(context);
        }
    }
}
