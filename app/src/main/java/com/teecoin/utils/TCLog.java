package com.teecoin.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.teecoin.base.TCApplication;

import core.base.BaseActivity;
import core.base.BaseFragment;

public class TCLog {

    //    private static final boolean isDebug = BuildConfig.DEBUG;
    private static final String tag = "TeeCoinLog";

    private static String getTag() {
        AppCompatActivity activity = TCApplication.getActiveActivity();
        if (activity != null && activity instanceof BaseActivity) {
            BaseFragment fragment = ((BaseActivity) activity).getTopFragment(((BaseActivity) activity).getFragmentContainerResId());
            if (fragment != null)
                return fragment.getClass().getSimpleName();
        }
        return tag;
    }

    public static void d(String message) {
        if (TCUtils.isShowLog())
            Log.d(getTag(), message);
    }

    public static void d(String tag, String message) {
        if (TCUtils.isShowLog())
            Log.d(tag, message);
    }

    public static void e(String message) {
        if (TCUtils.isShowLog())
            Log.e(getTag(), message);
    }

    public static void e(String tag, String message) {
        if (TCUtils.isShowLog())
            Log.e(tag, message);
    }

    public static void i(String message) {
        if (TCUtils.isShowLog())
            Log.i(getTag(), message);
    }

    public static void i(String tag, String message) {
        if (TCUtils.isShowLog())
            Log.i(tag, message);
    }
}
