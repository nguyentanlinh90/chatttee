package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.realmdb.RealmController;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralStoreSecretKeyRequest extends GeneralApiRequest {

    public GeneralStoreSecretKeyRequest(APIResponseListener listener) {
        super(false, GeneralRequestTarget.GENERAL_STORE_SECRET_KEY, listener);
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.storeSecretKey(
                String.format(requestTarget.toString(), getAccountUUID()), new StoreSecretKey())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));

    }


    public class StoreSecretKey extends TeeCoinModel {

        @SerializedName("secret_key")
        @Expose
        private String secret_key;

        public StoreSecretKey() {
            AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
            this.secret_key = accountModel != null ? accountModel.getSecret_key() : "";
        }
    }
}
