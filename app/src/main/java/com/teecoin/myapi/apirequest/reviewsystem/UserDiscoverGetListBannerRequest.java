package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDiscoverGetListBannerRequest  extends ReviewApiRequest {

    private String params;

    public UserDiscoverGetListBannerRequest(String params, APIResponseListener listener) {
        super(true, ReviewRequestTarget.DISCOVER_BANNERS, listener);
        this.params = params;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getListDiscoverBanner(String.format(requestTarget.toString(), params), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
