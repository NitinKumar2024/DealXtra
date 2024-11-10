package com.dealxtra.dealxtra.api_service;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class VolleyService {

    private static VolleyService instance;
    private RequestQueue requestQueue;
    private SecureStorage secureStorage;  // Add SecureStorage instance to handle token

    // Private constructor
    private VolleyService(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        secureStorage = new SecureStorage(context);  // Initialize SecureStorage
    }

    // Singleton pattern to get an instance of VolleyService
    public static synchronized VolleyService getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyService(context);
        }
        return instance;
    }

    // Method for POST requests with dynamic parameters
    public void postData(String url, Map<String, String> params, final VolleyCallback callback) {
        String action = params.get("action");  // Get the action from params
        JSONObject jsonBody = new JSONObject(params);  // Convert map to JSON

        // Create JsonObjectRequest and set headers
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("JSON Parsing Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Volley Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Skip adding token for signup and signin actions
                if (!"signup".equals(action) && !"login".equals(action)) {
                    String token = secureStorage.getToken();  // Get the token from SecureStorage
                    headers.put("Authorization", "Bearer " + token);  // Add token to headers
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    // Method for GET requests with dynamic parameters
    public void getData(String url, Map<String, String> params, final VolleyCallback callback) {
        String action = params.get("action");  // Get the action from params
        StringBuilder urlWithParams = new StringBuilder(url + "?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlWithParams.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // Create JsonObjectRequest and set headers
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlWithParams.toString(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("JSON Parsing Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Volley Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Skip adding token for signup and signin actions
                if (!"signup".equals(action) && !"signin".equals(action)) {
                    String token = secureStorage.getToken();  // Get the token from SecureStorage
                    headers.put("Authorization", "Bearer " + token);  // Add token to headers
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
