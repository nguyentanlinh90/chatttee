package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.model.reviewsystem.ReviewShopModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserSubmitReviewShopRequest extends ReviewApiRequest {

    private ReviewShopModel reviewShopModel;

    public ReviewUserSubmitReviewShopRequest(ReviewShopModel reviewShopModel, APIResponseListener listener) {
        super(false, ReviewRequestTarget.SUBMIT_USER_REVIEW_SHOP, listener);
        this.reviewShopModel = reviewShopModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.submitUserReviewShop(String.format(requestTarget.toString(),
                getAccountUUID()), getUserToken(), this.reviewShopModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
