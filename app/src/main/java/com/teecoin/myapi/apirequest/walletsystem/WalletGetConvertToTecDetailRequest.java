package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletGetConvertToTecDetailRequest extends WalletApiRequest {


    private String transactionId;

    public WalletGetConvertToTecDetailRequest(String transactionId, APIResponseListener listener) {
        super(true, WalletRequestTarget.GET_CONVERT_TO_TEC_DETAIL, listener);
        this.transactionId = transactionId;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getConvertToTecDetail(
                String.format(requestTarget.toString(), this.transactionId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }
}
