package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserCheckInNotificationDetailRequest extends CouponApiRequest {

    private String id;

    public CouponUserCheckInNotificationDetailRequest(String id, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_CHECK_INS_NOTIFICATION_DETAIL, listener);
        this.id = id;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.checkinNotification(
                String.format(requestTarget.toString(), id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
