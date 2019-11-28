package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponExpiredRedeemedListRequest extends CouponApiRequest {

    private int pageIndex;
    private int type;

    public CouponUserGetCouponExpiredRedeemedListRequest(int pageIndex, int type, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_EXPIRED_REDEEMED_LIST, listener);
        this.pageIndex = pageIndex;
        this.type = type;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getUserCouponList(
                String.format(requestTarget.toString(), getAccountUUID(), pageIndex, this.type), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
