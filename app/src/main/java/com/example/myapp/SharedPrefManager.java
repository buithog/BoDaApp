package com.example.myapp;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "UserInfo";
    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUserInfo(String username, String firstname, String lastname, String token, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("token", token);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getFirstname() {
        return sharedPreferences.getString("firstname", null);
    }

    public String getLastname() {
        return sharedPreferences.getString("lastname", null);
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString("refreshToken", null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
