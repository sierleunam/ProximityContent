package com.estimote.proximitycontent;

import com.estimote.coresdk.recognition.utils.MacAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonBeacon {
    public static JSONObject makeJsonObject(
            String proximityUUID,
            int major,
            int minor,
            MacAddress macAddress,
            int rssi,
            double nearestBeaconsDistance) {
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

        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyyMMddmmss", Locale.ENGLISH);
        String time = sdf.format(new Date());
        try {
            jsonObject.put("timestamp", time);
            jsonObject.put("name", name);
            jsonObject.put("uuid", proximityUUID);
            jsonObject.put("macaddress", macAddress.toString());
            jsonObject.put("rssi", rssi);
            jsonObject.put("distance", nearestBeaconsDistance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;


    }
}
