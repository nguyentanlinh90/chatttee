package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;

import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

import java.io.Serializable;

public abstract class CouponDetailFlow implements Serializable, APIResponseListener {

    protected CouponDetailFlowListener listener;
    protected Activity mainActivity;

    public abstract void getData(Activity activity, CouponDetailFlowListener listener);

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (this.mainActivity != null) {
            ((TCMainActivity) mainActivity).showBaseMessage(errorModel.getErrorMessage());
        }
    }
}
