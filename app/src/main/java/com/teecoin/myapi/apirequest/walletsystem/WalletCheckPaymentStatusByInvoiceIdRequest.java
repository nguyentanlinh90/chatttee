package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletCheckPaymentStatusByInvoiceIdRequest extends WalletApiRequest {


    private String invoiceId;

    public WalletCheckPaymentStatusByInvoiceIdRequest(String invoiceId, APIResponseListener listener) {
        super(true, WalletRequestTarget.CHECK_PAYMENT_STATUS_BY_INVOICE_ID, listener);
        this.invoiceId = invoiceId;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.checkPaymentStatusByInvoiceId(
                String.format(requestTarget.toString(), getAccountUUID(), this.invoiceId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
