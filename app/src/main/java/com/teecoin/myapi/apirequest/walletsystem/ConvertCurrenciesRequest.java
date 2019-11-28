package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConvertCurrenciesRequest extends WalletApiRequest {

    private String base;
    private String to;
    private String amount;

    public ConvertCurrenciesRequest(String base, String to, String amount, APIResponseListener listener) {
        super(true, WalletRequestTarget.CONVERT_CURRENCIES, listener);
        this.base = base;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getConvertCurrencies(String.format(requestTarget.toString(), base, to, amount), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
