package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewShopGetVendorReviewRequest extends ReviewApiRequest {

    private String id;
    private int page;

    public ReviewShopGetVendorReviewRequest(String id, int page, APIResponseListener listener) {
        super(false, ReviewRequestTarget.GET_VENDOR_REVIEWS, listener);
        this.id = id;
        this.page = page;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getVendorReviews(
                String.format(
                        requestTarget.toString(),
                        this.id,
                        getAccountUUID(),
                        this.page))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
