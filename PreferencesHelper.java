package com.example.personalitytest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.personalitytest.models.User;

public class PreferencesHelper {
    private static final String PREFS_NAME = "PersonalityTestPrefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_ADMIN = "is_admin";
    
    private final SharedPreferences preferences;
    
    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveLoginState(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(KEY_IS_ADMIN, user.isAdmin());
        editor.apply();
    }
    
    public void clearLoginState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_IS_ADMIN);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public User getSavedUser() {
        if (!isLoggedIn()) {
            return null;
        }
        
        return new User(
            preferences.getInt(KEY_USER_ID, 0),
            preferences.getString(KEY_USERNAME, ""),
            preferences.getString(KEY_EMAIL, ""),
            preferences.getBoolean(KEY_IS_ADMIN, false)
        );
    }
}