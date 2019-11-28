package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetPromotionRequest extends ReviewApiRequest {


    public ReviewUserGetPromotionRequest(APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_PROMOTION, listener);
    }

    public ReviewUserGetPromotionRequest(boolean isLoading, APIResponseListener listener) {
        super(isLoading, ReviewRequestTarget.GET_PROMOTION, listener);
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getPromotion(requestTarget.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
