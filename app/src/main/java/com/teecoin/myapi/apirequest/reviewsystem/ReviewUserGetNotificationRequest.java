package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetNotificationRequest extends ReviewApiRequest {

    private int page;

    public ReviewUserGetNotificationRequest(int page, APIResponseListener listener) {
        super(true,
                TCUtils.isUserApp() ? ReviewRequestTarget.USER_GET_NOTIFY : ReviewRequestTarget.SHOP_GET_NOTIFY,
                listener);
        this.page = page;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getLisNotifications(
                String.format(requestTarget.toString(), getAccountUUID(), this.page), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
