package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponListRequest extends CouponApiRequest {

    private int pageIndex;

    public CouponUserGetCouponListRequest(int pageIndex, APIResponseListener listener) {
        super(false, CouponRequestTarget.COUPON_USER_GET_COUPON_LIST, listener);
        this.pageIndex = pageIndex;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getUserCouponList(
                String.format(requestTarget.toString(), getAccountUUID(), pageIndex), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
