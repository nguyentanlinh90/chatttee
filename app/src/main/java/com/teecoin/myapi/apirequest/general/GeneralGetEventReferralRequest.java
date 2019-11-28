package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralGetEventReferralRequest extends GeneralApiRequest {


    public GeneralGetEventReferralRequest(APIResponseListener listener) {
        super(false, GeneralRequestTarget.GET_EVENT_REFERRAL, listener);
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.getReferralEventModel(requestTarget.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
