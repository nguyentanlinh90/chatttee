package com.teecoin.myapi.apirequest.walletsystem.shop;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopCheckPaymentRequest extends WalletApiRequest {
    private String invoince;

    public ShopCheckPaymentRequest(String invoince, APIResponseListener listener) {
        super(true, WalletRequestTarget.SHOP_CHECK_PAYMENT, listener);
        this.invoince = invoince;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.checkPayment(String.format(requestTarget.toString(), invoince), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
