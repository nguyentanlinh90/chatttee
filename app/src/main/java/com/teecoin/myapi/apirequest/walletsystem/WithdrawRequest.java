package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.WithdrawPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WithdrawRequest extends WalletApiRequest {


    private WithdrawPostModel withdrawPostModel;

    public WithdrawRequest(WithdrawPostModel withdrawPostModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.WITHDRAW, listener);
        this.withdrawPostModel = withdrawPostModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.withdraw(requestTarget.toString(), this.withdrawPostModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
