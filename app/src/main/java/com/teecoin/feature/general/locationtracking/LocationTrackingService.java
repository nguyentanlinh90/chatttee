package com.teecoin.feature.general.locationtracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import com.teecoin.base.TCApplication;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.retrofit.RetrofitGenerator;
import com.teecoin.retrofit.URL;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LocationTrackingService extends Service implements LocationChangedListener, APIResponseListener {

    public static final int LOCATION_DISTANCE = 50;
    private static final String TAG = "hung";
    private static final int LOCATION_INTERVAL = 1000;
    private final LocationTrackingService.LocationServiceBinder binder = new LocationTrackingService.LocationServiceBinder();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private GeneralApiManager generalApiManager;

//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public LocationTrackingService(String name) {
//        super(name);
//    }
//
//    public LocationTrackingService() {
//        super("LocationTrackingService");
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        TCLog.d("hung LocationTrackingService onHandleIntent");
//        generalApiManager = RetrofitGenerator.createService(GeneralApiManager.class, URL.getServer());
//        startTracking();
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TCLog.d("hung LocationTrackingService onStartCommand");
        generalApiManager = RetrofitGenerator.createService(GeneralApiManager.class, URL.getServer());
        startTracking();

        return super.onStartCommand(intent, flags, startId);

    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {
        TCLog.d("hung LocationTrackingService startTracking 1");
        initializeLocationManager();
        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER, this);

        try {
            TCLog.d("hung LocationTrackingService requestLocationUpdates 1");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
            TCLog.d("hung LocationTrackingService requestLocationUpdates 2");
        } catch (java.lang.SecurityException ex) {
            TCLog.d(TAG, "fail to request location update, ignore" + ex);
        } catch (IllegalArgumentException ex) {
            TCLog.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception ex) {
                TCLog.d(TAG, "fail to remove location listners, ignore" + ex);
            }
        }
    }

    public void stopTracking() {
        this.onDestroy();
    }

    private void requestApi(CurrentLocationModel location) {
        if (TCUtils.isNetworkConnectionAvailable()) {
            if (generalApiManager == null)
                generalApiManager = RetrofitGenerator.createService(GeneralApiManager.class, URL.getServer());
            AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
            if (accountModel != null && !TCUtils.isEmpty(accountModel.getUuid())) {
                generalApiManager.locationTrack(
                        String.format(WalletRequestTarget.LOCATION_TRACK.toString(), accountModel.getUuid()),
                        getUserToken(), location)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MyApiSubscribe<Object>(TCApplication.getActiveActivity(), this, WalletRequestTarget.LOCATION_TRACK));
            }
        }
    }

    public String getUserToken() {
        return TCConstant.TOKEN + TCSharePreferenceManager.getInstance().getString(DataKey.Token);
    }


    @Override
    public void onLocationChanged(Location location) {
        TCLog.d("hung onLocationChanged: lat:" + location.getLatitude() + ", lng:" + location.getLongitude());
        requestApi(new CurrentLocationModel(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        TCLog.d("hung request success");
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        TCLog.d("hung request fail");
    }

    public class LocationServiceBinder extends Binder {
        public LocationTrackingService getService() {
            return LocationTrackingService.this;
        }
    }
}
