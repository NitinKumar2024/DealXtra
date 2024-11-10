package com.dealxtra.dealxtra.api_service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.nio.charset.StandardCharsets;

public class SecureStorage {

    private static final String PREFS_NAME = "secure_prefs";
    private static final String NAME_KEY = "user_name";
    private static final String EMAIL_KEY = "user_email";
    private static final String TOKEN_KEY = "user_token";

    private SharedPreferences sharedPreferences;

    public SecureStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Encode data to Base64 before saving
    private String encodeData(String data) {
        return Base64.encodeToString(data.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }

    // Decode data from Base64 after retrieving
    private String decodeData(String data) {
        return new String(Base64.decode(data, Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    // Save user data securely
    public void saveUserData(String name, String email, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_KEY, encodeData(name));
        editor.putString(EMAIL_KEY, encodeData(email));
        editor.putString(TOKEN_KEY, encodeData(token));
        editor.apply();  // Save changes
    }

    // Get user data securely
    public String getName() {
        return decodeData(sharedPreferences.getString(NAME_KEY, ""));
    }

    public String getEmail() {
        return decodeData(sharedPreferences.getString(EMAIL_KEY, ""));
    }

    public String getToken() {
        return decodeData(sharedPreferences.getString(TOKEN_KEY, ""));
    }

    // Clear user data
    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(NAME_KEY);
        editor.remove(EMAIL_KEY);
        editor.remove(TOKEN_KEY);
        editor.apply();  // Save changes
    }
}
