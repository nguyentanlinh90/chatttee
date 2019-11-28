package com.teecoin.myapi.apirequest.walletsystem.shop;

import android.content.Context;

import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopPaymentInvoiceRequest extends WalletApiRequest {
    private PaymentInvoiceSubmitModel model;

    public ShopPaymentInvoiceRequest(PaymentInvoiceSubmitModel model, APIResponseListener listener) {
        super(true, WalletRequestTarget.SHOP_PAYMENT_INVOICE, listener);
        this.model = model;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getPaymentInvoice(requestTarget.toString(), getUserToken(), this.model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
