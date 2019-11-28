package com.teecoin.feature.couponSystem.user.myCouponDetail;

import java.io.Serializable;

public class CouponDetailController implements Serializable {

    private CouponDetailFlow couponDetailFlow;
    private boolean redeemExpired;


    public CouponDetailController(CouponDetailFlow couponDetailFlow) {
        this.couponDetailFlow = couponDetailFlow;
    }

    public CouponDetailController(CouponDetailFlow couponDetailFlow, boolean redeemExpired) {
        this.couponDetailFlow = couponDetailFlow;
        this.redeemExpired = redeemExpired;
    }

    public CouponDetailFlow getCouponDetailFlow() {
        return couponDetailFlow;
    }

    public boolean isRedeemExpired() {
        return redeemExpired;
    }
}
