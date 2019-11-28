package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.ReadNotificationRequestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralNotificationReadRequest extends GeneralApiRequest {

    private ReadNotificationRequestModel readNotificationRequestModel;

    public GeneralNotificationReadRequest(ReadNotificationRequestModel readNotificationRequestModel, GeneralRequestTarget requestTarget, APIResponseListener listener) {
        super(false, requestTarget, listener);
        this.readNotificationRequestModel = readNotificationRequestModel;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.readNotifications(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), readNotificationRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
