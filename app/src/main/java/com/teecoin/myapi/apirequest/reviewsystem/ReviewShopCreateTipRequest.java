package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.feature.reviewSystem.tip.TipModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewShopCreateTipRequest extends ReviewApiRequest {

    private TipModel tipModel;

    public ReviewShopCreateTipRequest(TipModel tipModel, APIResponseListener listener) {
        super(true, ReviewRequestTarget.CREATE_TIP, listener);
        this.tipModel = tipModel;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.createTip(requestTarget.toString(), getUserToken(), this.tipModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<>(context, listener, requestTarget));
    }
}
