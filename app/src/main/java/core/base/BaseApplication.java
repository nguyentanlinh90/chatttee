package core.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;

public class BaseApplication extends MultiDexApplication {

    private static Context mContext;
    private static AppCompatActivity mActiveActivity;

    public static Context getContext() {
        return mContext;
    }

    public static AppCompatActivity getActiveActivity() {

        return mActiveActivity;
    }

    public static void setActiveActivity(AppCompatActivity active) {
        mActiveActivity = active;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

}
