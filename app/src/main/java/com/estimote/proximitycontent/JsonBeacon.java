package com.estimote.proximitycontent;

import com.estimote.coresdk.recognition.utils.MacAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class JsonBeacon {
    public static JSONObject makeJsonObject(String name, String proximityUUID, MacAddress macAddress, int rssi, double nearestBeaconsDistance) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        int time = (int) System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(time);
        jsonObject.put("timestamp", tsTemp.toString());
        jsonObject.put("name", name);
        jsonObject.put("uuid", proximityUUID);
        jsonObject.put("macaddress", macAddress);
        jsonObject.put("rssi", rssi);
        jsonObject.put("distance", nearestBeaconsDistance);

        return jsonObject;


    }
}
