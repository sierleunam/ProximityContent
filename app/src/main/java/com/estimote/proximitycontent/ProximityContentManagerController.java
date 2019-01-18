package com.estimote.proximitycontent;

import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetails;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import static com.estimote.proximitycontent.MyApplication.proximityContentManager;

public class ProximityContentManagerController {
    static void startScan() {
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;

                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;

//                    text = "You're in " + beaconDetails + "'s range!";
                    MyApplication.beaconName = beaconDetails.getBeaconName();

                } else {
//                    text = "No beacons in range.";
                    MyApplication.beaconName = "UNKNOWN";
                }
            }
        });
        proximityContentManager.startContentUpdates();
    }

    public static void stopScan() {
        proximityContentManager.stopContentUpdates();
    }

    public static void destroyScan() {
        proximityContentManager.destroy();
    }
}
