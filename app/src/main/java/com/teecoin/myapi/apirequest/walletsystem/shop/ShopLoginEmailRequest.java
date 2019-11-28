package com.teecoin.myapi.apirequest.walletsystem.shop;

import android.content.Context;

import com.teecoin.model.walletsystem.LoginEmailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopLoginEmailRequest extends WalletApiRequest {


    private LoginEmailModel loginEmailModel;

    public ShopLoginEmailRequest(LoginEmailModel loginEmailModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.SHOP_LOGIN_EMAIL, listener);
        this.loginEmailModel = loginEmailModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.loginEmail(requestTarget.toString(), this.loginEmailModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
