package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletTransferMoneyRequest extends WalletApiRequest {

    private TransferMoneyModel transferMoneyModel;

    public WalletTransferMoneyRequest(TransferMoneyModel transferMoneyModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.TRANSFER_MONEY, listener);
        this.transferMoneyModel = transferMoneyModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.transferMoney(requestTarget.toString(),
                getUserToken(), this.transferMoneyModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
