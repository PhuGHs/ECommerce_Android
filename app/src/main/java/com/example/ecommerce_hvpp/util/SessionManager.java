package com.example.ecommerce_hvpp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String REF_NAME = "HVPP";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";

    private SharedPreferences sharedPreferences;


    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(REF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String userId, String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.apply();
    }
    public boolean isLoggedIn(String id) {
        return sharedPreferences.contains(id);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
