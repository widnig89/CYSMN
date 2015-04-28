package at.sum.android.cysmn.utils;

import android.util.Log;

/**
 * Created by widnig89 on 28.04.15.
 */
public class AppLogger {

    private static String app_name = "AppLogger";

    public static void logDebug(String tag, String message)
    {
        Log.d(app_name, tag + ": \t" + message);
    }
}
