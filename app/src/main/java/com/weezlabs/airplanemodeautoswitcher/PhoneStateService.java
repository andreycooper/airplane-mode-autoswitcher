package com.weezlabs.airplanemodeautoswitcher;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.weezlabs.airplanemodeautoswitcher.util.AirPlaneModeUtils;
import com.weezlabs.airplanemodeautoswitcher.util.PreferenceUtils;


public class PhoneStateService extends WakefulIntentService {
    public static final String ACTION_STATE_OUT_OF_SERVICE =
            "com.weezlabs.airplanemodeautoswitcher.STATE_OUT_OF_SERVICE";
    public static final String ACTION_STATE_IN_SERVICE =
            "com.weezlabs.airplanemodeautoswitcher.STATE_IN_SERVICE";

    public static final long MINUTE = 60 * 1000;
    public static final long WAIT_TIME = 1 * MINUTE;

    public static final String TAG = "PhoneStateService";

    public PhoneStateService() {
        super("PhoneStateService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        Log.i(TAG, "doWakeFulWork()..");
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_STATE_OUT_OF_SERVICE:
                    handleActionStateOutOfService();
                    break;
                case ACTION_STATE_IN_SERVICE:
                    handleActionStateInService();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleActionStateInService() {

    }

    private void handleActionStateOutOfService() {
        Log.i(TAG, "STATE_OUT_OF_SERVICE action...");
        long currentTime = System.currentTimeMillis();
        int state = PreferenceUtils.getPhoneState(this);
        if (state == ServiceState.STATE_OUT_OF_SERVICE
                && currentTime - PreferenceUtils.getTimeOutOfService(this) >= WAIT_TIME) {
            Log.d(TAG, "Time to switch airplane mode!");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                boolean isAirPlaneModeEnabled = AirPlaneModeUtils.isAirplaneModeOn(this);
                if (!isAirPlaneModeEnabled) {
                    AirPlaneModeUtils.setAirPlaneMode(this, true);
                    AirPlaneModeUtils.notifyAboutAirPlaneMode(this);
                }
            } else {
                showToast("Time to switch airplane mode!");
            }
        }
    }

    private void showToast(final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent getStateOutOfServiceIntent(Context context) {
        Intent intent = new Intent(context, PhoneStateService.class);
        intent.setAction(ACTION_STATE_OUT_OF_SERVICE);
        return intent;
    }

    public static Intent getStateInServiceIntent(Context context) {
        Intent intent = new Intent(context, PhoneStateService.class);
        intent.setAction(ACTION_STATE_IN_SERVICE);
        return intent;
    }

}
