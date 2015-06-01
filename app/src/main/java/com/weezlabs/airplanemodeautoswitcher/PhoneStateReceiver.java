package com.weezlabs.airplanemodeautoswitcher;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.weezlabs.airplanemodeautoswitcher.util.PreferenceUtils;

import java.util.Date;

public class PhoneStateReceiver extends WakefulBroadcastReceiver {
    private static final String LOG_TAG = PhoneStateReceiver.class.getSimpleName();

    public static final String KEY_STATE_AFTER_JB_MR2 = "voiceRegState";
    public static final String KEY_STATE_BEFORE_JB_MR2 = "state";


    public PhoneStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!PreferenceUtils.isPhoneStateReceiverWork(context)) {
            return;
        }
        String key;
        Bundle bundle = intent.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            key = KEY_STATE_AFTER_JB_MR2;
        } else {
            key = KEY_STATE_BEFORE_JB_MR2;
        }
        handleState(context, bundle, key);
    }

    private void handleState(final Context context, Bundle bundle, String key) {
        int state = bundle.getInt(key);
        PreferenceUtils.setPhoneState(context, state);
        final AlarmListener alarmListener = new AlarmListener();
        switch (state) {
            case ServiceState.STATE_IN_SERVICE:
                logState(context, "STATE_IN_SERVICE");
                WakefulIntentService.cancelAlarms(context);
                // TODO: send action to IntentService for switch off airplane mode
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                logState(context, "STATE_EMERGENCY_ONLY");
                WakefulIntentService.cancelAlarms(context);
                // TODO: send action to IntentService for switch off airplane mode
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                logState(context, "STATE_OUT_OF_SERVICE");
                PreferenceUtils.setTimeOutOfService(context, System.currentTimeMillis());
                WakefulIntentService.scheduleAlarms(alarmListener, context, true);
                break;
            case ServiceState.STATE_POWER_OFF:
                logState(context, "STATE_POWER_OFF");
                break;
            default:
                break;
        }
    }

    private void logState(Context context, String state) {
        Toast.makeText(context, "State: " + state, Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, new Date().toString());
        Log.i(LOG_TAG, "State: " + state);
    }

}
