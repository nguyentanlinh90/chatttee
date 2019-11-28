package com.teecoin.myapi.apirequest.walletsystem;

import android.content.Context;

import com.teecoin.model.walletsystem.ConvertToTecPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConvertToTecRequest extends WalletApiRequest {


    private ConvertToTecPostModel convertToTecPostModel;

    public ConvertToTecRequest(ConvertToTecPostModel convertToTecPostModel, APIResponseListener listener) {
        super(true, WalletRequestTarget.CONVERT_TO_TEC, listener);
        this.convertToTecPostModel = convertToTecPostModel;
    }

    @Override
    public void requestApi(WalletApiManager walletApiManager, Context context) {
        walletApiManager.convertToTec(requestTarget.toString(), this.convertToTecPostModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
