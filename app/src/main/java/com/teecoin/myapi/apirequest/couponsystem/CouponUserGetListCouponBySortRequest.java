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

public class CouponUserGetListCouponBySortRequest extends CouponApiRequest {

    private String params;

    public CouponUserGetListCouponBySortRequest(String params, APIResponseListener listener) {
        super(false, TCUtils.isEmpty(params) ? CouponRequestTarget.GET_LIST_COUPON_DEFAULT : CouponRequestTarget.GET_LIST_COUPON_SORT_TYPE, listener);
        this.params = params;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getCouponCataloguesList(
                String.format(requestTarget.toString(), this.params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
