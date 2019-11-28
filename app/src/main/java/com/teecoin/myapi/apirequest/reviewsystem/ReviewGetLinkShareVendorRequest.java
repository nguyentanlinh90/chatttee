package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGetLinkShareVendorRequest  extends ReviewApiRequest {
    String vendorID;
    public ReviewGetLinkShareVendorRequest(String vendorID,APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_LINK_SHARE_VENDOR, listener);
        this.vendorID=vendorID;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getLinkShareVendor(String.format(requestTarget.toString(),vendorID),getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
