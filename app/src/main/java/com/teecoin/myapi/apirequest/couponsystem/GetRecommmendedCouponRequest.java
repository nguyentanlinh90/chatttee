package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetRecommmendedCouponRequest  extends CouponApiRequest {

    private String id;
    private String params;

    public GetRecommmendedCouponRequest(String id,String params, APIResponseListener listener) {
        super(false, CouponRequestTarget.GET_LIST_RECOMMMENDE_COUPON, listener);
        this.id = id;
        this.params = params;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getCouponCataloguesList(
                String.format(requestTarget.toString(),this.id, this.params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}

