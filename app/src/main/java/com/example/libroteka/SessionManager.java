package com.example.libroteka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    /** Shared Preferences*/

     SharedPreferences pref;

     /**Editor for Shared preferences*/

    SharedPreferences.Editor editor;

    /** Context*/

    Context _context;

    /** Shared pref mode*/

    int PRIVATE_MODE = 0;

    /** Sharedpref file name*/

    private static final String PREF_NAME = "AndroidHivePref";

    /** All Shared Preferences Keys*/

    private static final String IS_LOGIN = "IsLoggedIn";

    public Context getContext() {
        return _context;
    }

    /** Email address (make variable public to access from outside)*/

    public static final String KEY_EMAIL = "email";

    /** Token (make variable public to access from outside)*/

    public static final String KEY_TOKEN = "access_token";

    /** Refresh token (make variable public to access from outside)*/

    public static final String KEY_REFRESH_TOKEN = "refresh_token";

    /** Constructor*/

    public SessionManager(Context context){

        this._context = context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();

    }
    /**

     * Create login session

     * */

    public void createLoginSession(String accessToken, String refreshToken, String email){

    // Storing login value as TRUE

        editor.putBoolean(IS_LOGIN, true);

    // Storing email in pref

        editor.putString(KEY_EMAIL, email);

    // Storing accesstoken  in pref

        editor.putString(KEY_TOKEN, accessToken);

    // Storing refreshtoken  in pref


        editor.putString(KEY_REFRESH_TOKEN, refreshToken);

    // commit changes

        editor.commit();

    }

    /**

     * Get stored session data

     * */

    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<String, String>();

// user email

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

// user token

        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

// user refresh token

        user.put(KEY_REFRESH_TOKEN, pref.getString(KEY_REFRESH_TOKEN, null));

// return user

        return user;

    }

    public void logoutUser(){

// Clearing all data from Shared Preferences

        editor.clear();

        editor.commit();

    }
}

