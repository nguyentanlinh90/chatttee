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

public class GeneralCheckRewardStatusRequest extends GeneralApiRequest {


    public GeneralCheckRewardStatusRequest(APIResponseListener listener) {
        super(true, GeneralRequestTarget.GENERAL_CHECK_REWARD_STATUS, listener);
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.checkRewardStatus(
                String.format(requestTarget.toString(), TCUtils.getUniquePseudoID()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
