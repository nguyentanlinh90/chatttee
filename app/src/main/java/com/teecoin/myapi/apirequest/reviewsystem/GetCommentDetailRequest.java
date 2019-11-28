package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetCommentDetailRequest  extends ReviewApiRequest {
    private String idComment;
    public GetCommentDetailRequest(String idComment, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_COMMENT_DETAIL, listener);
        this.idComment=idComment;
    }
    @Override
    public void requestApi(ReviewApiManager apiManager, Context context) {
        apiManager.getCommentDetail(String.format(requestTarget.toString(), idComment,getAccountUUID()),
                getUserToken()).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
