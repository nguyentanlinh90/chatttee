package com.teecoin.myapi.apirequest;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public abstract class WalletApiRequest extends APIBaseRequest {

    protected WalletApiRequest(boolean isLoading, BaseRequestTarget requestTarget, APIResponseListener listener) {
        super(isLoading, requestTarget, listener);
    }

    public abstract void requestApi(WalletApiManager walletApiManager, Context context);
}
