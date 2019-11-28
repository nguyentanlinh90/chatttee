package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.utils.TCDateUtility;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewShopGetVendorDetailRequest extends ReviewApiRequest {

    private String id;

    public ReviewShopGetVendorDetailRequest(String id, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_VENDOR_DETAIL, listener);
        this.id = id;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getVendorDetail(
                String.format(requestTarget.toString(), this.id, TCDateUtility.getTimeZone()),getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }

}
