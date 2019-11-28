package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetVisitTypeRequest extends ReviewApiRequest {


    public GetVisitTypeRequest(APIResponseListener listener) {
        super(true, ReviewRequestTarget.REVIEW_GET_VISIT_TYPE, listener);
    }

    @Override
    public void requestApi(ReviewApiManager apiManager, Context context) {
        apiManager.getVisitType(String.format(requestTarget.toString()))
                .subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
