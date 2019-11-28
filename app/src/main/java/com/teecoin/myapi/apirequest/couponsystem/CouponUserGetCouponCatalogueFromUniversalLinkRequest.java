package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponCatalogueFromUniversalLinkRequest extends CouponApiRequest {


    private String requestApiUrl;

    public CouponUserGetCouponCatalogueFromUniversalLinkRequest(String requestApiUrl, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUES_LIST, listener);
        this.requestApiUrl = requestApiUrl;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getCouponCataloguesList(requestApiUrl, getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
