package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletGetTransactionDetailRequest extends WalletApiRequest {


    private String transactionHash;

    public WalletGetTransactionDetailRequest(String transactionHash, APIResponseListener listener) {
        super(false, WalletRequestTarget.GET_TRANSACTION_DETAIL, listener);
        this.transactionHash = transactionHash;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getTransactionDetail(
                String.format(requestTarget.toString(), this.transactionHash))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
