package com.weezlabs.airplanemodeautoswitcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.weezlabs.airplanemodeautoswitcher.util.StateUtils;


public class StateAlarmListener implements WakefulIntentService.AlarmListener {

    private static final String LOG_TAG = StateAlarmListener.class.getSimpleName();

    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    // time to wait before set AirPlane Mode OFF for check PhoneState
    public static final long WAIT_TIME_TO_SWITCH_OFF = 2 * MINUTE;
    // time to wait before set AirPlane Mode ON
    public static final long WAIT_TIME_TO_SWITCH_ON = 2 * MINUTE;
    // time to wait after AirPlane Mode OFF for check PhoneState
    public static final long WAIT_TIME_TO_CHECK = 1 * MINUTE;
    public static final long MAX_AGE_OF_ALARM = SECOND;

    private long mAlarmTime;

    // need for WakefulIntentService
    public StateAlarmListener() {

    }

    public StateAlarmListener(long alarmTime) {
        mAlarmTime = alarmTime;
    }

    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mAlarmTime, pendingIntent);
    }

    @Override
    public void sendWakefulWork(Context context) {
        // set correct intent by flag from SharedPrefs
        boolean isCheckState = StateUtils.isAfterAirPlaneModeTrigger(context);
        if (isCheckState) {
            WakefulIntentService.sendWakefulWork(context,
                    PhoneStateService.getCheckStateIntent(context));
        } else {
            WakefulIntentService.sendWakefulWork(context,
                    PhoneStateService.getStateOutOfServiceIntent(context));
        }
    }

    @Override
    public long getMaxAge() {
        return MAX_AGE_OF_ALARM;
    }
}
