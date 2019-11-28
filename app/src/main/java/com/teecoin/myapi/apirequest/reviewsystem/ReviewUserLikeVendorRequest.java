package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.model.reviewsystem.LikeModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserLikeVendorRequest extends ReviewApiRequest {

    private LikeModel likeModel;

    public ReviewUserLikeVendorRequest(LikeModel likeModel, APIResponseListener listener) {
        super(true, ReviewRequestTarget.USER_LIKE_VENDOR, listener);
        this.likeModel = likeModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.userLike(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), this.likeModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
