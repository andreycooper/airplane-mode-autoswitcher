package com.weezlabs.airplanemodeautoswitcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.commonsware.cwac.wakeful.WakefulIntentService;


public class AlarmListener implements WakefulIntentService.AlarmListener {
    public AlarmListener() {

    }

    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + PhoneStateService.WAIT_TIME, pendingIntent);
    }

    @Override
    public void sendWakefulWork(Context context) {
        WakefulIntentService.sendWakefulWork(context,
                PhoneStateService.getStateOutOfServiceIntent(context));
    }

    @Override
    public long getMaxAge() {
        return 1000;
    }
}
