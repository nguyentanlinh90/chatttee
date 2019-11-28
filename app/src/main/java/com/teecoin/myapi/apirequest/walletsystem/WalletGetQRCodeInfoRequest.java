package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletGetQRCodeInfoRequest extends WalletApiRequest {

    private String publicKey;

    public WalletGetQRCodeInfoRequest(String publicKey, APIResponseListener listener) {
        super(true, WalletRequestTarget.GET_QR_CODE_INFO, listener);
        this.publicKey = publicKey;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.getQRInfo(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), new QRCodeInfoRequest(this.publicKey))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }

    public class QRCodeInfoRequest extends TeeCoinModel {
        @SerializedName("public_key")
        @Expose
        private String public_key;

        public QRCodeInfoRequest(String public_key) {
            this.public_key = public_key;
        }
    }

}
