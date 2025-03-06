package com.example.todoapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "TodoAppSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String email, String fullName) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getUserFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }

    public void updateUserDetails(String email, String fullName) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.apply();
    }
}