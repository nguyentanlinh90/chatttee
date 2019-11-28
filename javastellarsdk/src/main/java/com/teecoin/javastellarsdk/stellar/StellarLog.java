package com.teecoin.javastellarsdk.stellar;

import android.util.Log;

import com.teecoin.javastellarsdk.BuildConfig;

public class StellarLog {

    private static String tag="TeeCoinStellar";
    public static void d(String msg){
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

}
