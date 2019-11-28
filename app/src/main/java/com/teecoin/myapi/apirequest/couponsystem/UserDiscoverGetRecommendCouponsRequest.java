package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDiscoverGetRecommendCouponsRequest extends ReviewApiRequest {
    private String params="";

    public UserDiscoverGetRecommendCouponsRequest(LatLng latLng, String countryCode, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_RECOMMEND, listener);
        this.params = TCUtils.paramsToGetCategory(1,"", latLng, countryCode, "", null);
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiRequest, Context context) {
        reviewApiRequest.getCouponRecommendations(String.format(requestTarget.toString(),
                params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
