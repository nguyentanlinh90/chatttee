package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponCheckInListRequest extends CouponApiRequest {

    private VendorCodeCheckInModel vendorCodeCheckInModel;

    public CouponUserGetCouponCheckInListRequest(VendorCodeCheckInModel vendorCodeCheckInModel, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_CHECK_IN_LIST, listener);
        this.vendorCodeCheckInModel = vendorCodeCheckInModel;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getListCouponCheckIn(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), vendorCodeCheckInModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
