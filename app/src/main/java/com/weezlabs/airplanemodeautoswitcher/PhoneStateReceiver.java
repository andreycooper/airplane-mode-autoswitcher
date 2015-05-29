package com.weezlabs.airplanemodeautoswitcher;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class PhoneStateReceiver extends WakefulBroadcastReceiver {
    private static final String LOG_TAG = PhoneStateReceiver.class.getSimpleName();
    public static final String KEY_STATE_AFTER_JB_MR2 = "voiceRegState";
    public static final String KEY_STATE_BEFORE_JB_MR2 = "state";


    public PhoneStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String key;
        Bundle bundle = intent.getExtras();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            key = KEY_STATE_AFTER_JB_MR2;
        } else {
            key = KEY_STATE_BEFORE_JB_MR2;
        }
        showState(context, bundle, key);
    }

    private void showState(Context context, Bundle bundle, String key) {
        int state = bundle.getInt(key);
        switch (state) {
            case ServiceState.STATE_IN_SERVICE:
                Toast.makeText(context, "State: STATE_IN_SERVICE", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, new Date().toString());
                Log.i(LOG_TAG, "State: STATE_IN_SERVICE");
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                Toast.makeText(context, "State: STATE_EMERGENCY_ONLY", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, new Date().toString());
                Log.i(LOG_TAG, "State: STATE_EMERGENCY_ONLY");
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                Toast.makeText(context, "State: STATE_OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, new Date().toString());
                Log.i(LOG_TAG, "State: STATE_OUT_OF_SERVICE");
                break;
            case ServiceState.STATE_POWER_OFF:
                Toast.makeText(context, "State: STATE_POWER_OFF", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, new Date().toString());
                Log.i(LOG_TAG, "State: STATE_POWER_OFF");
                break;
            default:
                break;
        }
    }

}
