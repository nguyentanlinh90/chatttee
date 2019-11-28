package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import java.io.Serializable;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewUserCheckReferralCodeRequest extends ReviewApiRequest {

    private CheckReferralCodeModel checkReferralCodeModel;

    public ReviewUserCheckReferralCodeRequest(String referralCode, APIResponseListener listener) {
        super(true, ReviewRequestTarget.CHECK_REFERRAL_CODE, listener);
        this.checkReferralCodeModel = new CheckReferralCodeModel(referralCode);
    }

    @Override
    public void requestApi(ReviewApiManager reviewApiManager, Context context) {
        reviewApiManager.checkReferralCode(requestTarget.toString(), checkReferralCodeModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }


    public class CheckReferralCodeModel extends TeeCoinModel implements Serializable {

        @SerializedName("referral_code")
        @Expose
        private String referral_code;

        public CheckReferralCodeModel(String referral_code) {
            this.referral_code = referral_code;
        }
    }

}
