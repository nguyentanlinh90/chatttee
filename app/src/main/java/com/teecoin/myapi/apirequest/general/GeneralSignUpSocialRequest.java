package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.SocialInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralSignUpSocialRequest extends GeneralApiRequest {
    private SocialInfoModel accountSocialModel;

    public GeneralSignUpSocialRequest(SocialInfoModel accountSocialModel, GeneralRequestTarget requestTarget, APIResponseListener listener) {
        super(true, requestTarget, listener);
        this.accountSocialModel = accountSocialModel;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.signUpSocial(requestTarget.toString(), getUserToken(), accountSocialModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
