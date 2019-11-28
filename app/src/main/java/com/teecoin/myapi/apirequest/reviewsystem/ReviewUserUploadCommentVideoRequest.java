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

public class ReviewUserUploadCommentVideoRequest extends ReviewApiRequest {

    private MultipartBody.Part[] files;
    private String uuidVideo;

    public ReviewUserUploadCommentVideoRequest(MultipartBody.Part[] files, String uuidVideo, APIResponseListener listener) {
        super(false, ReviewRequestTarget.UPLOAD_COMMENT_VIDEO, listener);
        this.files = files;
        this.uuidVideo = uuidVideo;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.uploadCommentVideo(requestTarget.toString(), this.files, this.uuidVideo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
