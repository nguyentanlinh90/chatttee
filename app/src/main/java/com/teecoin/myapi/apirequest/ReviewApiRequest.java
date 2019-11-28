package com.teecoin.myapi.apirequest;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public abstract class ReviewApiRequest extends APIBaseRequest {

    protected ReviewApiRequest(boolean isLoading, BaseRequestTarget requestTarget, APIResponseListener listener) {
        super(isLoading, requestTarget, listener);
    }

    public abstract void requestApi(ReviewApiManager apiManager, Context context);
}
