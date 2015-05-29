package com.weezlabs.airplanemodeautoswitcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class SignalStrengthReceiver extends WakefulBroadcastReceiver {
    private static final String LOG_TAG = SignalStrengthReceiver.class.getSimpleName();

    public SignalStrengthReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // stop execute if user sets AutoSwitcher off
        if (!Utils.isSignalReceiverWork(context)) {
            return;
        }

        Bundle bundle = intent.getExtras();
        int signalStrength = bundle.getInt("GsmSignalStrength");
        Toast.makeText(context, "signalStrength: " + signalStrength, Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, new Date().toString());
        Log.i(LOG_TAG, "signalStrength: " + signalStrength);
    }
}
