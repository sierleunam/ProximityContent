package com.estimote.proximitycontent;

import com.estimote.coresdk.recognition.utils.MacAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class JsonBeacon {
    public static JSONObject makeJsonObject(String proximityUUID, int major, int minor, MacAddress macAddress, int rssi, double nearestBeaconsDistance) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        String name = null;
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

        }

        int time = (int) System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(time);
        jsonObject.put("timestamp", tsTemp.toString());
        jsonObject.put("name", name);
        jsonObject.put("uuid", proximityUUID);
        jsonObject.put("macaddress", macAddress.toString());
        jsonObject.put("rssi", rssi);
        jsonObject.put("distance", nearestBeaconsDistance);

        return jsonObject;


    }
}
