package com.teecoin.model.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserCreatePaymentRequest extends WalletApiRequest {

    private PaymentModel paymentModel;

    public UserCreatePaymentRequest(PaymentModel paymentModel, APIResponseListener listener) {
        super(false, WalletRequestTarget.CREATE_PAYMENT, listener);
        this.paymentModel = paymentModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.userCreatePayment(requestTarget.toString(), getUserToken(), this.paymentModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
