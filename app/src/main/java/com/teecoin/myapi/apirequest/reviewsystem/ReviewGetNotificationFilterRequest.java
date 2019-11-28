package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGetNotificationFilterRequest extends ReviewApiRequest {


    private String id;
    private int type;
    private int page;

    public ReviewGetNotificationFilterRequest(String id, int type, int page, ReviewRequestTarget requestTarget, APIResponseListener listener) {
        super(true, requestTarget, listener);
        this.id = id;
        this.type = type;
        this.page = page;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getLisNotifications(
                String.format(requestTarget.toString(), this.id, this.type, this.page), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
