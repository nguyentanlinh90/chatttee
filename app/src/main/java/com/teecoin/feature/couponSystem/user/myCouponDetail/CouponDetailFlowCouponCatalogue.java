package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;

import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCataloguesDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

public class CouponDetailFlowCouponCatalogue extends CouponDetailFlow implements APIResponseListener {

    private String couponCatalogueId;

    public CouponDetailFlowCouponCatalogue(String CouponCatalogueId) {
        this.couponCatalogueId = CouponCatalogueId;
    }

    public void getData(Activity activity, CouponDetailFlowListener listener) {
        this.listener = listener;
        this.mainActivity = activity;
        ((TCMainActivity) activity).requestApi(new CouponUserGetCouponCataloguesDetailRequest(this.couponCatalogueId, this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUE_DETAIL) {
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
