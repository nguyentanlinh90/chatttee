package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletLoginAccountRequest extends WalletApiRequest {


    private AccountModel accountModel;

    public WalletLoginAccountRequest(AccountModel accountModel, APIResponseListener listener) {
        super(true,
                TCUtils.isUserApp() ? WalletRequestTarget.LOGIN_USER_ACCOUNT
                        : WalletRequestTarget.LOGIN_SHOP_ACCOUNT,
                listener);
        this.accountModel = accountModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.logInAccount(requestTarget.toString(), accountModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
