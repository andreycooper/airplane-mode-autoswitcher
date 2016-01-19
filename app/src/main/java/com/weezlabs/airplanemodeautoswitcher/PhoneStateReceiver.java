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
import com.weezlabs.airplanemodeautoswitcher.util.StateUtils;

import java.util.Date;

public class PhoneStateReceiver extends WakefulBroadcastReceiver {
    private static final String LOG_TAG = PhoneStateReceiver.class.getSimpleName();

    public static final String KEY_STATE_AFTER_JB_MR2 = "voiceRegState";
    public static final String KEY_STATE_BEFORE_JB_MR2 = "state";


    public PhoneStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!StateUtils.isPhoneStateReceiverWork(context)) {
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
        StateUtils.setPhoneState(context, state);

        long time = StateUtils.isCheckState(context) ?
                StateAlarmListener.WAIT_TIME_TO_CHECK : StateAlarmListener.WAIT_TIME_TO_SWITCH_ON;
        final StateAlarmListener alarmListener = new StateAlarmListener(time);

        switch (state) {
            case ServiceState.STATE_IN_SERVICE:
                logState(context, "STATE_IN_SERVICE");
                setNormalWork(context, state);
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                logState(context, "STATE_EMERGENCY_ONLY");
                setNormalWork(context, state);
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                logState(context, "STATE_OUT_OF_SERVICE");
                // save last time of STATE_OUT_OF_SERVICE and set alarm for calling WakefulIntentService
                StateUtils.setTimeOutOfService(context, System.currentTimeMillis());
                WakefulIntentService.scheduleAlarms(alarmListener, context);
                break;
            case ServiceState.STATE_POWER_OFF:
                logState(context, "STATE_POWER_OFF");
                // receive that state when airplane mode is ON
                break;
            default:
                break;
        }
    }

    private void setNormalWork(Context context, int state) {
        Log.i(LOG_TAG, "Good state received, setting to normal work...");
        WakefulIntentService.cancelAlarms(context);
        StateUtils.setPhoneState(context, state);
        StateUtils.setCheckState(context, false);
        StateUtils.setAfterAirplaneModeTrigger(context, false);
    }

    private void logState(Context context, String state) {
        Toast.makeText(context, context.getString(R.string.toast_state, state), Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, new Date().toString());
        Log.i(LOG_TAG, "State: " + state);
    }

}
