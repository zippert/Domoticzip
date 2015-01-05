package org.zippert.domoticz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefUtils {
    private static final String LOGIN_KEY = "login_key";
    private static final String WEBSERVICE_URL_KEY = "address";

    public static void setLoginInfo(Context context, String loginInfo) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(LOGIN_KEY, loginInfo).apply();
    }

    public static String getLoginInfo(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(LOGIN_KEY, null);
    }

    public static void setWebserviceAddress(Context context, String url) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(WEBSERVICE_URL_KEY, url).apply();
    }

    public static String getWebserviceAddress(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(WEBSERVICE_URL_KEY, null);
    }
}
