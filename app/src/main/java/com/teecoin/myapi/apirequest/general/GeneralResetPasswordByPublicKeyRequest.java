package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.ResetPasswordByPublicKeyRequestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralResetPasswordByPublicKeyRequest extends GeneralApiRequest {

    private ResetPasswordByPublicKeyRequestModel resetPasswordByPublicKeyRequestModel;

    public GeneralResetPasswordByPublicKeyRequest(String publicKey, APIResponseListener listener) {
        super(true, GeneralRequestTarget.RESET_PASSWORD, listener);
        this.resetPasswordByPublicKeyRequestModel = new ResetPasswordByPublicKeyRequestModel(publicKey);
    }


    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.resetPasswordByPublicKey(requestTarget.toString(), resetPasswordByPublicKeyRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
