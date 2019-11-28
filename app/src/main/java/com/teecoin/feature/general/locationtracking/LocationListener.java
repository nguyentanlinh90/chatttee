package com.teecoin.feature.general.locationtracking;

import android.location.Location;
import android.os.Bundle;

import com.teecoin.utils.TCLog;

public class LocationListener implements android.location.LocationListener {
    private final String TAG = "LocationListener";
    //    private Location lastLocation = null;
    private Location mLastLocation;
    private LocationChangedListener locationChangedListener;

    public LocationListener(String provider, LocationChangedListener locationChangedListener) {
        mLastLocation = new Location(provider);
        this.locationChangedListener = locationChangedListener;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mLastLocation != null)
            TCLog.d("hung lastLocation lat:" + mLastLocation.getLatitude() + ", lng:" + mLastLocation.getLongitude());
        TCLog.d("hung newLocation lat:" + location.getLatitude() + ", lng:" + location.getLongitude());
        if (mLastLocation != null)
            TCLog.d("hung distant:" + meterDistanceBetweenPoints(mLastLocation.getLatitude(), mLastLocation.getLongitude(), location.getLatitude(), location.getLongitude()));

        if (meterDistanceBetweenPoints(mLastLocation.getLatitude(), mLastLocation.getLongitude(), location.getLatitude(), location.getLongitude()) >= LocationTrackingService.LOCATION_DISTANCE)
            locationChangedListener.onLocationChanged(location);
        mLastLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        TCLog.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        TCLog.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        TCLog.e(TAG, "onStatusChanged: " + status);
    }

    public double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }
}
