package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponDetailFromTransactionRequest extends CouponApiRequest {

    private String transactionId;
    private String serial;

    public CouponUserGetCouponDetailFromTransactionRequest(String transactionId, String serial, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_DETAIL_FROM_TRANSACTION, listener);
        this.transactionId = transactionId;
        this.serial = serial;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getCouponDetailFromTransaction(
                String.format(requestTarget.toString(), transactionId, serial), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
