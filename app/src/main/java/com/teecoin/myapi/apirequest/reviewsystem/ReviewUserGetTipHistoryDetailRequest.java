package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetTipHistoryDetailRequest extends ReviewApiRequest {

    private String transactionId;

    public ReviewUserGetTipHistoryDetailRequest(String transactionId, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_TIP_HISTORY_DETAIL, listener);
        this.transactionId = transactionId;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getTipHistoryDetail(String.format(requestTarget.toString(), transactionId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
