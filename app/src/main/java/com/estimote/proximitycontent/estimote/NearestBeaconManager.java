package com.estimote.proximitycontent.estimote;

import android.content.Context;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.proximitycontent.FileUtils;
import com.estimote.proximitycontent.MyApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;
import static com.estimote.coresdk.observation.region.RegionUtils.computeAccuracy;
import static com.estimote.proximitycontent.FileUtils.CheckFile;
import static com.estimote.proximitycontent.FileUtils.DeleteFile;
import static com.estimote.proximitycontent.FileUtils.WriteJsonToFile;
import static com.estimote.proximitycontent.JsonBeacon.makeJsonObject;
import static com.estimote.proximitycontent.MyProximityService.stopActionScan;
import static java.lang.Thread.sleep;

public class NearestBeaconManager {

    private static final String TAG = "NearestBeaconManager";
    private static final String FILE_STOP_SCAN = "stop.scan";
    private static final String FILE_START_SCAN = "start.scan";
    private static final BeaconRegion ALL_ESTIMOTE_BEACONS = new BeaconRegion("all Estimote beacons", null, null, null);

    private List<BeaconID> beaconIDs;

    private Listener listener;

    private BeaconID currentlyNearestBeaconID;
    private boolean firstEventSent = false;

    private BeaconManager beaconManager;

    public NearestBeaconManager(Context context, List<BeaconID> beaconIDs) {
        this.beaconIDs = beaconIDs;

        beaconManager = new BeaconManager(context);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                checkForNearestBeacon(list);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onNearestBeaconChanged(BeaconID beaconID);
    }

    public void startNearestBeaconUpdates() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
            }
        });
    }

    public void stopNearestBeaconUpdates() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
    }

    public void destroy() {
        beaconManager.disconnect();
    }

    private void checkForNearestBeacon(List<Beacon> allBeacons) {
        List<Beacon> beaconsOfInterest = filterOutBeaconsByIDs(allBeacons, beaconIDs);

           try {
               Beacon nearestBeacon = findNearestBeacon(beaconsOfInterest);
               if (nearestBeacon != null) {
                   BeaconID nearestBeaconID = BeaconID.fromBeacon(nearestBeacon);
                   if (!nearestBeaconID.equals(currentlyNearestBeaconID) || !firstEventSent) {
                       updateNearestBeacon(nearestBeaconID);
                   }
               } else if (currentlyNearestBeaconID != null || !firstEventSent) {
                   updateNearestBeacon(null);
               }
           } catch (Exception e)
           {}

    }

    private void updateNearestBeacon(BeaconID beaconID) {
        currentlyNearestBeaconID = beaconID;
        firstEventSent = true;
        if (listener != null) {
            listener.onNearestBeaconChanged(beaconID);
        }
    }

    private static List<Beacon> filterOutBeaconsByIDs(List<Beacon> beacons, List<BeaconID> beaconIDs) {
        List<Beacon> filteredBeacons = new ArrayList<>();
        for (Beacon beacon : beacons) {
            BeaconID beaconID = BeaconID.fromBeacon(beacon);
            if (beaconIDs.contains(beaconID)) {
                filteredBeacons.add(beacon);
            }
        }
//        Log.d(TAG, "filterOutBeaconsByIDs: " + filteredBeacons);
        return filteredBeacons;
    }

    private static Beacon findNearestBeacon(List<Beacon> beacons) throws JSONException {
        Beacon nearestBeacon = null;
        double nearestBeaconsDistance = -1;
        for (Beacon beacon : beacons) {
            double distance = computeAccuracy(beacon);
            if (distance > -1 && (distance < nearestBeaconsDistance || nearestBeacon == null)) {
                nearestBeacon = beacon;
                nearestBeaconsDistance = distance;


                    WriteJsonToFile(
                            FileUtils.filename,
                            makeJsonObject(
                                    String.valueOf(nearestBeacon.getProximityUUID()),
                                    nearestBeacon.getMajor(),
                                    nearestBeacon.getMinor(),
                                    nearestBeacon.getMacAddress(),
                                    nearestBeacon.getRssi(), nearestBeaconsDistance));


//                DeleteFile(FILE_START_SCAN);
            }
        }


        Log.d(TAG, "Nearest beacon: " + nearestBeacon + ", distance: " + nearestBeaconsDistance);

//        if (CheckFile(FILE_STOP_SCAN)) {
//            stopActionScan(getApplicationContext());
//
//        }

        return nearestBeacon;
    }
}
