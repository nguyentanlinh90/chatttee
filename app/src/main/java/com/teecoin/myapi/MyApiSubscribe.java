package com.teecoin.myapi;

import android.content.Context;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseAlertDialog;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.retrofit.StatusCode;
import com.teecoin.utils.TCUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

import static core.base.BaseApplication.getActiveActivity;
import static core.base.BaseApplication.getContext;


public class MyApiSubscribe<Model> extends Subscriber<Model> {

    private APIResponseListener apiResponseListener;
    private BaseRequestTarget requestTarget;
    private Model model;
    private Context context;

//    public MyApiSubscribe(APIResponseListener apiResponseListener, RequestTarget requestTarget) {
//        this.apiResponseListener = apiResponseListener;
//        this.requestTarget = requestTarget;
//    }

    public MyApiSubscribe(Context context, APIResponseListener apiResponseListener, BaseRequestTarget requestTarget) {
        this.context = context;
        this.apiResponseListener = apiResponseListener;
        this.requestTarget = requestTarget;
    }


    @Override
    public void onCompleted() {
        if (context != null && context instanceof TCMainActivity) {
            ((TCMainActivity) context).hideLoadingDialog();
        }
        if (!isUnsubscribed()) {
            if (apiResponseListener != null && this.model instanceof BaseResponseModel) {
                apiResponseListener.onSuccess((BaseResponseModel) this.model, requestTarget);
            }
        }

    }

    @Override
    public void onError(Throwable e) {
        if (context != null && context instanceof TCMainActivity) {
            ((TCMainActivity) context).hideLoadingDialog();
        }
        if (e instanceof SocketTimeoutException) {
            ErrorModel errorModel = new ErrorModel(StatusCode.TIMEOUT_CONNECTION.getValue(),
                    TCUtils.getString(R.string.error_request_time_out));
            if (!isUnsubscribed()) {
                if (apiResponseListener != null) {
                    apiResponseListener.onFail(errorModel, StatusCode.TIMEOUT_CONNECTION.getValue(), requestTarget);
                }
            }
        } else if ((e instanceof ConnectException || e instanceof UnknownHostException)
                && this.context != null && context instanceof TCMainActivity) {

            ((TCMainActivity) context).showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
        } else if (e instanceof retrofit2.adapter.rxjava.HttpException) {
            HttpException httpException = (HttpException) e;
            try {
                ErrorModel errorModel = new ErrorModel(httpException.response().errorBody().string());

                //force login
                if (TCUtils.isForceLogin(errorModel)) {
                    ((TCMainActivity) getActiveActivity()).showForceLoginDialog();
                    return;
                }

                if (!isUnsubscribed()) {
                    if (apiResponseListener != null) {
                        apiResponseListener.onFail(errorModel, httpException.code(), requestTarget);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            if (apiResponseListener != null) {
                ErrorModel errorModel = new ErrorModel(-1, e.getMessage());
                apiResponseListener.onFail(errorModel, -1, requestTarget);
            }
        }
    }

    @Override
    public void onNext(Model model) {
        if (context != null && context instanceof TCMainActivity) {
            ((TCMainActivity) context).hideLoadingDialog();
        }
        this.model = null;
        this.model = model;

    }
}
