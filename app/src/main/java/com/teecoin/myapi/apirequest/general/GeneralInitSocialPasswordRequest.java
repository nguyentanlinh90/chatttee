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

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralInitSocialPasswordRequest extends GeneralApiRequest {

    private InitSocialPasswordModel initSocialPasswordModel;

    public GeneralInitSocialPasswordRequest(String password, String confirmPassword, APIResponseListener listener) {
        super(true, GeneralRequestTarget.GENERAL_INIT_SOCIAL_PASSWORD, listener);
        this.initSocialPasswordModel = new InitSocialPasswordModel(password, confirmPassword);
    }


    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.initSocialPassword(
                String.format(requestTarget.toString(), getAccountUUID()), initSocialPasswordModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }


    public class InitSocialPasswordModel extends TeeCoinModel {
        @SerializedName("password")
        @Expose
        private String password;

        @SerializedName("confirm_password")
        @Expose
        private String confirm_password;

        private InitSocialPasswordModel(String password, String confirm_password) {
            this.password = password;
            this.confirm_password = confirm_password;
        }
    }
}
