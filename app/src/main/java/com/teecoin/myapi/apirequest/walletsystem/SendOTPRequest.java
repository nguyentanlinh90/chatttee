package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.SendOTPModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendOTPRequest extends WalletApiRequest {


    private SendOTPModel otpModel;

    public SendOTPRequest(String country_code, String phoneNumber, String deviceId, String recaptchaToken, APIResponseListener listener) {
        super(false, WalletRequestTarget.WALLET_SEND_OTP, listener);
        this.otpModel = new SendOTPModel(country_code, phoneNumber, deviceId, recaptchaToken);
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.sendPhoneOTP(requestTarget.toString(), this.otpModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
