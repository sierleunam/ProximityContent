package com.estimote.proximitycontent;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;
import static com.estimote.proximitycontent.MyProximityIntentService.startActionScan;
import static com.estimote.proximitycontent.MyProximityIntentService.stopActionScan;

public class MyProximityReceiver extends BroadcastReceiver {
    public static final String TAG = "MyProximityReceiver";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (Objects.requireNonNull(intent.getAction()).equals("com.sysdevmobile.estimote.START"))
            startActionScan(getApplicationContext());
        else if (Objects.requireNonNull(intent.getAction()).equals("com.sysdevmobile.estimote.STOP"))
            stopActionScan(getApplicationContext());
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");

    }
}
