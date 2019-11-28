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

public class ReviewGetCuisineListByIdRequest extends ReviewApiRequest {

    private int page;
    private LatLng latLng;
    private String distance;
    private String cuisine;
    private String timezone;

    public ReviewGetCuisineListByIdRequest(int page, LatLng latLng, String distance, String cuisine, String timezone, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_LIST_CUISINES_BY_ID, listener);
        this.page = page;
        this.latLng = latLng;
        this.distance = distance;
        this.cuisine = cuisine;// or options if call from ReviewForUser
        this.timezone = timezone;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getCuisineListByID(
                String.format(
                        requestTarget.toString(),
                        this.page,
                        this.latLng.latitude,
                        this.latLng.longitude,
                        this.distance,
                        this.cuisine,
                        this.timezone))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
