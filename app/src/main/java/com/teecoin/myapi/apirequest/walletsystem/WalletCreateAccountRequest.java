package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WalletCreateAccountRequest extends WalletApiRequest {


    private AccountModel accountModel;

    public WalletCreateAccountRequest(AccountModel accountModel, BaseRequestTarget requestTarget, APIResponseListener listener) {
        super(true, requestTarget, listener);
        this.accountModel = accountModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        if (requestTarget == WalletRequestTarget.CREATE_USER) {
            walletApiManager.createUserAccount(requestTarget.toString(), accountModel)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
        } else if (requestTarget == WalletRequestTarget.CREATE_SHOP) {
            walletApiManager.createShopAccount(requestTarget.toString(), accountModel)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
        }
    }
}
