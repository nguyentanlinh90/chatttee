package com.teecoin.feature.general.universallink;

import android.net.Uri;

import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponDetailFromUniversalLinkRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

public class TCUniversalLinkProcess implements APIResponseListener {


    private static final String REFERRAL_CODE = "referral_code";
    private static final String TYPE = "type";
    private TCMainActivity mainActivity;
    private String type;

    public TCUniversalLinkProcess(TCMainActivity mainActivity, Uri requestApiUri) {
        this.mainActivity = mainActivity;
        this.type = requestApiUri.getQueryParameter(TYPE);
        if (type != null)
            process(type, requestApiUri);
    }

    private void process(String type, Uri requestApiUri) {
        if (mainActivity.isAlreadyLogin()) {
            if (type.equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
                mainActivity.requestApi(new CouponUserGetCouponDetailFromUniversalLinkRequest(requestApiUri.toString(), this));
            } else if (type.equals(EnumMgr.PushNotification.VendorDetail.getValue())) {
                String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(requestApiUri);
                if (!TCUtils.isEmpty(vendorId)) {
                    mainActivity.openHomeScreen(vendorId, false);
                }
            } else if (type.equals(EnumMgr.PushNotification.VendorCouponList.getValue())) {
                String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(requestApiUri);
                if (!TCUtils.isEmpty(vendorId)) {
                    mainActivity.openHomeScreen(vendorId, true);
                }
            } else if (type.equals(EnumMgr.PushNotification.CategoryCatalogue.getValue())) {
                String couponId = TCUtils.getCouponIdFromCouponDynamicLink(requestApiUri);
                if (!TCUtils.isEmpty(couponId)) {
                    mainActivity.openCouponUserScreen(couponId);
                }
            }
        } else if (type.equals(EnumMgr.PushNotification.UserRegister.getValue())) {
            //TODO: remove CreateWalletScreen, using SignUpAccountEmailScreen,should we use this Screen?
//            String referralCode = requestApiUri.getQueryParameter(REFERRAL_CODE);
//            mainActivity.addFragment(CreateWalletScreen.getInstance(referralCode));
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUE_DETAIL) {
            mainActivity.addFragment(UserMyCouponDetailScreen.newInstance((CouponDetailModel) response.getResult()));
        }
//        else if (requestTarget == RequestTarget.GET_VENDOR_DETAIL) {
//            if (type.equals(EnumMgr.PushNotification.VendorDetail.getValue())) {
//                mainActivity.addFragment(VendorScreen.getInstance((VendorDetailModel) response.getResult(), false));
//            } else if (type.equals(EnumMgr.PushNotification.VendorCouponList.getValue())) {
//                mainActivity.addFragment(VendorScreen.getInstance((VendorDetailModel) response.getResult(), true));//TODO: show coupon tab
//            }
//        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUE_DETAIL) {
            TCLog.d("hung COUPON_USER_GET_COUPON_CATALOGUE_DETAIL fail");
        }
    }
}
