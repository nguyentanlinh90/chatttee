package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletGetCoinBackDetailRequest extends WalletApiRequest {


    private String transactionId;

    public WalletGetCoinBackDetailRequest(String transactionId, APIResponseListener listener) {
        super(true, WalletRequestTarget.GET_COIN_BACK_DETAIL, listener);
        this.transactionId = transactionId;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getCoinbackDetail(
                String.format(requestTarget.toString(), transactionId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
