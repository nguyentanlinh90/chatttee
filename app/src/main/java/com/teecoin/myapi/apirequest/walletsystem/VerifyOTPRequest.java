package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.VerifyOTPModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VerifyOTPRequest extends WalletApiRequest {


    private VerifyOTPModel otpModel;

    public VerifyOTPRequest(String country_code, String phoneNumber, String verificationCode, APIResponseListener listener) {
        super(false, WalletRequestTarget.WALLET_VERIFY_OTP, listener);
        this.otpModel = new VerifyOTPModel(country_code, phoneNumber, verificationCode);
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.verifyOTP(requestTarget.toString(), this.otpModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
