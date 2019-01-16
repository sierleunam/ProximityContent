package com.estimote.proximitycontent;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonBeacon {
    public static JSONObject makeJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title","Titulo");
        return jsonObject;


    }
}
