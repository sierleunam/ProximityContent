package com.estimote.proximitycontent;

import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.utils.MacAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.estimote.coresdk.observation.region.RegionUtils.computeAccuracy;

public class JsonBeacon {
    public static JSONObject makeJsonObject(
            String proximityUUID,
            int major,
            int minor,
            MacAddress macAddress,
            int rssi,
            double nearestBeaconsDistance) {
        JSONObject jsonObject = new JSONObject();


        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyyMMddmmss", Locale.ENGLISH);
        String time = sdf.format(new Date());
        try {
            jsonObject.put("timestamp", time);
            jsonObject.put("name", getName(major));
            jsonObject.put("uuid", proximityUUID);
            jsonObject.put("macaddress", macAddress.toString());
            jsonObject.put("rssi", rssi);
            jsonObject.put("distance", nearestBeaconsDistance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static JSONObject makeJsonArray(List<Beacon> beacons) {
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyyMMddmmss", Locale.ENGLISH);
        for (Beacon beacon : beacons) {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("timestamp", sdf.format(new Date()));
                jsonObject.put("name", getName(beacon.getMajor()));
                jsonObject.put("uuid", beacon.getProximityUUID().toString());
                jsonObject.put("macaddress", beacon.getMacAddress().toString());
                jsonObject.put("rssi", beacon.getMeasuredPower());
                jsonObject.put("distance", computeAccuracy(beacon));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }


        try {
            return new JSONObject().put("list", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getName(int major) {
        String name;
        switch (major) {
            case 43764:
                name = "branco";
                break;
            case 17715:
                name = "verde";
                break;
            case 31722:
                name = "roxo";
                break;
            case 20640:
                name = "azul";
                break;
            default:
                name = "UNKNOWN";
        }
        return name;
    }
}
