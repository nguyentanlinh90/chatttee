package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.RegisterNotifyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralNotificationRegistrationRequest extends GeneralApiRequest {

    private RegisterNotifyModel registerNotifyModel;

    public GeneralNotificationRegistrationRequest(RegisterNotifyModel registerNotifyModel, GeneralRequestTarget requestTarget, APIResponseListener listener) {
        super(false, requestTarget, listener);
        this.registerNotifyModel = registerNotifyModel;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.accountNotificationRegistration(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), this.registerNotifyModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
