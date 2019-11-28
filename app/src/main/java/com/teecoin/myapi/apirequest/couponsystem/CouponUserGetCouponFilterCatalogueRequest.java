package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetCouponFilterCatalogueRequest extends CouponApiRequest {

    private FilterCouponModel model;
    private int pageIndex;

    public CouponUserGetCouponFilterCatalogueRequest(FilterCouponModel filterCouponModel, int pageIndex, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_USER_GET_COUPON_FILTER_CATALOGUES, listener);
        this.model = filterCouponModel;
        this.pageIndex = pageIndex;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getFilterCatalogueCoupon(
                String.format(requestTarget.toString(), model.getId(), model.getFilterCouponString(), pageIndex), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
