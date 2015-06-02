package com.weezlabs.airplanemodeautoswitcher;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.weezlabs.airplanemodeautoswitcher.util.AirPlaneModeUtils;
import com.weezlabs.airplanemodeautoswitcher.util.StateUtils;


public class PhoneStateService extends WakefulIntentService {
    public static final String ACTION_STATE_OUT_OF_SERVICE =
            "com.weezlabs.airplanemodeautoswitcher.STATE_OUT_OF_SERVICE";
    public static final String ACTION_CHECK_STATE =
            "com.weezlabs.airplanemodeautoswitcher.STATE_IN_SERVICE";

    public static final String LOG_TAG = "PhoneStateService";

    public PhoneStateService() {
        super("PhoneStateService");
    }

    public static Intent getStateOutOfServiceIntent(Context context) {
        Intent intent = new Intent(context, PhoneStateService.class);
        intent.setAction(ACTION_STATE_OUT_OF_SERVICE);
        return intent;
    }

    public static Intent getCheckStateIntent(Context context) {
        Intent intent = new Intent(context, PhoneStateService.class);
        intent.setAction(ACTION_CHECK_STATE);
        return intent;
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        Log.i(LOG_TAG, "doWakeFulWork()..");
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_STATE_OUT_OF_SERVICE:
                    handleActionStateOutOfService();
                    break;
                case ACTION_CHECK_STATE:
                    handleActionCheckState();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleActionCheckState() {
        Log.i(LOG_TAG, "CHECK_STATE action...");
        boolean isAirPlaneModeEnabled = AirPlaneModeUtils.isAirplaneModeOn(this);
        if (isAirPlaneModeEnabled) {
            setAirPlaneModeOff();
        }
    }

    private void handleActionStateOutOfService() {
        Log.i(LOG_TAG, "STATE_OUT_OF_SERVICE action...");
        long currentTime = System.currentTimeMillis();
        long timePeriod = StateUtils.isCheckState(this) ?
                StateAlarmListener.WAIT_TIME_TO_CHECK : StateAlarmListener.WAIT_TIME_TO_SWITCH_ON;
        int state = StateUtils.getPhoneState(this);
        if (state == ServiceState.STATE_OUT_OF_SERVICE
                && currentTime - StateUtils.getTimeOutOfService(this) >= timePeriod) {
            Log.i(LOG_TAG, "Time to switch airplane mode!");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                boolean isAirPlaneModeEnabled = AirPlaneModeUtils.isAirplaneModeOn(this);
                if (!isAirPlaneModeEnabled) {
                    setAirPlaneModeOn();
                }
            } else {
                showToast(getString(R.string.toast_jelly_bean));
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

    private void setAirPlaneModeOn() {
        boolean wifiState = isWifiEnabled();
        boolean bluetoothState = isBluetoothEnabled();

        AirPlaneModeUtils.setAirPlaneMode(this, true);

        // set state for cancel AirPlane Mode and set alarm for calling WakefulIntentService
        StateUtils.setCheckState(this, true);
        StateUtils.setAfterAirplaneModeTrigger(this, true);
        StateAlarmListener alarmListener =
                new StateAlarmListener(StateAlarmListener.WAIT_TIME_TO_SWITCH_OFF);
        WakefulIntentService.scheduleAlarms(alarmListener, this);

        setConnectionState(wifiState, bluetoothState);
    }

    private void setAirPlaneModeOff() {
        boolean wifiState = isWifiEnabled();
        boolean bluetoothState = isBluetoothEnabled();

        AirPlaneModeUtils.setAirPlaneMode(this, false);
        StateUtils.setAfterAirplaneModeTrigger(this, false);

        setConnectionState(wifiState, bluetoothState);
    }

    private void setConnectionState(boolean wifiState, boolean bluetoothState) {
        setWifiState(wifiState);
        setBluetoothState(bluetoothState);
    }

    private boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private void setWifiState(boolean state) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
    }

    private boolean isBluetoothEnabled() {
        final BluetoothAdapter bluetoothAdapter = getBlueToothAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private boolean setBluetoothState(boolean state) {
        final BluetoothAdapter bluetoothAdapter = getBlueToothAdapter();
        return bluetoothAdapter != null
                && (state ? bluetoothAdapter.enable() : bluetoothAdapter.disable());
    }

    private BluetoothAdapter getBlueToothAdapter() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return BluetoothAdapter.getDefaultAdapter();
        } else {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            return bluetoothManager.getAdapter();
        }
    }

}
