package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletCheckPublicKeyExistRequest extends WalletApiRequest {


    private String secretKey;

    public WalletCheckPublicKeyExistRequest(String secretKey, APIResponseListener listener) {
        super(true, WalletRequestTarget.CHECK_SECRET_KEY_EXIST, listener);
        this.secretKey = secretKey;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.checkPublicKeyExist(String.format(requestTarget.toString(), this.secretKey))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
