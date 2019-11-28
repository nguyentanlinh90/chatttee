package com.teecoin.myapi.apirequest.general;

import android.content.Context;

import com.teecoin.model.general.ChangeRecoveryPasswordModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.utils.TCUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralChangeRecoveryPasswordRequest extends GeneralApiRequest {

    private ChangeRecoveryPasswordModel changeRecoveryPasswordModel;

    public GeneralChangeRecoveryPasswordRequest(ChangeRecoveryPasswordModel changeRecoveryPasswordModel, APIResponseListener listener) {
        super(true, TCUtils.isUserApp()?GeneralRequestTarget.CHANGE_RECOVERY_PASSWORD:GeneralRequestTarget.CHANGE_RECOVERY_PASSWORD_FOR_SHOP, listener);
        this.changeRecoveryPasswordModel = changeRecoveryPasswordModel;
    }

    @Override
    public void requestApi(GeneralApiManager generalApiManager, Context context) {
        generalApiManager.changeRecoveryPassword(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken(), changeRecoveryPasswordModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
