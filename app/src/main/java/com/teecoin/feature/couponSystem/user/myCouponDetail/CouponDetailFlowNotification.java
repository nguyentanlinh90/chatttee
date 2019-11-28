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
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.EnumMgr;

public class CouponDetailFlowNotification extends CouponDetailFlow implements APIResponseListener {

    private String notificationType;
    private String notificationCouponId;
    private String notificationSerial;

    public CouponDetailFlowNotification(String notificationType, String notificationCouponId, String notificationSerial) {
        this.notificationType = notificationType;
        this.notificationCouponId = notificationCouponId;
        this.notificationSerial = notificationSerial;
    }

    @Override
    public void getData(Activity activity, CouponDetailFlowListener listener) {
        this.listener = listener;
        this.mainActivity = activity;
        if (this.notificationType.equals(
                EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
            ((TCMainActivity) activity).requestApi(new CouponUserGetCouponCataloguesDetailRequest(this.notificationCouponId, this));
        } else {
            ((TCMainActivity) activity).requestApi(new CouponUserGetNotificationDetailRequest(this.notificationCouponId, this.notificationSerial, this));
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUE_DETAIL
                || requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_NOTIFICATION_DETAIL) {
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
