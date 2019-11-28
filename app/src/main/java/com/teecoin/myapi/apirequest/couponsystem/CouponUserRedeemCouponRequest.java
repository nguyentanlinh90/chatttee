package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserRedeemCouponRequest extends CouponApiRequest {


    private VendorCodeRedeemModel model;

    public CouponUserRedeemCouponRequest(VendorCodeRedeemModel vendorCodeRedeemModel, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_REDEEM_COUPON, listener);
        this.model = vendorCodeRedeemModel;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.userRedeemCoupon(
                String.format(requestTarget.toString(), model.getTypeRedeem(), model.getId()), getUserToken(), model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
