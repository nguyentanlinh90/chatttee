package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralGetVerificationCodeRequest extends GeneralApiRequest {

    public GeneralGetVerificationCodeRequest(APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?GeneralRequestTarget.GENERAL_GET_VERIFICATION_CODE:GeneralRequestTarget.GENERAL_GET_VERIFICATION_CODE_FOR_SHOP, listener);
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.getVerificationCode(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
