package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.couponSystem.user.getCouponResult.CoinbackSettingAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.couponSystem.user.validationCode.UserCouponValidationCodeScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CoinBackModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponCoinBackRedemptionResponse;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponConbackRedemption;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserRedeemCouponRequest;
import com.teecoin.myapi.apirequest.couponsystem.GetRecommmendedCouponRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class UserRedeemResultScreen extends TCCouponBaseFragment {

    private static final String COUPON_REDEEM_DATA = "COUPON_REDEEM_DATA";

    @BindView(R.id.view_coupon_result_processing_ll_view_processing)
    View vProcessing;
    @BindView(R.id.frg_redeem_result_ll_redeem_success)
    View vRedeemSuccess;

    @BindView(R.id.frg_redeem_result_iv_status)
    ImageView ivStatus;
    @BindView(R.id.frg_redeem_result_tv_status)
    TextView tvStatus;
    @BindView(R.id.frg_redeem_result_tv_redeem)
    TextView tvRedeem;
    @BindView(R.id.frg_redeem_result_tv_redeem_serial)
    TextView tvRedeemSerial;
    @BindView(R.id.frg_redeem_result_tv_message_fail)
    TextView tvMessageFail;
    @BindView(R.id.frg_redeem_result_tv_thanks)
    TextView tvThanks;
    @BindView(R.id.frg_redeem_result_ll_coupon_for_you)
    View cvCouponForYou;
    @BindView(R.id.view_coupon_user_result_view_redeem)
    View view_redeem;

    @BindView(R.id.frg_redeem_result_rcv)
    RecyclerView recyclerView;

    @BindView(R.id.view_redemption_rcv)
    TCRecyclerView rcv_redeem;
    @BindView(R.id.view_redemption_tv_title_1)
    TextView tvTitleRedemption_1;
    @BindView(R.id.view_redemption_tv_title_2)
    TextView tvTitleRedemption_2;

    private UserCouponRedeemDataModel couponRedeemData;

    private CheckInCatalogueResultAdapter adapter;

    //redeem in UserMyCouponDetailScreen (2 case - from My Coupon and Notification)
    public static UserRedeemResultScreen getInstance(CouponDetailModel couponDetailModel, VendorCodeRedeemModel vendorCodeRedeemModel) {//---1
        UserRedeemResultScreen screen = new UserRedeemResultScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel couponRedeemData = new UserCouponRedeemDataModel(couponDetailModel, vendorCodeRedeemModel);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    //from validate code
    public static UserRedeemResultScreen getInstance(UserCouponRedeemDataModel couponRedeemData) {
        UserRedeemResultScreen screen = new UserRedeemResultScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    //redeem in UserGetCouponResultScreen - User use coupon after purchase
    public static UserRedeemResultScreen getInstance(CouponCataloguePurchaseResponseModel purchaseResponseModel, VendorCodeRedeemModel vendorCodeRedeemModel) {///---2
        UserRedeemResultScreen screen = new UserRedeemResultScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel couponRedeemData = new UserCouponRedeemDataModel(purchaseResponseModel, vendorCodeRedeemModel);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redeem_result, container, false);
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
            if (null != couponRedeemData && null != couponRedeemData.getVendorCodeRedeemModel()) {
                startRedeem(couponRedeemData.getVendorCodeRedeemModel());
            }
            getCoupon();
        }

        registerSingleClick(
                R.id.frg_check_in_result_tv_see_more, R.id.frg_redeem_result_ll_all, R.id.frg_redeem_result_tv_thanks);
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frg_check_in_result_tv_see_more, R.id.frg_redeem_result_ll_all, R.id.frg_redeem_result_tv_thanks);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.frg_check_in_result_tv_see_more:
                ((TCMainActivity) getActiveActivity()).selectCoupon();
                replaceFragment(UserCouponScreen.getInstance(), true);
                break;

            case R.id.frg_redeem_result_ll_all:
                //  ((TCMainActivity) getActiveActivity()).selectDiscover();
                //  replaceFragment(VendorScreen.getInstance(couponRedeemData.getVendor().getId(), true), true);
                ((TCMainActivity) getActiveActivity()).selectCoupon();
                replaceFragment(UserCouponScreen.getInstance(), true);
                break;
            case R.id.frg_redeem_result_tv_thanks:
                if (couponRedeemData.getVendor() != null) {
                    addFragment(VendorScreen.getInstance(couponRedeemData.getVendor().getId()));
                }

                break;

        }
    }

    private void startRedeem(VendorCodeRedeemModel vendorCodeRedeemModel) {
        requestApi(new CouponUserRedeemCouponRequest(vendorCodeRedeemModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {

                    RedeemCodeCouponResultModel redeemCodeCouponResultModel = (RedeemCodeCouponResultModel) response.getResult();

                    updateStatus(redeemCodeCouponResultModel);
                    // initRedeemtion(redeemCodeCouponResultModel);
                    if (redeemCodeCouponResultModel != null) {
                        if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.SUCCESS.getValue()) {
                            getCoinbackRedemption(true);
                        } else {
                            getCoinbackRedemption(false);
                        }
                    } else {
                        getCoinbackRedemption(false);
                    }

                } else {
                    updateStatus(null);
                    getCoinbackRedemption(false);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showViewUnSuccess();
//                updateStatus(null);
                getCoinbackRedemption(false);
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void updateStatus(RedeemCodeCouponResultModel redeemCodeCouponResultModel) {
        vProcessing.setVisibility(View.GONE);
        vRedeemSuccess.setVisibility(View.VISIBLE);

        if (null != redeemCodeCouponResultModel) {
            tvThanks.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.thank_you_for_visiting_s),
                    couponRedeemData.getVendor() != null ? couponRedeemData.getVendor().getName() : "")));
            if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.SUCCESS.getValue()) {

                ivStatus.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_check_success));
                tvStatus.setText(TCUtils.getString(R.string.coupon_successful));

                if (null != couponRedeemData) {
                    tvRedeem.setVisibility(View.VISIBLE);
                    tvRedeemSerial.setVisibility(View.VISIBLE);
                    //tvRedeemSerial.setText(couponRedeemData.getSerial());
                    tvRedeemSerial.setText(redeemCodeCouponResultModel.getRedeem_code());
                }

            } else if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.TRY_AGAIN.getValue()) {
                tvRedeem.setVisibility(View.GONE);
                if (null != couponRedeemData) {
                    replaceFragment(UserCouponValidationCodeScreen.newInstance(couponRedeemData, true), false);
                }
            } else if (redeemCodeCouponResultModel.getRedeemStatus() == RedeemCodeCouponResultModel.RedeemStatus.VALIDITY_TIME.getValue()) {

                showViewUnSuccess();

                tvMessageFail.setText(String.format(TCUtils.getString(R.string.redeem_validity_time), redeemCodeCouponResultModel.getVendor().getName()));
            } else {

                showViewUnSuccess();

                tvMessageFail.setText(redeemCodeCouponResultModel.getMessageErrorCheckIn());
            }
        } else {

            showViewUnSuccess();

            tvMessageFail.setText(TCUtils.getString(R.string.sorry_something_went_wrong));

        }
    }

    private void showViewUnSuccess() {
        ivStatus.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_fail));
        tvStatus.setTextColor(TCUtils.getColor(R.color.c_8e8e93));
        tvStatus.setText(TCUtils.getString(R.string.coupon_unsuccessful));
        tvMessageFail.setVisibility(View.VISIBLE);
        tvRedeem.setVisibility(View.GONE);
        //tvThanks.setVisibility(View.GONE);
        //view_redeem.setVisibility(View.GONE);
        //cvCouponForYou.setVisibility(View.GONE);
    }

    private void initCatalogueCoupon(ArrayList<UserCouponCatalogueDataModel> list) {
        if (list.size() > 0) {

            cvCouponForYou.setVisibility(View.VISIBLE);

            adapter = new CheckInCatalogueResultAdapter(LayoutInflater.from(getActiveActivity()), list, (view, item, position, clickType) -> {
                UserCouponCatalogueDataModel userCouponCatalogueDataModel = new UserCouponCatalogueDataModel();
                userCouponCatalogueDataModel.setId(item.getId());
                addFragment(UserMyCouponDetailScreen.newInstance(userCouponCatalogueDataModel));
            });

            recyclerView.setAdapter(adapter);
        }
    }

    private void getCoinbackRedemption(boolean redeemSuccess) {
        ArrayList<CoinBackModel> listCoinback = new ArrayList<>();
        requestApi(new CouponConbackRedemption(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                CouponCoinBackRedemptionResponse couponConbackRedemption = (CouponCoinBackRedemptionResponse) response.getResult();
                if (couponConbackRedemption != null) {
                    listCoinback.addAll(couponConbackRedemption.getCoin_back_setting());
                    if (redeemSuccess) {
                        if (TCUtils.convertToDouble(couponConbackRedemption.getCoin_back_amount()) == 0) {
                            tvTitleRedemption_1.setText(TCUtils.getString(R.string.we_regret_that_currently_this_merchant_is_not_eligible_for_coinback_redemption_event));

                        } else {
                            tvTitleRedemption_1.setText(String.format(TCUtils.getString(R.string.redeem_success_received_tec_message), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, couponConbackRedemption.getCoin_back_amount())));
                        }
                    }

                    if (!TCUtils.isEmpty(couponConbackRedemption.getRedemption_count())) {
                        int redeem_count = Integer.parseInt(couponConbackRedemption.getRedemption_count());
                        if (redeem_count <= listCoinback.size()) {
                            for (int i = 0; i < redeem_count; i++) {
                                listCoinback.get(i).setSelected(true);
                            }
                        } else {
                            if (redeemSuccess && TCUtils.convertToDouble(couponConbackRedemption.getCoin_back_amount()) > 0) {
                                tvTitleRedemption_1.setText(String.format(TCUtils.getString(R.string.redeem_success_received_tec_message_over), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, couponConbackRedemption.getCoin_back_amount())));
                            }
                            for (int i = 0; i < listCoinback.size(); i++) {
                                listCoinback.get(i).setSelected(true);
                            }
                        }

                    }
                    CoinbackSettingAdapter coinbackSettingAdapter = new CoinbackSettingAdapter(LayoutInflater.from(getActiveActivity()), listCoinback, (view, item, position, clickType) -> {

                    });
                    rcv_redeem.setAdapter(coinbackSettingAdapter);

                } else {
                    view_redeem.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                view_redeem.setVisibility(View.GONE);
            }
        }));

    }

    private void getCoupon() {
        if(couponRedeemData==null)
            return;
        cvCouponForYou.setVisibility(View.GONE);
        requestApi(new GetRecommmendedCouponRequest(couponRedeemData.getVendorCodeRedeemModel().getId(),TCUtils.paramsGetCategoryCoupon(getLatLngCurrent(), getCountryCodeModel().getCountry_code(), "", "", ""), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                BaseResultsResponseModel<UserCouponCatalogueDataModel> catalogueResponseModel
                        = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult());
                ArrayList<UserCouponCatalogueDataModel> list = catalogueResponseModel.getResults();
                initCatalogueCoupon(list);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    public void reloadPriceCoupon() {
        if (null != adapter)
            adapter.notifyDataSetChanged();
    }
}
