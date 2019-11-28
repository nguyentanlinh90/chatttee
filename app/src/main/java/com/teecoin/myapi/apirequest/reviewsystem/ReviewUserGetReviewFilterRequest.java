package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.feature.reviewSystem.filter.FilterModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetReviewFilterRequest extends ReviewApiRequest {

    private FilterModel filterModel;

    public ReviewUserGetReviewFilterRequest(FilterModel filterModel, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_FILTER_REVIEW, listener);
        this.filterModel = filterModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getFilterReview(String.format(requestTarget.toString(), filterModel.getFilterString()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
