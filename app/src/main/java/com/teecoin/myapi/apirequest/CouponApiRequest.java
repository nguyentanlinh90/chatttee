package com.teecoin.myapi.apirequest;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public abstract class CouponApiRequest extends APIBaseRequest {


    protected CouponApiRequest(boolean isLoading, BaseRequestTarget requestTarget, APIResponseListener listener) {
        super(isLoading, requestTarget, listener);
    }

    public abstract void requestApi(CouponApiManager apiManager, Context context);
}
