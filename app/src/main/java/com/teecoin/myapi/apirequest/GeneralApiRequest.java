package com.teecoin.myapi.apirequest;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public abstract class GeneralApiRequest extends APIBaseRequest {

    protected GeneralApiRequest(boolean isLoading, BaseRequestTarget requestTarget, APIResponseListener listener) {
        super(isLoading, requestTarget, listener);
    }

    public abstract void requestApi(GeneralApiManager generalApiManager, Context context);
}
