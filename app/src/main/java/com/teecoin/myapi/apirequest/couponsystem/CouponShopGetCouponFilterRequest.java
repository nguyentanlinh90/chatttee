package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponShopGetCouponFilterRequest extends com.teecoin.myapi.apirequest.CouponApiRequest {

    private FilterCouponModel filterCouponModel;

    public CouponShopGetCouponFilterRequest(FilterCouponModel filterCouponModel, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_SHOP_GET_COUPON_FILTER, listener);
        this.filterCouponModel = filterCouponModel;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getFilterShopCoupon(
                String.format(requestTarget.toString(), getAccountUUID(), filterCouponModel.getFilterCouponString()), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
