package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import okhttp3.MultipartBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserUploadCommentImageRequest extends ReviewApiRequest {

    private MultipartBody.Part[] files;

    public ReviewUserUploadCommentImageRequest(MultipartBody.Part[] files, APIResponseListener listener) {
        super(true, ReviewRequestTarget.UPLOAD_COMMENT_IMAGE, listener);
        this.files = files;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.uploadCommentImage(requestTarget.toString(), files)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
