package com.recycler.walletapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static PreferencesManager mInstance;
    private static Context ctx;
    private static final String SHARED_PREFERENCE_NAME = Constants.PREFERENCE_NAME;
    private static final String KEY_USERNAME = Constants.PREFERENCE_NAME;
    private static final String KEY_ID = Constants.ID;
    private static final String KEY_EMAIL = Constants.EMAIL;
    private static final String KEY_PASSWORD = Constants.PASSWORD;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    private PreferencesManager(Context context) {
        ctx = context;
    }

    public static synchronized PreferencesManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new PreferencesManager(context);

        }
        return mInstance;
    }

    public boolean UserLogin(String id,/*String UserName,*/ String Email, String Password) {
        sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_PASSWORD, Password);
        editor.apply();
        return true;
    }

    public boolean UserRegister(String id,/*String UserName,*/ String Email, String Password) {
        sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_PASSWORD, Password);
        editor.apply();
        return true;
    }
    public boolean IsLoggedIn() {
        sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_EMAIL, null) != null) {
            return true;

        }
        return false;
    }

    public boolean Logout() {
        sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }

    public String getId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID,null);

    }
    public String getEmail(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null);

    }
    public String getPassword(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD,null);

    }

}
