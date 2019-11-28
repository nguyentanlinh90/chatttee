package com.teecoin.feature.couponSystem.user.getCouponResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserRedeemResultScreen;
import com.teecoin.feature.couponSystem.user.validationCode.UserCouponValidationCodeScreen;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CoinBackModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponCoinBackRedemptionResponse;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponConbackRedemption;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.RippleBackground;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class UserGetCouponResultScreen extends TCCouponBaseFragment {
    private static final String PURCHASE_MODEL = "CouponCataloguePurchaseResponseModel";
    @BindView(R.id.view_coupon_result_processing_ll_view_processing)
    View view_processing;
    @BindView(R.id.view_coupon_status)
    View view_coupon_status;
    @BindView(R.id.view_header_success)
    View view_successful;
    @BindView(R.id.view_header_fail)
    View view_fail;
    @BindView(R.id.view_header_fail_tv_error_msg)
    TextView view_header_fail_tv_error_msg;
    @BindView(R.id.view_coupon_user_result_view_redeem)
    View view_redeem;


    @BindView(R.id.view_coupon_user_result_success_tv_you_got)
    TextView tv_you_got_congratulation;
    @BindView(R.id.view_coupon_user_result_success_tv_use_now)
    TextView tv_use_now;
    @BindView(R.id.view_coupon_user_result_success_tv_back_to_catalogue)
    TextView tv_success_back_to_catalogue;

    @BindView(R.id.rip_coinback_success)
    RippleBackground rip_success;
    private CouponCataloguePurchaseResponseModel purchaseResponseModel;

    @BindView(R.id.view_redemption_rcv)
    TCRecyclerView rcv_redeem;
    private ArrayList<CoinBackModel> listCoinback;
    private CoinbackSettingAdapter coinbackSettingAdapter;


    public static UserGetCouponResultScreen getInstance(CouponCataloguePurchaseResponseModel purchaseResponseModel) {
        UserGetCouponResultScreen screen = new UserGetCouponResultScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PURCHASE_MODEL, purchaseResponseModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_get_coupon_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideFooter();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.coupon_my_coupons).toUpperCase());
        hideButtonBackToolbar();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            purchaseResponseModel = (CouponCataloguePurchaseResponseModel) bundle.getSerializable(PURCHASE_MODEL);

        }
        setUpRecyclerView();
        updateStatus(EnumMgr.CouponScanQRCodeResult.processing);
        new Handler().postDelayed(() -> {
            if (purchaseResponseModel != null && purchaseResponseModel.isSuccess()) {
                updateStatus(EnumMgr.CouponScanQRCodeResult.Successful);

            } else {
                updateStatus(EnumMgr.CouponScanQRCodeResult.Unsuccessful);
                view_redeem.setVisibility(View.GONE);
            }

        }, 1000L);

        getCoinbackRedemption();

        onClick();
    }

    private void setUpRecyclerView() {
        listCoinback = new ArrayList<>();
        coinbackSettingAdapter = new CoinbackSettingAdapter(LayoutInflater.from(getActiveActivity()), listCoinback, (view, item, position, clickType) -> {

        });
        rcv_redeem.setAdapter(coinbackSettingAdapter);
    }

    private void onClick() {
        tv_success_back_to_catalogue.setOnClickListener(v -> finishWithResult(RESULT_OK, new Intent()));
        tv_use_now.setOnClickListener(v -> {
            TCAppFlyerTrackingEvent.getInstance().trackCouponRedemptionUseNow();
            gotoScanQRCode(EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue());
        });
    }

    private void updateStatus(EnumMgr.CouponScanQRCodeResult status) {
        if (status.equals(EnumMgr.CouponScanQRCodeResult.processing)) {
            view_processing.setVisibility(View.VISIBLE);
            view_coupon_status.setVisibility(View.GONE);
        } else if (status.equals(EnumMgr.CouponScanQRCodeResult.Successful)) {
            view_processing.setVisibility(View.GONE);
            view_coupon_status.setVisibility(View.VISIBLE);
            view_successful.setVisibility(View.VISIBLE);
            view_fail.setVisibility(View.GONE);
            //rip_success.startRippleAnimation();
            if (purchaseResponseModel != null) {
                String purchaseCouponName = String.format(TCUtils.getString(R.string.coupon_congratulation_you_get),
                        purchaseResponseModel.getCoupon().getName(), purchaseResponseModel.getVendor().getName());
                int indexOfCouponName = purchaseCouponName.indexOf(purchaseResponseModel.getCoupon().getName());

                SpannableString styledText =
                        TCUtils.getStyledText(purchaseCouponName, R.style.TCTextViewBlackBoldLargeEditor,
                                indexOfCouponName, indexOfCouponName + purchaseResponseModel.getCoupon().getName().length());
                tv_you_got_congratulation.setText(styledText, TextView.BufferType.SPANNABLE);
            }

        } else if (status.equals(EnumMgr.CouponScanQRCodeResult.Unsuccessful)) {
            view_processing.setVisibility(View.GONE);
            view_coupon_status.setVisibility(View.VISIBLE);
            view_successful.setVisibility(View.GONE);
            view_fail.setVisibility(View.VISIBLE);
            view_header_fail_tv_error_msg.setText(purchaseResponseModel != null ? purchaseResponseModel.getErrorMessage() : view_header_fail_tv_error_msg.getText().toString());
            tv_use_now.setVisibility(View.GONE);
        }
    }

    private void setText() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (rip_success != null) {
//            if (rip_success.isRippleAnimationRunning()) {
//                rip_success.stopRippleAnimation();
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if (result != null && result.getContents() != null) {
            if (!TCUtils.isEmpty(result.getContents())) {
                VendorCodeRedeemModel vendorCodeRedeemModel;
                //check if qr code wrong format
                try {
                    vendorCodeRedeemModel = TCUtils.convertToModel(result.getContents(), VendorCodeRedeemModel.class);
                } catch (Exception e) {
                    vendorCodeRedeemModel = null;
                }
                if (purchaseResponseModel != null && vendorCodeRedeemModel != null) {
                    vendorCodeRedeemModel = new VendorCodeRedeemModel(vendorCodeRedeemModel.getVendor_code(), purchaseResponseModel.getCoupon().getSerial());
                    addFragment(UserRedeemResultScreen.getInstance(purchaseResponseModel, vendorCodeRedeemModel));
                } else {
                    addFragment(UserCouponValidationCodeScreen.newInstance(purchaseResponseModel, false));
                }
            }
        } else if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue() && resultCode == Activity.RESULT_OK) {//no need data
            if (purchaseResponseModel != null) {
                addFragment(UserCouponValidationCodeScreen.newInstance(purchaseResponseModel, false));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCoinbackRedemption() {
        requestApi(new CouponConbackRedemption(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                CouponCoinBackRedemptionResponse couponConbackRedemption = (CouponCoinBackRedemptionResponse) response.getResult();
                if (couponConbackRedemption != null) {
                    if (couponConbackRedemption.getCoin_back_setting() != null) {
                        listCoinback.addAll(couponConbackRedemption.getCoin_back_setting());
                        if (!TCUtils.isEmpty(couponConbackRedemption.getRedemption_count())) {
                            int redeem_count = Integer.parseInt(couponConbackRedemption.getRedemption_count());
                            if (redeem_count <= listCoinback.size()) {
                                for (int i = 0; i < redeem_count; i++) {
                                    listCoinback.get(i).setSelected(true);
                                }
                            } else {
                                for (int i = 0; i < listCoinback.size(); i++) {
                                    listCoinback.get(i).setSelected(true);
                                }
                            }

                        }
                    }
                    rcv_redeem.onLoadMoreComplete();
                } else {
                    view_redeem.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));

    }
}
