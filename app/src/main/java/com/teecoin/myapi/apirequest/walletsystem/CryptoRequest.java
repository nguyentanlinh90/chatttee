package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.TopUpPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CryptoRequest extends WalletApiRequest {

    private TopUpPostModel topUpPostModel;

    public CryptoRequest(TopUpPostModel topUpPostModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.POST_CRYPTO, listener);
        this.topUpPostModel = topUpPostModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.postCrypto(String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), topUpPostModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
