package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.couponSystem.user.validationCode.UserCouponValidationCodeScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.model.reviewsystem.CuisineModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCheckInListRequest;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserRedeemCouponRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.RippleBackground;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class UserCouponResultScreen extends TCCouponBaseFragment {

    private static final String COUPON_REDEEM_DATA = "COUPON_REDEEM_DATA";

    @BindView(R.id.view_header_vendor_info)
    View vendor_info;
    @BindView(R.id.frag_vendor_ll_write_review)
    View ll_write_review;
    @BindView(R.id.frag_vendor_detail_iv_image)
    ImageView iv_image_vendor;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.frag_vendor_rating_bar)
    MaterialRatingBar rating_bar;
    @BindView(R.id.frag_vendor_tv_cuisine_type)
    TextView tv_cuisine_type;
    @BindView(R.id.frag_vendor_iv_logo)
    ImageView iv_logo;
    //---
    @BindView(R.id.view_coupon_result_processing_ll_view_processing)
    View view_processing;
    @BindView(R.id.view_coupon_user_result_success_ll_view_success)
    View view_success;
    @BindView(R.id.view_coupon_user_result_success_ll_view_unsuccessful)
    View view_un_success;
    @BindView(R.id.rip_coinback_success)
    RippleBackground rip_success;

    @BindView(R.id.view_coupon_user_result_success_tv_success)
    TextView tv_success;
    @BindView(R.id.view_coupon_user_result_unsuccessful_tv_error)
    TextView tv_error;
    @BindView(R.id.view_coupon_user_result_success_tv_success_thank)
    TextView tv_success_thank;
    @BindView(R.id.view_coupon_user_result_unsuccessful_tv_fail_thank)
    TextView tv_fail_thank;
    @BindView(R.id.view_coupon_user_result_success_tv_you_got)
    TextView tv_you_got;
    @BindView(R.id.view_coupon_user_result_success_tv_serial)
    TextView tv_serial;

    //---- bottom
    @BindView(R.id.frag_coupon_user_result_view_more)
    View view_more;
    @BindView(R.id.frag_coupon_user_result_view_check_in)
    View view_check_in;
    @BindView(R.id.frag_coupon_user_result_view_coupon_running)
    View view_running;
    @BindView(R.id.frag_coupon_user_result_view_coupon_purchase)
    View view_purchase;
    @BindView(R.id.frag_coupon_user_result_view_menu)
    View view_menu;
    @BindView(R.id.item_coupon_user_catalogue_rcv_running)
    TCRecyclerView rcv_running;
    @BindView(R.id.item_coupon_user_catalogue_rcv_purchasing)
    TCRecyclerView rcv_purchasing;
    @BindView(R.id.item_coupon_user_catalogue_rcv_menu)
    TCRecyclerView rcv_menu;
    @BindView(R.id.frag_coupon_user_result_tv_go_back)
    TextView tv_go_back;

    private UserCouponRedeemDataModel couponRedeemData;

    private UserCouponCatalogueResultAdapter adapter_purchasing;

    //redeem in UserMyCouponDetailScreen (2 case - from My Coupon and Notification)
    public static UserCouponResultScreen getInstance(CouponDetailModel couponDetailModel, VendorCodeRedeemModel vendorCodeRedeemModel) {
        UserCouponResultScreen screen = new UserCouponResultScreen();
        Bundle bundle = new Bundle();
        //for use coupon in My Coupon and from Notification Screen
        UserCouponRedeemDataModel couponRedeemData =
                new UserCouponRedeemDataModel(couponDetailModel, vendorCodeRedeemModel);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    //from validate code
    public static UserCouponResultScreen getInstance(UserCouponRedeemDataModel couponRedeemData) {
        UserCouponResultScreen screen = new UserCouponResultScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    //redeem in UserGetCouponResultScreen - User use coupon after purchase
    public static UserCouponResultScreen getInstance(CouponCataloguePurchaseResponseModel purchaseResponseModel, VendorCodeRedeemModel vendorCodeRedeemModel) {
        UserCouponResultScreen screen = new UserCouponResultScreen();
        Bundle bundle = new Bundle();
        //for use coupon after Purchase Coupon
        UserCouponRedeemDataModel couponRedeemData =
                new UserCouponRedeemDataModel(purchaseResponseModel, vendorCodeRedeemModel);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    //Check-in (2 case - from WalletUserScreen and after Redeem)
    /*public static UserCouponResultScreen getInstance(VendorCodeCheckInModel vendorCodeCheckInModel, boolean fromWallet) {
        UserCouponResultScreen screen = new UserCouponResultScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel couponResultModel = new UserCouponRedeemDataModel(fromWallet, vendorCodeCheckInModel);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponResultModel);
        screen.setArguments(bundle);
        return screen;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_user_coupon_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            couponRedeemData = (UserCouponRedeemDataModel) bundle.getSerializable(COUPON_REDEEM_DATA);
            if (couponRedeemData != null && couponRedeemData.getVendorCodeCheckInModel() != null) {
                startCheckIn(couponRedeemData.getVendorCodeCheckInModel());
            }
            if (couponRedeemData != null && couponRedeemData.getVendorCodeRedeemModel() != null) {
                startRedeem(couponRedeemData.getVendorCodeRedeemModel());
            }
        }
        updateStatus(EnumMgr.CouponScanQRCodeResult.processing);
        onClick();

        vendor_info.setVisibility(View.GONE);
        rating_bar.setIsIndicator(true);
        if (couponRedeemData != null) {
            fillDataInBanner(couponRedeemData);
        }
        toolbar.setNavigationIcon(null);
        iv_logo.setVisibility(View.GONE);//hide this logo don't show on Redeem result
    }

    private void startRedeem(VendorCodeRedeemModel vendorCodeRedeemModel) {
        toolbar.setNavigationIcon(null);
        requestApi(new CouponUserRedeemCouponRequest(vendorCodeRedeemModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {
                    RedeemCodeCouponResultModel redeemCodeCouponResultModel = (RedeemCodeCouponResultModel) response.getResult();
                    if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.SUCCESS.getValue()) {
                        showViewCheckIn(redeemCodeCouponResultModel.getHave_checkin());
                        setupRecycler(redeemCodeCouponResultModel);
                        updateStatus(EnumMgr.CouponScanQRCodeResult.Successful);
                    } else if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.TRY_AGAIN.getValue()) {
                        if (couponRedeemData != null) {
                            replaceFragment(UserCouponValidationCodeScreen.newInstance(couponRedeemData, true), false);
                        }
                    } else if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.VALIDITY_TIME.getValue()) {
                        updateUIFailValidateCode(String.format(TCUtils.getString(R.string.redeem_validity_time), redeemCodeCouponResultModel.getVendor().getName()));
                    } else {
                        updateUIFailValidateCode("");
                    }
                } else {
                    updateUIFailValidateCode("");
                }
                //update UI when redeem after purchase coupon
                if (couponRedeemData != null) {
                    fillDataInBanner(couponRedeemData);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                updateUIFailValidateCode(errorModel.getErrorMessage());
            }
        }));
    }

    private void updateUIFailValidateCode(String errorMessage) {
        updateStatus(EnumMgr.CouponScanQRCodeResult.Unsuccessful);
        if (!TCUtils.isEmpty(errorMessage))
            tv_error.setText(errorMessage);
    }

    private void setupRecycler(RedeemCodeCouponResultModel redeemCodeCouponResultModel) {
        if (!(redeemCodeCouponResultModel.getNotificationCoupons().size() > 0) && !(redeemCodeCouponResultModel.getCatalogueCoupons().size() > 0)
                && view_check_in.getVisibility() == View.GONE) {
            view_more.setVisibility(View.GONE);
        } else {
            view_more.setVisibility(View.VISIBLE);
        }

        if (redeemCodeCouponResultModel.getNotificationCoupons().size() > 0) {
            view_running.setVisibility(View.VISIBLE);
            rcv_running.setLayoutManager(new LinearLayoutManager(
                    getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
            UserCouponNotificationResultAdapter adapter_running = new UserCouponNotificationResultAdapter(LayoutInflater.from(getActiveActivity()), redeemCodeCouponResultModel.getNotificationCoupons(), null);
            rcv_running.setAdapter(adapter_running);
        } else {
            view_running.setVisibility(View.GONE);
        }

        if (redeemCodeCouponResultModel.getCatalogueCoupons().size() > 0) {
            view_purchase.setVisibility(View.VISIBLE);
            rcv_purchasing.setLayoutManager(new LinearLayoutManager(
                    getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
            adapter_purchasing = new UserCouponCatalogueResultAdapter(LayoutInflater.from(getActiveActivity()), redeemCodeCouponResultModel.getCatalogueCoupons(), (view, item, position, clickType) -> {
                UserCouponCatalogueDataModel userCouponCatalogueDataModel = new UserCouponCatalogueDataModel();
                userCouponCatalogueDataModel.setId(item.getId());
                //userCouponCatalogueDataModel.setVendor(item.getVendor());
                addFragment(UserMyCouponDetailScreen.newInstance(userCouponCatalogueDataModel));
            });
            rcv_purchasing.setAdapter(adapter_purchasing);
        } else {
            view_purchase.setVisibility(View.GONE);
        }
    }

    private void onClick() {
        registerSingleClick(toolbar, tv_go_back, view_check_in, ll_write_review);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_coupon_user_my_coupon_detail_toolbar:
//                ((TCMainActivity) getActiveActivity()).openCouponShopScreen();
                break;
            case R.id.frag_coupon_user_result_tv_go_back:
                if (couponRedeemData != null && couponRedeemData.isFromWallet()) {
                    ((TCMainActivity) getActiveActivity()).openWalletScreen();
                } else {
                    ((TCMainActivity) getActiveActivity()).openCouponUserScreen();
                }
                break;
            case R.id.frag_coupon_user_result_view_check_in:
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue());
                break;
            case R.id.frag_vendor_ll_write_review:
                if (couponRedeemData != null) {
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue(),
                            UserReviewShopScreen.getInstance(new VendorDetailModel(couponRedeemData)));
                }
                break;

        }
    }

    private void updateStatus(EnumMgr.CouponScanQRCodeResult status) {
        if (status.equals(EnumMgr.CouponScanQRCodeResult.processing)) {
            view_more.setVisibility(View.GONE);
            view_processing.setVisibility(View.VISIBLE);
            view_success.setVisibility(View.GONE);
            view_un_success.setVisibility(View.GONE);

        } else if (status.equals(EnumMgr.CouponScanQRCodeResult.Successful)) {
            view_more.setVisibility(View.VISIBLE);
            vendor_info.setVisibility(View.VISIBLE);
            hideHeader();
            view_processing.setVisibility(View.GONE);
            view_success.setVisibility(View.VISIBLE);
            view_un_success.setVisibility(View.GONE);
            collapsing_toolbar.setVisibility(View.VISIBLE);
            rip_success.startRippleAnimation();

            String formatTextThankYou;
            //set text for redeem success
            if (couponRedeemData != null && couponRedeemData.getVendorCodeCheckInModel() != null) {
                tv_success.setText(TCUtils.getString(R.string.coupon_successful));
                tv_you_got.setVisibility(View.GONE);
                tv_serial.setVisibility(View.GONE);
                formatTextThankYou = TCUtils.getString(R.string.coupon_successful_thank_you_visiting_checkin);
            } else {
                tv_success.setText(TCUtils.getString(R.string.coupon_successful));
                tv_you_got.setVisibility(View.VISIBLE);
                tv_you_got.setText(TCUtils.getString(R.string.redemption_code));
                tv_serial.setVisibility(View.VISIBLE);
                formatTextThankYou = TCUtils.getString(R.string.coupon_successful_thank_you_visiting);
            }

            if (couponRedeemData != null) {
                tv_success_thank.setText(String.format(formatTextThankYou, couponRedeemData.getVendor().getName()));
                tv_serial.setText(couponRedeemData.getSerial());
            }
        } else if (status.equals(EnumMgr.CouponScanQRCodeResult.Unsuccessful)) {
            view_more.setVisibility(View.GONE);
            vendor_info.setVisibility(View.VISIBLE);
            hideHeader();
            view_processing.setVisibility(View.GONE);
            view_success.setVisibility(View.GONE);
            view_un_success.setVisibility(View.VISIBLE);
            collapsing_toolbar.setVisibility(View.VISIBLE);
            tv_fail_thank.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rip_success != null) {
            if (rip_success.isRippleAnimationRunning()) {
                rip_success.stopRippleAnimation();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if (result != null && result.getContents() != null) {
            if (!TCUtils.isEmpty(result.getContents())) {
                VendorCodeCheckInModel vendorCodeCheckInModel = TCUtils.convertToModel(result.getContents(), VendorCodeCheckInModel.class);
                startCheckIn(vendorCodeCheckInModel);
            }
        }
    }

    private void startCheckIn(VendorCodeCheckInModel vendorCodeCheckInModel) {
        toolbar.setNavigationIcon(null);
        updateStatus(EnumMgr.CouponScanQRCodeResult.processing);
        view_success.setVisibility(View.GONE);
        view_check_in.setVisibility(View.GONE);
        view_running.setVisibility(View.GONE);
        view_purchase.setVisibility(View.GONE);
        view_menu.setVisibility(View.GONE);

        requestApi(new CouponUserGetCouponCheckInListRequest(vendorCodeCheckInModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {
                    RedeemCodeCouponResultModel redeemCodeCouponResultModel = (RedeemCodeCouponResultModel) response.getResult();
                    if (couponRedeemData != null)
                        couponRedeemData.updateAfterCheckIn(redeemCodeCouponResultModel);

                    if (redeemCodeCouponResultModel.getError_msg_code() != RedeemCodeCouponResultModel.Check_In_Error.SUCCESS.getValue()) {
                        tv_error.setText(redeemCodeCouponResultModel.getMessageErrorCheckIn());
                        updateStatus(EnumMgr.CouponScanQRCodeResult.Unsuccessful);
                    } else {
                        updateStatus(EnumMgr.CouponScanQRCodeResult.Successful);
                    }

                    showViewCheckIn(redeemCodeCouponResultModel.getCheck_in_status());
                    setupRecycler(redeemCodeCouponResultModel);

                    //for home check in
                    if (vendorCodeCheckInModel.isFromHomeCheckIn()) {

                        fillDataInBanner(new UserCouponRedeemDataModel(redeemCodeCouponResultModel));
                    }
                } else {
                    updateUIFailValidateCode("");
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                updateUIFailValidateCode(errorModel.getErrorMessage());
                if (vendorCodeCheckInModel.isFromHomeCheckIn()) {
                    vendor_info.setVisibility(View.GONE);
                }
            }
        }));
    }

    private void showViewCheckIn(boolean isShow) {
        view_check_in.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void fillDataInBanner(UserCouponRedeemDataModel couponRedeemData) {
        if (couponRedeemData.getVendor() != null) {
            Glide.with(getActiveActivity()).load(couponRedeemData.getVendor().getFeaturedImage())
                    .apply(new RequestOptions().placeholder(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).error(TCUtils.getDrawable(R.drawable.ic_cover_chattee)))
                    .into(iv_image_vendor);
            Glide.with(getActiveActivity()).load(couponRedeemData.getVendor().getFeaturedImage())
                    .apply(new RequestOptions().placeholder(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).error(TCUtils.getDrawable(R.drawable.ic_cover_chattee)))
                    .into(iv_logo);
            collapsing_toolbar.setTitle(couponRedeemData.getVendor().getName());
            rating_bar.setRating(couponRedeemData.getVendor().getRating());
        }
        tv_cuisine_type.setVisibility(View.VISIBLE);
        if (couponRedeemData.getCouponCuisines() != null && couponRedeemData.getCouponCuisines().size() > 0) {
            StringBuilder hash = new StringBuilder();
            for (CuisineModel tag : couponRedeemData.getCouponCuisines()) {
                hash.append(String.format(" %s,", tag.getName()));
            }
            tv_cuisine_type.setText(hash.substring(0, hash.length() - 1));
        } else {
            tv_cuisine_type.setVisibility(View.GONE);
        }
    }

    public void reloadPriceCoupon() {
        if (null != adapter_purchasing)
            adapter_purchasing.notifyDataSetChanged();
    }

}
