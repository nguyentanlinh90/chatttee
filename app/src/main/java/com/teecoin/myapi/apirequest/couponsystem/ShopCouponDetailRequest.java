package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopCouponDetailRequest extends CouponApiRequest {
    private int idCoupon;

    public ShopCouponDetailRequest(int idCoupon, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_SHOP_GET_COUPON_DETAIL, listener);
        this.idCoupon = idCoupon;
    }

    @Override
    public void requestApi(CouponApiManager apiManager, Context context) {
        apiManager.getShopCouponDetail(
                String.format(requestTarget.toString(), getAccountUUID(), this.idCoupon), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
