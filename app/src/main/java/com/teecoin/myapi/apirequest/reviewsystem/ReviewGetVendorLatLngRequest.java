package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGetVendorLatLngRequest extends ReviewApiRequest {

    private int page;
    private LatLng latLng;
    private String distance;
    private String timezones;

    public ReviewGetVendorLatLngRequest(int page, LatLng latLng, String distance, String timezones, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_VENDORS_LAT_LNG, listener);
        this.page = page;
        this.latLng = latLng;
        this.distance = distance;
        this.timezones = timezones;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getVendors(
                String.format(
                        requestTarget.toString(), this.page,
                        this.latLng.latitude,
                        this.latLng.longitude,
                        this.distance,
                        this.timezones))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
