package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;

import com.teecoin.model.couponsystem.CouponDetailModel;

public class CouponDetailFlowCouponDetail extends CouponDetailFlow {

    private CouponDetailModel couponDetailModel;

    public CouponDetailFlowCouponDetail(CouponDetailModel couponDetailModel) {
        this.couponDetailModel = couponDetailModel;
    }

    @Override
    public void getData(Activity activity, CouponDetailFlowListener listener) {
        this.listener = listener;
        this.mainActivity = activity;
        if (listener != null)
            listener.finishProcess(couponDetailModel);
    }
}
