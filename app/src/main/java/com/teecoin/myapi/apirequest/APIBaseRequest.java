package com.teecoin.myapi.apirequest;

import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;

public abstract class APIBaseRequest {

    protected BaseRequestTarget requestTarget;
    protected APIResponseListener listener;
    private boolean isLoading;

    APIBaseRequest(boolean isLoading, BaseRequestTarget requestTarget, APIResponseListener listener) {
        this.isLoading = isLoading;
        this.requestTarget = requestTarget;
        this.listener = listener;
    }

    protected String getUserToken() {
        return TCConstant.TOKEN + TCSharePreferenceManager.getInstance().getString(DataKey.Token);
    }

    public boolean isLoading() {
        return isLoading;
    }

    protected String getAccountUUID() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel != null ? accountModel.getUuid() : "";
    }

    protected String getAccountPublicKey() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel != null ? accountModel.getPublic_key() : "";
    }
}
