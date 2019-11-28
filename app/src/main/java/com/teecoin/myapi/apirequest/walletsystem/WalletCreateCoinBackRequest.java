package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.CoinBackModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletCreateCoinBackRequest extends WalletApiRequest {

    private CoinBackModel coinBackModel;

    public WalletCreateCoinBackRequest(CoinBackModel coinBackModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.CREATE_COIN_BACK, listener);
        this.coinBackModel = coinBackModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.createCoinBack(requestTarget.toString(), getUserToken(), this.coinBackModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
