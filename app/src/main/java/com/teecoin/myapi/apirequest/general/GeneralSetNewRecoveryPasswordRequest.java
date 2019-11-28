package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.SetNewRecoveryPasswordModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralSetNewRecoveryPasswordRequest extends GeneralApiRequest {

    private SetNewRecoveryPasswordModel model;

    public GeneralSetNewRecoveryPasswordRequest(SetNewRecoveryPasswordModel model, APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?GeneralRequestTarget.GENERAL_SET_NEW_RECOVERY_PASSWORD:GeneralRequestTarget.GENERAL_SET_NEW_RECOVERY_PASSWORD_FOR_SHOP, listener);
        this.model = model;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.setNewRecoveryPassword(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), this.model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
