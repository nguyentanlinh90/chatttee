package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponUserGetListOutletRequest extends CouponApiRequest {

    private String idVendor;
    private int page;
    private LatLng latLng;

    public CouponUserGetListOutletRequest(String idVendor,int page,LatLng latLng, APIResponseListener listener) {
        super(true, CouponRequestTarget.GET_OUTLET_LIST, listener);
        this.page = page;
        this.idVendor = idVendor;
        this.latLng = latLng;
    }

    @Override
    public void requestApi(CouponApiManager couponApiManager, Context context) {
        couponApiManager.getListOutlet(String.format(requestTarget.toString(),idVendor, page,latLng.latitude,latLng.longitude), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
