package com.teecoin.myapi.apirequest.walletsystem.shop;

import android.content.Context;

import com.teecoin.model.walletsystem.CoinBackPercentageModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopUpdateCoinBackRequest extends WalletApiRequest {

    private CoinBackPercentageModel coinBackPercentageModel;

    public ShopUpdateCoinBackRequest(CoinBackPercentageModel coinBackPercentageModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.SHOP_COIN_BACK_PERCENTAGE, listener);
        this.coinBackPercentageModel = coinBackPercentageModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.updateCoinBack(String.format(requestTarget.toString(), getAccountUUID()), this.coinBackPercentageModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
