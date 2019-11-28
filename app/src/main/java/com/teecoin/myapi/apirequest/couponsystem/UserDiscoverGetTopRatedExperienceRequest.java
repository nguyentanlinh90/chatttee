package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDiscoverGetTopRatedExperienceRequest extends CouponApiRequest {

    private String params;

    public UserDiscoverGetTopRatedExperienceRequest(LatLng currentLatLng, String countryCode, APIResponseListener listener) {
        super(true, CouponRequestTarget.TOP_RATED_EXPERIENCES, listener);
        this.params = TCUtils.paramsGetVendorList(1, TCConstant.PAGE_SIZE_10, currentLatLng, null, countryCode,
                "", "", "", "", "", EnumMgr.SortCondition.TopRatedExperiences.getValue(), "");
    }


    @Override
    public void requestApi(CouponApiManager apiManager, Context context) {
        apiManager.getTopRatedExperience(String.format(requestTarget.toString(), params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
