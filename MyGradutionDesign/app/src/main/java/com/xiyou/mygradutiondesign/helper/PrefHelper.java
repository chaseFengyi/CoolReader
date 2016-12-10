package com.xiyou.mygradutiondesign.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fengyi on 16/3/20.
 */
public class PrefHelper {

    private static final String defaultPrefName = "traffic_design.pref";

    private static SharedPreferences preferences;

    public static void put(String key, String value) {
        getPref().edit().putString(key, value).commit();
    }

    public static void put(String key, int value) {
        getPref().edit().putInt(key, value).commit();
    }

    public static void put(String key, boolean value) {
        getPref().edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return getPref().getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPref().getBoolean(key, defValue);
    }

    private static String preName = defaultPrefName;

    private static SharedPreferences getPref() {
        if (preferences == null) {
            preferences = ApplicationHelper.getApplication().getSharedPreferences(preName,
                    Context.MODE_PRIVATE);
        }
        return preferences;
    }

}
