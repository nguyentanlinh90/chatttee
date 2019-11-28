package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGiveAwayRequest extends CouponApiRequest {

    public CouponUserGiveAwayRequest(APIResponseListener listener) {
        super(true, CouponRequestTarget.GET_GIVE_AWAY, listener);
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getDailyGiveAway(
                requestTarget.toString(), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
