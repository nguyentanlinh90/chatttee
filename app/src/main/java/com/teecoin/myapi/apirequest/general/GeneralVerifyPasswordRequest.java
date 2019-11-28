package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralVerifyPasswordRequest extends GeneralApiRequest {

    private VerifyPasswordModel verifyPasswordModel;

    public GeneralVerifyPasswordRequest(String password, APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?GeneralRequestTarget.GENERAL_VERIFY_PASSWORD:GeneralRequestTarget.GENERAL_VERIFY_PASSWORD_FOR_SHOP, listener);
        this.verifyPasswordModel = new VerifyPasswordModel(password);
    }


    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.verifyPassword(
                String.format(requestTarget.toString(), getAccountUUID()), verifyPasswordModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }


    public class VerifyPasswordModel extends TeeCoinModel {
        @SerializedName("password")
        @Expose
        private String password;

        private VerifyPasswordModel(String password) {
            this.password = password;
        }
    }
}
