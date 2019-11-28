package com.teecoin.myapi.apirequest.walletsystem.user;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserGetPaymentInvoiceDetailRequest extends WalletApiRequest {
    private String invoice;

    public UserGetPaymentInvoiceDetailRequest(String invoice, APIResponseListener listener) {
        super(true, WalletRequestTarget.USER_GET_PAYMENT_INVOICE_DETAIL, listener);
        this.invoice = invoice;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getPaymentInvoiceDetail(String.format(requestTarget.toString(), invoice), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
