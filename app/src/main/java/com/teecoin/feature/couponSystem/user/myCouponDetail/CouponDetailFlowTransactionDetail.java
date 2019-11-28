package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;

import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponDetailFromTransactionRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.TCUtils;

public class CouponDetailFlowTransactionDetail extends CouponDetailFlow implements APIResponseListener {

    private String transactionURL;
    private String transactionSerial;
    private boolean redeemExpired;


    public CouponDetailFlowTransactionDetail(String transactionURL, String transactionSerial) {
        this.transactionURL = transactionURL;
        this.transactionSerial = transactionSerial;
    }

    @Override
    public void getData(Activity activity, CouponDetailFlowListener listener) {
        this.listener = listener;
        this.mainActivity = activity;
        ((TCMainActivity) activity).requestApi(new
                CouponUserGetCouponDetailFromTransactionRequest(
                TCUtils.getIDFromURL(this.transactionURL), this.transactionSerial, this));

    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (response.getResult() instanceof CouponDetailModel) {
            CouponDetailModel couponDetailModel = ((CouponDetailModel) response.getResult());
            if (listener != null) {
                listener.finishProcess(couponDetailModel);
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        super.onFail(errorModel, statusCode, requestTarget);
    }
}
