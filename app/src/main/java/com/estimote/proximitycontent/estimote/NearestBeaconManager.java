package com.estimote.proximitycontent.estimote;

import android.content.Context;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.proximitycontent.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.estimote.coresdk.observation.region.RegionUtils.computeAccuracy;
import static com.estimote.proximitycontent.FileUtils.listfilename;
import static com.estimote.proximitycontent.FileUtils.writeJsonToFile;
import static com.estimote.proximitycontent.JsonBeacon.makeJsonArray;
import static com.estimote.proximitycontent.JsonBeacon.makeJsonObject;

class NearestBeaconManager {

    private static final String TAG = "NearestBeaconManager";
    private static final BeaconRegion ALL_ESTIMOTE_BEACONS = new BeaconRegion("all Estimote beacons", null, null, null);

    private List<BeaconID> beaconIDs;

    private Listener listener;

    private BeaconID currentlyNearestBeaconID;
    private boolean firstEventSent = false;

    private BeaconManager beaconManager;

    NearestBeaconManager(Context context, List<BeaconID> beaconIDs) {
        this.beaconIDs = beaconIDs;

        beaconManager = new BeaconManager(context);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                checkForNearestBeacon(list);
            }
        });
    }

    private static Beacon findNearestBeacon(List<Beacon> beacons) {
        Beacon nearestBeacon = null;
        double nearestBeaconsDistance = -1;
        for (Beacon beacon : beacons) {
            double distance = computeAccuracy(beacon);
            if (distance > -1 && (distance < nearestBeaconsDistance || nearestBeacon == null)) {
                nearestBeacon = beacon;
                nearestBeaconsDistance = distance;


                writeJsonToFile(
                            FileUtils.filename,
                            makeJsonObject(
                                    String.valueOf(nearestBeacon.getProximityUUID()),
                                    nearestBeacon.getMajor(),
                                    nearestBeacon.getMinor(),
                                    nearestBeacon.getMacAddress(),
                                    nearestBeacon.getRssi(), nearestBeaconsDistance));
            }

        }

//        Log.d(TAG, "Nearest beacon: " + nearestBeacon + ", distance: " + nearestBeaconsDistance);

        return nearestBeacon;
    }

    public interface Listener {
        void onNearestBeaconChanged(BeaconID beaconID);
    }

    private static List<Beacon> filterOutBeaconsByIDs(List<Beacon> beacons, List<BeaconID> beaconIDs) {
        List<Beacon> filteredBeacons = new ArrayList<>();
        if (!beacons.isEmpty())
            for (Beacon beacon : beacons) {
                BeaconID beaconID = BeaconID.fromBeacon(beacon);
                if (beaconIDs.contains(beaconID)) {
                    filteredBeacons.add(beacon);
                }
            }
        writeJsonToFile(listfilename, Objects.requireNonNull(makeJsonArray(filteredBeacons)));
        return filteredBeacons;
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    void startNearestBeaconUpdates() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
            }
        });
    }

    void stopNearestBeaconUpdates() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
    }

    void destroy() {
        beaconManager.disconnect();
    }

    private void updateNearestBeacon(BeaconID beaconID) {
        currentlyNearestBeaconID = beaconID;
        firstEventSent = true;
        if (listener != null) {
            listener.onNearestBeaconChanged(beaconID);
        }
    }

    private void checkForNearestBeacon(List<Beacon> allBeacons) {
        List<Beacon> beaconsOfInterest = filterOutBeaconsByIDs(allBeacons, beaconIDs);
        Beacon nearestBeacon = findNearestBeacon(beaconsOfInterest);
        if (nearestBeacon != null) {
            BeaconID nearestBeaconID = BeaconID.fromBeacon(nearestBeacon);
            if (!nearestBeaconID.equals(currentlyNearestBeaconID) || !firstEventSent) {
                updateNearestBeacon(nearestBeaconID);
            }
        } else if (currentlyNearestBeaconID != null || !firstEventSent) {
            updateNearestBeacon(null);
        }


    }
}
