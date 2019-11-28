package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetCuisineRequest extends ReviewApiRequest {

    private double latitude;
    private double longitude;

    public ReviewUserGetCuisineRequest(double latitude, double longitude, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_CUISINES, listener);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getCuisines(String.format(requestTarget.toString(), latitude, longitude))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
