package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetImageVendorRequest extends ReviewApiRequest {
    private String idVendor;
    private String page;
    private String with_review;
    public GetImageVendorRequest(String idVendor,String page,String with_review,APIResponseListener listener) {
        super(false, ReviewRequestTarget.GET_LIST_IMAGES_VENDOR, listener);
        this.idVendor=idVendor;
        this.page=page;
        this.with_review=with_review;
    }
    @Override
    public void requestApi(ReviewApiManager apiManager, Context context) {
      //  TCLog.e("tu "+String.format(requestTarget.toString(), idVendor,page,with_review));
        apiManager.getListImageVendor(String.format(requestTarget.toString(), idVendor,page,with_review))
                .subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
