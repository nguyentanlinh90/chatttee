package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.CheckVerificationCodeModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralCheckVerificationCodeRequest extends GeneralApiRequest {

    private String verificationCode;

    public GeneralCheckVerificationCodeRequest(String verificationCode, APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?GeneralRequestTarget.GENERAL_CHECK_VERIFICATION_CODE:GeneralRequestTarget.GENERAL_CHECK_VERIFICATION_CODE_FOR_SHOP, listener);
        this.verificationCode = verificationCode;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.checkVerificationCode(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(),
                new CheckVerificationCodeModel(verificationCode))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
