package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserRedeemResultScreen;
import com.teecoin.feature.couponSystem.user.getCouponResult.UserGetCouponResultScreen;
import com.teecoin.feature.couponSystem.user.validationCode.UserCouponValidationCodeScreen;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseRequestModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.ResultGetCouponModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.UserCouponModel;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.ui.SimpleRatingBar;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.calculateFeeAmount;
import static com.teecoin.utils.TCUtils.calculateToTecIncludeFeeAmount;
import static com.teecoin.utils.TCUtils.calculateToTecNoFeeAmount;
import static com.teecoin.utils.TCUtils.convertToDouble;

public class UserMyCouponDetailScreen extends TCCouponBaseFragment implements OnMapReadyCallback {

    private static final String COUPON_DETAIL_CONTROLLER = "COUPON_DETAIL_CONTROLLER";
    @BindView(R.id.frg_coupon_user_my_coupon_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_iv_back)
    ImageView iv_back;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_iv_banner)
    ImageView iv_banner;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_iv_logo)
    ImageView iv_logo;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_name_vendor)
    TextView tv_name_vendor;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_rating)
    SimpleRatingBar rating;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_view_address)
    View view_address;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_view_tec)
    View view_tec;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_name_coupon)
    TextView tv_name_coupon;
    @BindView(R.id.view_coupon_user_detail_tv_start_date_time)
    TextView tv_date_time_start;
    @BindView(R.id.view_coupon_user_detail_tv_end_date_time)
    TextView tv_date_time_end;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_end_at)
    TextView tv_end_at;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_tec)
    TextView tv_tec;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_fee)
    TextView tv_fee;
    @BindView(R.id.view_user_coupon_detail_rl_redemption_date)
    View rl_redemption_date;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_redemption_date)
    TextView tv_redemption_date;
    @BindView(R.id.view_user_coupon_detail_ll_redemption_code)
    View ll_redemption_code;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_redemption_code)
    TextView tv_redemption_code;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_description)
    TextView tv_description;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_term)
    TextView tv_term;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_guide)
    TextView tv_guide;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_ll_hashtags)
    View ll_hash_tags;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_hashtags)
    TextView tv_hash_tags;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_scan_qr_code)
    TextView tv_scan_qr_code;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_time_remain)
    TextView tvTimeRemain;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_purchase)
    TextView tv_purchase;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_tv_get_coupon)
    TextView tv_get_coupon;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_ll_view)
    View ll_view;
    private CouponDetailModel couponDetailModel;
    private String couponIdForGoogleAnalyticTracking;//this variable for google analytic tracking
    private GoogleMap googleMap;
    private LatLng latLng;

    private long timeReload = TCConstant.TIME_COUPON_RELOAD;
    private CountDownTimer countDownTimer = new CountDownTimer(timeReload, TCConstant.ONE_SECOND_IN_MILLISECOND) {
        @Override
        public void onTick(long l) {
            timeReload = l;
            tvTimeRemain.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.coupon_price_will_change_in_s), String.valueOf(timeReload / 1000))));
        }

        @Override
        public void onFinish() {
            reLoadPrice();
        }
    };
    private DialogPurchaseCoupon dialogGetCoupon;

    public static UserMyCouponDetailScreen newInstance(CouponDetailModel couponDetailModel) {
        UserMyCouponDetailScreen screen = new UserMyCouponDetailScreen();
        Bundle bundle = new Bundle();
        CouponDetailController couponDetailController
                = new CouponDetailController(new CouponDetailFlowCouponDetail(couponDetailModel));
        screen.couponIdForGoogleAnalyticTracking = couponDetailModel.getCoupon_id();
        bundle.putSerializable(COUPON_DETAIL_CONTROLLER, couponDetailController);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserMyCouponDetailScreen newInstance(UserCouponModel couponUserModel, boolean redeemExpired) {// from coupon
        UserMyCouponDetailScreen screen = new UserMyCouponDetailScreen();
        Bundle bundle = new Bundle();
        CouponDetailController couponDetailController = new CouponDetailController(
                new CouponDetailFlowUserCoupon(couponUserModel.getType(),
                        couponUserModel.getCoupon_id(), couponUserModel.getSerial()),
                redeemExpired);
        screen.couponIdForGoogleAnalyticTracking = couponUserModel.getCoupon_id();
        bundle.putSerializable(COUPON_DETAIL_CONTROLLER, couponDetailController);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserMyCouponDetailScreen newInstance(UserCouponCatalogueDataModel userCouponCatalogueDataModel) {// from catalogues
        UserMyCouponDetailScreen screen = new UserMyCouponDetailScreen();
        Bundle bundle = new Bundle();
        CouponDetailController couponDetailController
                = new CouponDetailController(new CouponDetailFlowCouponCatalogue(
                userCouponCatalogueDataModel.getId()));
        screen.couponIdForGoogleAnalyticTracking = userCouponCatalogueDataModel.getId();
        bundle.putSerializable(COUPON_DETAIL_CONTROLLER, couponDetailController);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserMyCouponDetailScreen newInstance(PushNotificationModel pushNotificationModel) {// from notification
        UserMyCouponDetailScreen screen = new UserMyCouponDetailScreen();
        Bundle bundle = new Bundle();
        CouponDetailController couponDetailController
                = new CouponDetailController(new CouponDetailFlowNotification(
                pushNotificationModel.getData().getType(),
                pushNotificationModel.getData().getCoupon_id(),
                pushNotificationModel.getData().getSerial()));
        screen.couponIdForGoogleAnalyticTracking = pushNotificationModel.getData().getId();
        bundle.putSerializable(COUPON_DETAIL_CONTROLLER, couponDetailController);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserMyCouponDetailScreen newInstance(TransactionDetailModel transactionDetailModel) {// from transaction
        UserMyCouponDetailScreen screen = new UserMyCouponDetailScreen();
        Bundle bundle = new Bundle();
        CouponDetailController couponDetailController
                = new CouponDetailController(new CouponDetailFlowTransactionDetail(
                transactionDetailModel.getUrl(), transactionDetailModel.getSerial()));
        screen.couponIdForGoogleAnalyticTracking = TCUtils.getIDFromURL(transactionDetailModel.getUrl());
        bundle.putSerializable(COUPON_DETAIL_CONTROLLER, couponDetailController);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_user_my_coupon_detail, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideButtonBackToolbar();
        showTabMenuBottom();
        hideHeader();
        showFooter();
    }

    @Override
    public void onBindView() {
        tv_scan_qr_code.setVisibility(View.GONE);
        tvTimeRemain.setVisibility(View.GONE);
        tv_purchase.setVisibility(View.GONE);
        tv_get_coupon.setVisibility(View.GONE);
        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        if (feeConfigModel != null) {
            tv_fee.setText(String.format(TCUtils.getString(R.string.coupon_fee_purchase),
                    ((TCMainActivity) getActiveActivity()).getBasic_fee_amount()));
        }
        initClickEvent();

        if (getArguments() != null) {
            CouponDetailController couponDetailController = (CouponDetailController)
                    getArguments().getSerializable(COUPON_DETAIL_CONTROLLER);
            if (couponDetailController != null)
                getData(couponDetailController);
        }
    }

    private void getData(CouponDetailController couponDetailController) {
        couponDetailController.getCouponDetailFlow().getData(getActiveActivity(), model -> {
            couponDetailModel = model;
            fillData(couponDetailController, couponDetailModel);
            updateUI(couponDetailController, couponDetailModel);
        });
    }

    private void initClickEvent() {
        registerSingleClick(R.id.frg_coupon_user_my_coupon_detail_tv_scan_qr_code, R.id.frg_coupon_user_my_coupon_detail_iv_back,
                R.id.frg_coupon_user_my_coupon_detail_tv_purchase, R.id.frg_coupon_user_my_coupon_detail_tv_get_coupon,
                R.id.frg_coupon_user_my_coupon_detail_ll_view);
        toolbar.setNavigationOnClickListener(v -> getActiveActivity().onBackPressed());
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_coupon_user_my_coupon_detail_tv_scan_qr_code:
                TCAppFlyerTrackingEvent.getInstance().trackCouponRedemptionScanQRCode();
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue());
                break;
            case R.id.frg_coupon_user_my_coupon_detail_iv_back:
                getActiveActivity().onBackPressed();
                break;
            case R.id.frg_coupon_user_my_coupon_detail_tv_purchase:
                long timeCurr = System.currentTimeMillis();
                dialogGetCoupon = new DialogPurchaseCoupon(getActiveActivity(), couponDetailModel, this::handlePurchase, timeCurr, timeReload, () -> {
                    reLoadPrice();
                    dialogGetCoupon.reLoadPrice(couponDetailModel);
                });
                dialogGetCoupon.show(getChildFragmentManager(), "DialogPurchaseCoupon");
                break;
            case R.id.frg_coupon_user_my_coupon_detail_tv_get_coupon:
                userGetCoupon(couponDetailModel);
                break;
            case R.id.frg_coupon_user_my_coupon_detail_ll_view:
                if (couponDetailModel != null && couponDetailModel.getVendor() != null) {
                    addFragment(VendorScreen.getInstance(couponDetailModel.getVendor().getId()));
                }
                break;
        }
    }

    private void setPrice() {
        if (null != couponDetailModel) {
            couponDetailModel.setCalculateToTecIncludeFeeAmount(convertToDouble(calculateToTecIncludeFeeAmount(couponDetailModel.getUsd_price())));
            couponDetailModel.setCalculateToTecNoFeeAmount(calculateToTecNoFeeAmount(couponDetailModel.getUsd_price()));
            couponDetailModel.setFeeAmount(calculateFeeAmount(couponDetailModel.getUsd_price()));
            tv_tec.setText(String.valueOf(couponDetailModel.getCalculateToTecIncludeFeeAmount()));
        }
    }

    private void reLoadPrice() {
        setPrice();
        timeReload = TCConstant.TIME_COUPON_RELOAD;
        countDownTimer.start();
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
                if (couponDetailModel != null && vendorCodeRedeemModel != null) {
                    vendorCodeRedeemModel = new VendorCodeRedeemModel(vendorCodeRedeemModel.getVendor_code(), couponDetailModel.getSerial());
                    addFragment(UserRedeemResultScreen.getInstance(couponDetailModel, vendorCodeRedeemModel));
                } else {
                    addFragment(UserCouponValidationCodeScreen.newInstance(couponDetailModel, false));
                }
            }
        } else if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue() && resultCode == Activity.RESULT_OK) {//no need data
            if (couponDetailModel != null) {
                addFragment(UserCouponValidationCodeScreen.newInstance(couponDetailModel, false));
            }
        }
    }

    private void fillData(CouponDetailController couponDetailController, CouponDetailModel couponDetailModel) {
        if (couponDetailModel.getVendor() != null) {
            tv_name_vendor.setText(couponDetailModel.getVendor().getName());
            tv_address.setText(couponDetailModel.getVendor().getAddress());

            rating.setRating(TCUtils.roundRating(couponDetailModel.getVendor().getRating()));
//            rating.setIsIndicator(true);
            Glide.with(getActiveActivity()).load(couponDetailModel.getBanner())
                    .apply(new RequestOptions().placeholder(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).error(TCUtils.getDrawable(R.drawable.ic_cover_chattee)))
                    .into(iv_banner);
            Glide.with(getActiveActivity()).load(couponDetailModel.getVendor().getLogo())
                    .apply(new RequestOptions().placeholder(TCUtils.getDrawable(R.drawable.ic_logo_chattee)).error(TCUtils.getDrawable(R.drawable.ic_logo_chattee)))
                    .into(iv_logo);

            latLng = TCUtils.vendorGetLocation(couponDetailModel.getVendor().getLocation());
            setupMap();
        }
        tv_name_coupon.setText(couponDetailModel.getName());

        setPrice();

        if (!TCUtils.isEmpty(couponDetailModel.getCatalogue_end())) {
            String day = TCDateUtility.formatTimeForMyCoupon(couponDetailModel.getCatalogue_end(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCDateUtility.DateFormatDefinition.HH_MM,
                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY);
            String format_day = String.format(TCUtils.getString(R.string.coupon_end_at), "<font color='#b2973f'>" + day + "</font>");
            tv_end_at.setText(Html.fromHtml(format_day));
        }
        tv_description.setText(couponDetailModel.getDescription());
        tv_term.setText(couponDetailModel.getTerm());
        tv_guide.setText(couponDetailModel.getGuide());
        tv_date_time_start.setText(TCDateUtility.formatTimeForMyCoupon(couponDetailModel.getStart(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCDateUtility.DateFormatDefinition.HH_MM,
                TCDateUtility.DateFormatDefinition.DD_MM_YYYY));

        tv_date_time_end.setText(TCDateUtility.formatTimeForMyCoupon(couponDetailModel.getEnd(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCDateUtility.DateFormatDefinition.HH_MM,
                TCDateUtility.DateFormatDefinition.DD_MM_YYYY));

        if (couponDetailModel.getHashTags() != null && couponDetailModel.getHashTags().size() > 0) {
            StringBuilder hash = new StringBuilder();
            for (String hashTag : couponDetailModel.getHashTags()) {
                hash.append(String.format("#%s ", hashTag));
            }
            tv_hash_tags.setText(hash);
            ll_hash_tags.setVisibility(View.VISIBLE);
        } else {
            ll_hash_tags.setVisibility(View.GONE);
        }
        if (couponDetailController.isRedeemExpired()) {
            if (!TCUtils.isEmpty(couponDetailModel.getRedeem_code())) {
                tv_redemption_code.setText(couponDetailModel.getRedeem_code());
                ll_redemption_code.setVisibility(View.VISIBLE);
            } else {
                ll_redemption_code.setVisibility(View.GONE);
            }
            if (!TCUtils.isEmpty(couponDetailModel.getRedeem_time())) {
                tv_redemption_date.setText(TCDateUtility.formatTimeForMyCoupon(couponDetailModel.getRedeem_time(),
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TCDateUtility.DateFormatDefinition.HH_MM,
                        TCDateUtility.DateFormatDefinition.DD_MM_YYYY));
                rl_redemption_date.setVisibility(View.VISIBLE);
            } else {
                rl_redemption_date.setVisibility(View.GONE);
            }

        } else {
            ll_redemption_code.setVisibility(View.GONE);
            rl_redemption_date.setVisibility(View.GONE);
        }
    }

    private void userGetCoupon(CouponDetailModel couponDetailModel) {
        requestApi(new CouponUserGetCouponRequest(couponDetailModel.getId(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                tv_scan_qr_code.setVisibility(View.VISIBLE);
                tv_get_coupon.setVisibility(View.GONE);
                tvTimeRemain.setVisibility(View.GONE);
                tv_purchase.setVisibility(View.GONE);
                ResultGetCouponModel resultGetCouponModel = (ResultGetCouponModel) response.getResult();
                if (resultGetCouponModel != null) {
                    couponDetailModel.setSerial(resultGetCouponModel.getSerial());
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }


    /**
     * if serial == null or '':
     * if can_purchase_multiple == false:
     * hide button
     * else:
     * if is_own == true:
     * button = Purchase Again
     * else:
     * button = Purchase
     * else:
     * if usage_times - used_times > 0:
     * button = Scan QR Code
     * else:
     * if can_purchase_multiple == false:
     * hide button
     * else:
     * button = Purchase Again
     *
     * @param couponDetailController
     * @param couponDetailModel
     */
    private void updateUI(CouponDetailController couponDetailController, CouponDetailModel couponDetailModel) {
//        if (couponDetailController.getCouponDetailFlow() instanceof CouponDetailFlowUserCoupon
//                || couponDetailController.getCouponDetailFlow() instanceof CouponDetailFlowTransactionDetail
//                || couponDetailController.getCouponDetailFlow() instanceof CouponDetailFlowNotification) {
//            view_tec.setVisibility(View.GONE);
//        }

        if (TCUtils.isEmpty(couponDetailModel.getSerial())
                || couponDetailModel.getSerial().equals("null")) {
            if (!couponDetailModel.isCan_purchase_multiple()) {
                tv_scan_qr_code.setVisibility(View.GONE);
                tv_get_coupon.setVisibility(View.GONE);
                tvTimeRemain.setVisibility(View.GONE);
                tv_purchase.setVisibility(View.GONE);
            } else {
                tv_scan_qr_code.setVisibility(View.GONE);
                tv_get_coupon.setVisibility(View.GONE);
                tvTimeRemain.setVisibility(View.VISIBLE);
                tv_purchase.setVisibility(View.VISIBLE);
                tv_purchase.setText(TCUtils.getString(couponDetailModel.isIs_own() ?
                        R.string.purchase_again : R.string.text_purchase));
            }
        } else {
            int usageCount = getUsageCouponCount(couponDetailModel);
            if (usageCount > 0 && !couponDetailController.isRedeemExpired()) {
                tv_scan_qr_code.setVisibility(View.VISIBLE);
                tv_get_coupon.setVisibility(View.GONE);
                tvTimeRemain.setVisibility(View.GONE);
                tv_purchase.setVisibility(View.GONE);
                tv_scan_qr_code.setText(String.format(TCUtils.getString(R.string.coupon_scan_qr_code), usageCount));
            } else {
                if (!couponDetailModel.isCan_purchase_multiple()) {
                    tv_scan_qr_code.setVisibility(View.GONE);
                    tvTimeRemain.setVisibility(View.GONE);
                    tv_purchase.setVisibility(View.GONE);
                    tv_get_coupon.setVisibility(View.GONE);
                } else {
                    tv_scan_qr_code.setVisibility(View.GONE);
                    tv_get_coupon.setVisibility(View.GONE);
                    tvTimeRemain.setVisibility(View.VISIBLE);
                    tv_purchase.setVisibility(View.VISIBLE);
                    tv_purchase.setText(TCUtils.getString(couponDetailModel.isIs_own() ?
                            R.string.purchase_again : R.string.text_purchase));
                }
            }
        }
    }

    private void handlePurchase() {
        countDownTimer.cancel();
        if (couponDetailModel == null)
            return;
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));

//        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        double balance = convertToDouble(accountModel.getBalance());
        double totalFee = convertToDouble(calculateToTecIncludeFeeAmount(couponDetailModel.getUsd_price()))
                + ((TCMainActivity) getActiveActivity()).getBasic_fee_amount();
        if (balance - totalFee < 0) {
            showBaseMessage(TCUtils.getString(R.string.coupons_message_balance_is_not_enough_to_purchase));
            return;
        }
        CouponCataloguePurchaseRequestModel purchaseRequestModel = new CouponCataloguePurchaseRequestModel(
                couponDetailModel.getId(),
                accountModel.getPublic_key(),
                couponDetailModel.getDestination(),
                couponDetailModel.getCalculateToTecNoFeeAmount(),
                couponDetailModel.getFee_amount_destination(),
                couponDetailModel.getFeeAmount(),
                couponDetailModel.getBasic_fee_amount_destination(),
                ((TCMainActivity) getActiveActivity()).getBasic_fee_amount()
        );

        TCAppFlyerTrackingEvent.getInstance().trackCouponPurchaseConfirm();

        StellarBusinessProcess.getInstance().startPurchase(purchaseRequestModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                TCLog.d("linhnt 6");
                showLoading(false);
                Bundle bundle = new Bundle();
                bundle.putString(TCGoogleAnalyticTrackingEvent.COUPON_ID, couponIdForGoogleAnalyticTracking);
                analyticLogEvent(TCGoogleAnalyticTrackingEvent.TrackType.PurchaseSuccess, "", bundle);
                CouponCataloguePurchaseResponseModel purchaseResponseModel = (CouponCataloguePurchaseResponseModel) response.getResult();
                purchaseResponseModel.setSuccess(true);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_PURCHASE_COUPON_RESULT.getValue(), UserGetCouponResultScreen.getInstance(purchaseResponseModel));
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                TCLog.e("linhnt 7" + errorModel.getErrorMessage());
                showLoading(false);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_PURCHASE_COUPON_RESULT.getValue(),
                        UserGetCouponResultScreen.getInstance(new CouponCataloguePurchaseResponseModel(false, errorModel.getErrorMessage())));
            }
        });
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_COUPON_USER_PURCHASE_COUPON_RESULT.getValue()
                && finishedResultCode == RESULT_OK) {
            finishWithResult(RESULT_OK, finishedResult);
        }
    }

    private boolean isCurrentDateSmallerThanEndDate(String endDateWithYYYY_MM_DD_T_HH_MM_SS_Z) {
        Date date = null;
        try {
            date = TCDateUtility.toCouponDate(endDateWithYYYY_MM_DD_T_HH_MM_SS_Z,
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z);
        } catch (ParseException e) {
            return false;
        }

        return date != null && TCDateUtility.getCurrentDate().before(date);
    }

    /**
     * how many usage, user can use
     * > 0, user has not use coupon yet
     * =0 , user already used coupon
     *
     * @param couponDetailModel
     * @return
     */
    private int getUsageCouponCount(CouponDetailModel couponDetailModel) {
        if (!TCUtils.isEmpty(couponDetailModel.getUsage_times()) && !TCUtils.isEmpty(couponDetailModel.getUsedTimes())) {
            return Integer.parseInt(couponDetailModel.getUsage_times()) - Integer.parseInt(couponDetailModel.getUsedTimes());
        }
        return 0;
    }


    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_coupon_user_my_coupon_detail_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setOnMarkerClickListener(marker -> true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.setOnMapClickListener(TCUtils::gotoDirection);
        moveCamera(latLng);

        if (tvTimeRemain.getVisibility() == View.VISIBLE) {
            countDownTimer.start();
        }
    }

    private void moveCamera(LatLng latLng) {
        if (latLng != null) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, TCConstant.MAP_ZOOM_DEFAULT);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    public String getCouponIdForGoogleAnalyticTracking() {
        return couponIdForGoogleAnalyticTracking;
    }

    public void reloadPriceCoupon() {
        if (tv_purchase.getVisibility() == View.GONE) {
            setPrice();
        }
    }
}
