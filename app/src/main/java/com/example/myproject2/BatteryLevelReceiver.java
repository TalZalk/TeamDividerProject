package com.example.myproject2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class BatteryLevelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryLevel = (level / (float) scale) * 100;

            // Show the battery level in a Toast message
            Toast.makeText(context, "Battery Level: " + batteryLevel + "%", Toast.LENGTH_SHORT).show();
        }
    }
}
