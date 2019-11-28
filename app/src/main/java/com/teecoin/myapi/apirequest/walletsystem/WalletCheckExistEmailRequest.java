package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.R;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletCheckExistEmailRequest extends WalletApiRequest {


    private String email;

    public WalletCheckExistEmailRequest(String email, APIResponseListener listener) {
        super(false, WalletRequestTarget.CHECK_EXIST_EMAIL, listener);
        this.email = email;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        try {
            walletApiManager.checkExistEmail(
                    String.format(requestTarget.toString(), URLEncoder.encode(this.email, "UTF-8")))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
        } catch (UnsupportedEncodingException e) {
            this.listener.onFail(new ErrorModel(-1, TCUtils.getString(R.string.error_text_contains_unsupported_encoding)), -1, this.requestTarget);
        }
    }
}
