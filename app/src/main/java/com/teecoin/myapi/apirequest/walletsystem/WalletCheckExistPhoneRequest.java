package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletCheckExistPhoneRequest extends WalletApiRequest {

    private String phone;
    private String countryCode;

    public WalletCheckExistPhoneRequest(String phone, String countryCode, APIResponseListener listener) {
        super(false, WalletRequestTarget.CHECK_EXIST_PHONE, listener);
        this.phone = phone;
        this.countryCode = countryCode;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        try {
            walletApiManager.checkExistPhone(
                    String.format(requestTarget.toString(), this.phone, URLEncoder.encode(this.countryCode, "UTF-8")))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
