package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDiscoverSubmitBannerClickRequest extends ReviewApiRequest {

    private DiscoverBannerModel discoverBannerModel;

    public UserDiscoverSubmitBannerClickRequest(DiscoverBannerModel discoverBannerModel, APIResponseListener listener) {
        super(true, ReviewRequestTarget.DISCOVER_BANNERS, listener);
        this.discoverBannerModel = discoverBannerModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.submitBannerClick(requestTarget.toString(), getUserToken(), this.discoverBannerModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
