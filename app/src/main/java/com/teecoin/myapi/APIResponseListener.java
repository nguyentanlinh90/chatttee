package com.teecoin.myapi;

import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;


public interface APIResponseListener<Model> {

    void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget);

    void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget);

}
