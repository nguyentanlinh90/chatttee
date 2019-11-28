package com.teecoin.myapi.apirequest.walletsystem.shop;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopFiatTransactionHistoryRequest extends WalletApiRequest {
    private int pageIndex;

    public ShopFiatTransactionHistoryRequest(int pageIndex, APIResponseListener listener) {
        super(true, WalletRequestTarget.SHOP_SGD_TRANSACTION_HISTORY_LIST, listener);
        this.pageIndex = pageIndex;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getSGDTransactionHistory(
                String.format(requestTarget.toString(), getAccountUUID(), this.pageIndex), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
