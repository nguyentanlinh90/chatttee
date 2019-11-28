package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGetGoogleReviewRequest extends ReviewApiRequest {

    private String vendorId;

    public ReviewGetGoogleReviewRequest(String vendorId, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_GOOGLE_REVIEW, listener);
        this.vendorId = vendorId;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getGoogleReview(String.format(requestTarget.toString(), this.vendorId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
