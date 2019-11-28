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

public class WalletUpdateAccountRequest extends WalletApiRequest {


    private AccountModel accountModel;

    public WalletUpdateAccountRequest(AccountModel accountModel, APIResponseListener listener) {

        super(true, TCUtils.isUserApp() ?
                        WalletRequestTarget.UPDATE_USER_ACCOUNT : WalletRequestTarget.UPDATE_SHOP_ACCOUNT,
                listener);
        this.accountModel = accountModel;
    }

    public WalletUpdateAccountRequest(AccountModel accountModel, boolean isLoading, APIResponseListener listener) {
        super(isLoading, TCUtils.isUserApp() ?
                        WalletRequestTarget.UPDATE_USER_ACCOUNT : WalletRequestTarget.UPDATE_SHOP_ACCOUNT,
                listener);
        this.accountModel = accountModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.updateAccount(
                String.format(requestTarget.toString(), accountModel.getUuid()), getUserToken(), accountModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
