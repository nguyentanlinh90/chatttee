package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralCheckSecretKeyRequest extends GeneralApiRequest {

    public GeneralCheckSecretKeyRequest(APIResponseListener listener) {
        super(false, GeneralRequestTarget.GENERAL_CHECK_SECRET_KEY, listener);
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.checkSecretKey(
                String.format(requestTarget.toString(), getAccountUUID()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
