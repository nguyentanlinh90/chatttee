package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletGetTransactionHistoryListRequest extends WalletApiRequest {

    private int pageIndex;

    public WalletGetTransactionHistoryListRequest(int pageIndex, APIResponseListener listener) {
        super(true,
                TCUtils.isUserApp() ? WalletRequestTarget.GET_USER_TRANSACTION_HISTORY_LIST
                        : WalletRequestTarget.GET_SHOP_TRANSACTION_HISTORY_LIST,
                listener);
        this.pageIndex = pageIndex;
    }

//    public WalletGetTransactionHistoryListRequest(int pageIndex, boolean isLoading, RequestTarget requestTarget, APIResponseListener listener) {
//        super(isLoading, requestTarget, listener);
//        this.pageIndex = pageIndex;
//    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getAccountTransactionHistory(
                String.format(requestTarget.toString(), getAccountUUID(), this.pageIndex), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
