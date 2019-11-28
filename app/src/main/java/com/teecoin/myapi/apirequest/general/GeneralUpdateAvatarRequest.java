package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import okhttp3.MultipartBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralUpdateAvatarRequest extends GeneralApiRequest {

    private MultipartBody.Part filePart;

    public GeneralUpdateAvatarRequest(MultipartBody.Part filePart, GeneralRequestTarget requestTarget, APIResponseListener listener) {
        super(true, requestTarget, listener);
        this.filePart = filePart;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.updateAvatar(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), filePart)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
