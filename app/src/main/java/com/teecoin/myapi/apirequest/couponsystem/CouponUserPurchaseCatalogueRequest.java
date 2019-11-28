package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.model.couponsystem.CouponCataloguePurchaseRequestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserPurchaseCatalogueRequest extends CouponApiRequest {

    private CouponCataloguePurchaseRequestModel purchaseRequestModel;

    public CouponUserPurchaseCatalogueRequest(CouponCataloguePurchaseRequestModel purchaseRequestModel, APIResponseListener listener) {
        super(false, CouponRequestTarget.COUPON_USER_CATALOGUE_PURCHASE, listener);
        this.purchaseRequestModel = purchaseRequestModel;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.userCataloguePurchase(requestTarget.toString(), getUserToken(), purchaseRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
