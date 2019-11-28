package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetRecentListRequest extends ReviewApiRequest {

    private LatLng latLng;

    private String countryCode;
    private int page;


    public ReviewUserGetRecentListRequest(int page,LatLng latLng, String countryCode, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_VENDOR_RECENT_LIST, listener);
        this.latLng = latLng;
        this.countryCode = countryCode;
        this.page = page;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getListFavorite(String.format(requestTarget.toString(), getAccountUUID(),
                TCUtils.paramsGetRecent(page, latLng, countryCode)), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
