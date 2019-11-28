package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetNotificationDetailRequest extends CouponApiRequest {

    private String id;
    private String serial;

    public CouponUserGetNotificationDetailRequest(String id, String serial, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_NOTIFICATION_DETAIL, listener);
        this.id = id;
        this.serial = serial;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getUserCouponDetail(
                String.format(requestTarget.toString(), id, serial), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
