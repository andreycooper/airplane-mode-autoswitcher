package com.weezlabs.airplanemodeautoswitcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.weezlabs.airplanemodeautoswitcher.util.PreferenceUtils;


public class MainActivity extends AppCompatActivity {

    private Button mStartButton;
    private Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartButton = (Button) findViewById(R.id.start_button);
        mStopButton = (Button) findViewById(R.id.stop_button);
        updateButtons();
    }

    private void updateButtons() {
        if (PreferenceUtils.isPhoneStateReceiverWork(this)) {
            mStartButton.setEnabled(false);
            mStartButton.setText(getString(R.string.label_button_start_disabled));
            mStopButton.setEnabled(true);
            mStopButton.setText(getString(R.string.label_button_stop_enabled));

        } else {
            mStopButton.setEnabled(false);
            mStopButton.setText(getString(R.string.label_button_stop_disabled));
            mStartButton.setEnabled(true);
            mStartButton.setText(getString(R.string.label_button_start_enabled));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickStartSwitcher(View view) {
        PreferenceUtils.setPhoneStateReceiverWork(this, true);
        updateButtons();
    }

    public void onClickStopSwitcher(View view) {
        PreferenceUtils.setPhoneStateReceiverWork(this, false);
        updateButtons();
    }
}
