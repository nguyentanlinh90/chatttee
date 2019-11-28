package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;

import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCataloguesDetailRequest;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetNotificationDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;

public class CouponDetailFlowUserCoupon extends CouponDetailFlow implements APIResponseListener {

    private String couponType;
    private String couponId;
    private String couponSerial;


    public CouponDetailFlowUserCoupon(String couponType, String couponId, String couponSerial) {
        this.couponType = couponType;
        this.couponId = couponId;
        this.couponSerial = couponSerial;
    }

    @Override
    public void getData(Activity activity, CouponDetailFlowListener listener) {
        this.listener = listener;
        this.mainActivity = activity;
        if (this.couponType.equals(EnumMgr.CouponType.Catalogues.getValue())) {
            ((TCMainActivity) activity).requestApi(new
                    CouponUserGetCouponCataloguesDetailRequest(this.couponId, this.couponSerial, this));
        } else {
            ((TCMainActivity) activity).requestApi(new CouponUserGetNotificationDetailRequest(this.couponId, "", this));
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        CouponDetailModel couponDetailModel = ((CouponDetailModel) response.getResult());
        if (listener != null) {
            listener.finishProcess(couponDetailModel);
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        super.onFail(errorModel, statusCode, requestTarget);
    }
}
