package com.teecoin.feature.general.notification;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.checkinNotification.CheckInNotificationScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.payment.shopPaymentDetail.ShopPaymentDetailScreen;
import com.teecoin.feature.payment.userPaymentAndCoinbackDetail.UserPaymentAndCoinBackDetailScreen;
import com.teecoin.feature.reviewSystem.detailReview.UserReviewDetailScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.walletSystem.convertToTecDetails.ConvertToTecDetailScreen;
import com.teecoin.feature.walletSystem.historyPaymentDetail.TransferMoneyDetailScreen;
import com.teecoin.feature.walletSystem.withdrawDetails.WithdrawDetailScreen;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

public class TCNotificationProcess {

    private Context context;

    public TCNotificationProcess(Context context, PushNotificationModel pushNotificationModel) {
        this.context = context;
        gotoSpecifiedScreen(pushNotificationModel);
    }

    private void gotoSpecifiedScreen(PushNotificationModel pushNotificationModel) {
        if (pushNotificationModel.getData() != null) {
            if (pushNotificationModel.getData().getType() != null) {
                if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Reward.getValue())
                        || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.TransferMoney.getValue())) {
                    ((TCMainActivity) context).addFragment(TransferMoneyDetailScreen.getInstance(pushNotificationModel));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Payment.getValue())
                        || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CoinBack.getValue())) {

                    ((TCMainActivity) context).addFragment(((TCMainActivity) context).isAppUser() ? UserPaymentAndCoinBackDetailScreen.getInstance(pushNotificationModel) : ShopPaymentDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id()));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Tip.getValue())) {
                    ((TCMainActivity) context).addFragment(UserReviewDetailScreen.getInstance(pushNotificationModel));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue())
                        || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Event.getValue())) {
                    ((TCMainActivity) context).addFragment(WebViewScreen.getInstance(pushNotificationModel.getData().getUrl(), pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue()) ? TCUtils.getString(R.string.review_promotion_detail_title) : TCUtils.getString(R.string.events)));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Referral.getValue())) {
                    ((TCMainActivity) context).gotoNotificationScreen();
//                    PopupNotification notification = new PopupNotification(context, pushNotificationModel, new TCConfirmListener() {
//                        @Override
//                        public void onConfirmed(int id, Object onWhat) {
//                            PushNotificationModel pushNotificationModel = (PushNotificationModel) onWhat;
//                            ((TCMainActivity) context).addFragment(WebViewScreen.getInstance(pushNotificationModel.getData().getUrl(), EnumMgr.PushNotification.Referral.getValue()));
//                        }
//                    });
//                    notification.show();
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Coupon.getValue())) {
                    ((TCMainActivity) context).addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CheckInCoupon.getValue())) {
                    ((TCMainActivity) context).addFragment(CheckInNotificationScreen.getInstance(pushNotificationModel));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.ConvertToTEC.getValue())||pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Incoming.getValue())) {
                    ((TCMainActivity) context).addFragment(ConvertToTecDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id(),pushNotificationModel.getData().getType()));
                }else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.WithdrawRequest.getValue())||pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.WithdrawCompleted.getValue())) {
                    ((TCMainActivity) context).addFragment(WithdrawDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id(),pushNotificationModel.getData().getType()));
                }
                else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
                    ((TCMainActivity) context).addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.VendorDetail.getValue())) {
                    FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                            .addOnSuccessListener(((TCMainActivity) context), new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    if (pendingDynamicLinkData != null) {
                                        String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                        if (!TCUtils.isEmpty(vendorId)) {
                                            ((TCMainActivity) context).openHomeScreen(vendorId, false);
                                        }
//                                        ((TCMainActivity) context).requestApi(new ReviewGetVendorDetailFromUniversalLinkRequest(pendingDynamicLinkData.getLink().toString(), new APIResponseListener() {
//                                            @Override
//                                            public void onSuccess(BaseResponseModel response, RequestTarget requestTarget) {
//                                                ((TCMainActivity) context).addFragment(VendorScreen.getInstance((VendorDetailModel) response.getResult(), false));
//                                            }
//
//                                            @Override
//                                            public void onFail(ErrorModel errorModel, int statusCode, RequestTarget requestTarget) {
//
//                                            }
//                                        }));
                                    }
                                }
                            });
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.VendorCouponList.getValue())) {
                    FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                            .addOnSuccessListener(((TCMainActivity) context), new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    if (pendingDynamicLinkData != null) {
                                        String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                        if (!TCUtils.isEmpty(vendorId)) {
                                            ((TCMainActivity) context).openHomeScreen(vendorId, true);
                                        }
                                    }
                                }
                            });
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CategoryCatalogue.getValue())) {
                    FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                            .addOnSuccessListener(((TCMainActivity) context), new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    if (pendingDynamicLinkData != null) {
                                        String couponId = TCUtils.getCouponIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                        if (!TCUtils.isEmpty(couponId)) {
                                            ((TCMainActivity) context).openCouponUserScreen(couponId);
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }
}
