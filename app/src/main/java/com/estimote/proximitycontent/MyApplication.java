package com.estimote.proximitycontent;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.proximitycontent.estimote.BeaconID;
import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetails;
import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetailsFactory;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import java.io.File;
import java.util.Arrays;


//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    static ProximityContentManager proximityContentManager;
    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: put your App ID and App Token here
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "androidthings-0ui", "8e98d34da8cbd04cf4b15d78d6e2a321");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
//        EstimoteSDK.enableDebugLogging(true);

        proximityContentManager =
                new ProximityContentManager(
                        this,
                        Arrays.asList(
                                // TODO: replace with UUIDs, majors and minors of your own beacons
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 43764, 47789),
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 17715, 13079),
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 31722, 6285),
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 31722, 6285)),
                        new EstimoteCloudBeaconDetailsFactory());

        File DownloadsPublicDir = Environment.getExternalStoragePublicDirectory("Estimote");

    }
}
