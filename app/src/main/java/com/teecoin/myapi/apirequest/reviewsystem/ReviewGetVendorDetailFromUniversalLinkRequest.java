package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGetVendorDetailFromUniversalLinkRequest extends ReviewApiRequest {

    private String requestApiUrl;

    public ReviewGetVendorDetailFromUniversalLinkRequest(String requestApiUrl, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_VENDOR_DETAIL, listener);
        this.requestApiUrl = requestApiUrl;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getVendorDetail(requestApiUrl,getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
