package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetListCheckinRewardsRequest  extends ReviewApiRequest {

    private String params;

    public GetListCheckinRewardsRequest( LatLng latLng, LatLng latLngSearch, String countryCode, String distance, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_LIST_CHECKIN_REWARDS, listener);
        this.params = TCUtils.paramsGetVendorList(1, TCConstant.PAGE_SIZE_10, latLng, latLngSearch, countryCode,
                "", distance, "", "", "",
                EnumMgr.SortCondition.NearMe.getValue(), "");
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getListCheckinRewards(String.format(requestTarget.toString(), params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }

}
