package com.dealxtra.dealxtra.api_service;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onError(String error);
}

