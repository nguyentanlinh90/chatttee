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

public class ReviewUserGetFavoriteListRequest extends ReviewApiRequest {

    private int pageIndex;

    private LatLng latLng;

    private String countryCode;

    public ReviewUserGetFavoriteListRequest(int pageIndex, LatLng latLng, String countryCode, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_VENDOR_FAVORITE_LIST, listener);
        this.pageIndex = pageIndex;
        this.latLng = latLng;
        this.countryCode = countryCode;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getListFavorite(String.format(requestTarget.toString(), getAccountUUID(),
                TCUtils.paramsGetRecent(pageIndex, latLng, countryCode)), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
