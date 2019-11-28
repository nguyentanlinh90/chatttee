package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.ResetPasswordByEmailRequestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralResetPasswordByEmailRequest extends GeneralApiRequest {

    private ResetPasswordByEmailRequestModel resetPasswordByEmailRequestModel;

    public GeneralResetPasswordByEmailRequest(String email, APIResponseListener listener) {
        super(true, GeneralRequestTarget.RESET_PASSWORD, listener);
        this.resetPasswordByEmailRequestModel = new ResetPasswordByEmailRequestModel(email);
    }


    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.resetPasswordByEmail(requestTarget.toString(), resetPasswordByEmailRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
