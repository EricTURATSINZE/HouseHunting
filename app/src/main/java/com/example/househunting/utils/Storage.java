package com.example.househunting.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "HouseHuntingApp";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String USER_TOKEN = "USER_TOKEN";
    private static final String PREFERRED_HOUSES= "PREFERRED_HOUSES";
    private static final String SEEN_HOUSES="SEEN_HOUSES";

    public Storage(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public void setSeenHouses(String seenHouses) {
        editor.putString(SEEN_HOUSES, seenHouses);
    }

    public String getSeenHouses() {
        return pref.getString(SEEN_HOUSES, "");
    }

    public void setPreferredHouses(String preferredHouses) {
        editor.putString(PREFERRED_HOUSES, preferredHouses);
    }

    public String getPreferredHouses() {
        return pref.getString(PREFERRED_HOUSES, "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getToken() {
        return pref.getString(USER_TOKEN, "");
    }

    public void setToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }
}
