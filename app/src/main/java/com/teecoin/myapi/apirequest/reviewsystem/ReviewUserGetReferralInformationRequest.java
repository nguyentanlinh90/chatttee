package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserGetReferralInformationRequest extends ReviewApiRequest {

    private int pageIndex;

    public ReviewUserGetReferralInformationRequest(int pageIndex, APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?ReviewRequestTarget.GET_REFERRAL_INFORMATION:ReviewRequestTarget.GET_REFERRAL_INFORMATION_FOR_SHOP, listener);
        this.pageIndex = pageIndex;
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.getReferralInformation(
                String.format(requestTarget.toString(), getAccountUUID(), pageIndex))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
