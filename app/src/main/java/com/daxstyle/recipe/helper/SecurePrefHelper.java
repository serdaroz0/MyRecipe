package com.daxstyle.recipe.helper;

import android.content.Context;

/**
 * Created by Ussal Software on 08.06.2018.
 */
public class SecurePrefHelper {

    //region Description /* Prefs */

    private static SecurePreferences getPrefClass(Context context) {
        return new SecurePreferences(context, "app", "M8d3#sdfwwwerdw*qwS1mn2sFRs58g2", true);
    }

    public static String getPrefString(Context context, String key, String defaultVal) {
        return getPrefClass(context).getString(key, defaultVal);
    }

    public static void setPrefString(Context context, String key, String val) {
        getPrefClass(context).put(key, val);
    }

    public static boolean getPrefBoolean(Context c, String s) {
        return Boolean.parseBoolean(getPrefString(c, s, "false"));
    }

    public static int getPrefInt(Context c, String s) {
        return Integer.parseInt(getPrefString(c, s, "-1"));
    }

    public static long getPrefLong(Context c, String s) {
        return Long.parseLong(getPrefString(c, s, "-1"));
    }

    public static float getPrefFloat(Context c, String s) {
        return Float.parseFloat(getPrefString(c, s, "-1"));
    }

    public static void setPrefBoolean(Context c, String s, boolean val) {
        setPrefString(c, s, Boolean.toString(val));
    }

    public static void setPrefInt(Context c, String s, int val) {
        setPrefString(c, s, Integer.toString(val));
    }

    public static void setPrefLong(Context c, String s, long val) {
        setPrefString(c, s, Long.toString(val));
    }

    public static void setPrefFloat(Context c, String s, float val) {
        setPrefString(c, s, Float.toString(val));
    }

}
