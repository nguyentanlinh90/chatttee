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

public class ReviewUserGetNewMerchantRequest extends ReviewApiRequest {

    private int page;
    private LatLng latLng;
    private String distance;
    private String options;
    private String timezones;


    public ReviewUserGetNewMerchantRequest(int page, String distance, String options, String timezones, LatLng latLng, boolean isLoading, APIResponseListener listener) {
        super(isLoading, ReviewRequestTarget.GET_NEW_MERCHANTS, listener);//TODO: RequestTarget.GET_NEW_MERCHANTS_LAT_LNG should be correct
        this.page = page;
        this.distance = distance;
        this.options = options;
        this.timezones = timezones;
        this.latLng = latLng;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getNewMerchants(
                String.format(
                        requestTarget.toString(),
                        this.page,
                        this.distance,
                        this.options,
                        this.timezones,
                        this.latLng.latitude,
                        this.latLng.longitude))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
