package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.model.reviewsystem.PostVendorFavoriteModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserSubmitFavoriteRequest extends ReviewApiRequest {

    private PostVendorFavoriteModel postVendorFavoriteModel;

    public ReviewUserSubmitFavoriteRequest(PostVendorFavoriteModel postVendorFavoriteModel, APIResponseListener listener) {
        super(true, ReviewRequestTarget.SUBMIT_VENDOR_FAVORITE, listener);
        this.postVendorFavoriteModel = postVendorFavoriteModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.submitFavoriteVendor(String.format(requestTarget.toString(),
                getAccountUUID()), getUserToken(), this.postVendorFavoriteModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
