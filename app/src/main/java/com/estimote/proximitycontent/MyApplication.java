package com.estimote.proximitycontent;

import android.app.Application;
import android.os.Environment;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.proximitycontent.estimote.BeaconID;
import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetailsFactory;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import java.util.Arrays;


//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static String beaconName = "UNKNOWN";

    public static final String DOWNLOADS_FOLDER = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS).toString();
    public static final String FILE_START_SCAN = "start.scan";
    public static final String FILE_STOP_SCAN = "stop.scan";


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
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 43764, 47789), //branco
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 17715, 13079), //verde
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 31722, 6285),  //roxo
                                new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 20640, 6416)), //azul
                        new EstimoteCloudBeaconDetailsFactory());


    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        proximityContentManager.stopContentUpdates();
        proximityContentManager.destroy();
    }
}
