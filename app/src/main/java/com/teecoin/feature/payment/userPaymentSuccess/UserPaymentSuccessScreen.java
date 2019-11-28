package com.teecoin.feature.payment.userPaymentSuccess;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponCatalogue.RecommendCouponsHorizontalAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.UserDiscoverGetRecommendCouponsRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class UserPaymentSuccessScreen extends TCCouponBaseFragment implements APIResponseListener {
    private static final String PAYMET_SUCCESS_MODEL = "PAYMET_SUCCESS_MODEL";

    @BindView(R.id.frag_user_payment_success_tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.frag_user_payment_success_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frag_user_payment_success_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frag_user_payment_success_tv_converd_to_tec)
    TextView tv_converd_to_tec;
    @BindView(R.id.frag_user_payment_success_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frag_user_payment_success_tv_amount_cash)
    TextView tv_amount_cash;


    @BindView(R.id.frag_user_payment_success_v_coupon)
    View view_coupon;
    @BindView(R.id.frag_user_payment_success_v_all_coupon)
    View view_all_coupon;
    @BindView(R.id.frag_user_payment_success_rcv_coupon)
    TCRecyclerView rcv_coupon;
    private ArrayList<UserCouponCatalogueDataModel> listCouponAndCashVoucher;

    private PaymentInvoiceDetailModel paymentSuccessModel;

    private RecommendCouponsHorizontalAdapter recommendCouponsHorizontalAdapter;

    public static UserPaymentSuccessScreen getInstance(PaymentInvoiceDetailModel paymentSuccessModel) {
        UserPaymentSuccessScreen screen = new UserPaymentSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMET_SUCCESS_MODEL, paymentSuccessModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_payment_success, container, false);
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
            paymentSuccessModel = (PaymentInvoiceDetailModel) bundle.getSerializable(PAYMET_SUCCESS_MODEL);
            setData();
        }
        registerSingleClick(R.id.frag_user_payment_success_v_all_coupon, R.id.frag_user_payment_success_tv_back_to_wallet);
        setupRecyclerView();
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_user_payment_success_v_all_coupon:
                ((TCMainActivity) getActiveActivity()).openCouponUserScreen();
                break;
            case R.id.frag_user_payment_success_tv_back_to_wallet:
                openWalletScreen(false, true);
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_user_payment_success_v_all_coupon, R.id.frag_user_payment_success_tv_back_to_wallet);
    }

    private void setData() {
        if (paymentSuccessModel == null)
            return;
        if (paymentSuccessModel.getVendor() != null) {
            tv_merchant.setText(paymentSuccessModel.getVendor().getName());
        }

        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentSuccessModel.getInvoice_cash_amount()), TCUtils.getString(R.string.text_sgd)));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentSuccessModel.getPaid_amount()), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_converd_to_tec.setText(String.format("%s %s %s", TCUtils.getString(R.string.text_character), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentSuccessModel.getPaid_cash_amount()), TCUtils.getString(R.string.text_sgd)));
        tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentSuccessModel.getFee_amount()) + TCUtils.convertToDouble(paymentSuccessModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentSuccessModel.getRemain_cash_amount()), TCUtils.getString(R.string.text_sgd)));
    }

    private void setupRecyclerView() {
        listCouponAndCashVoucher = new ArrayList<>();
        recommendCouponsHorizontalAdapter =
                new RecommendCouponsHorizontalAdapter(LayoutInflater.from(getActiveActivity()),
                        listCouponAndCashVoucher,
                        true,
                        (view, item, position, clickType) -> {
                            addFragment(UserMyCouponDetailScreen.newInstance(item));
                        });
        rcv_coupon.setAdapter(recommendCouponsHorizontalAdapter);

        getData();
    }

    private void getData() {
        requestApi(new UserDiscoverGetRecommendCouponsRequest(getLocation(), getCountryCodeModel().getCountry_code(), this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_RECOMMEND) {
            BaseResultsResponseModel<UserCouponCatalogueDataModel> catalogueResponseModel
                    = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult());
            ArrayList<UserCouponCatalogueDataModel> list = catalogueResponseModel.getResults();
            if (list != null && list.size() > 0) {
                listCouponAndCashVoucher.addAll(list);
                rcv_coupon.onLoadMoreComplete();
            }
            view_coupon.setVisibility(listCouponAndCashVoucher.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_RECOMMEND) {
            view_coupon.setVisibility(listCouponAndCashVoucher.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    public void reloadPriceCoupon() {
        if (null != recommendCouponsHorizontalAdapter)
            recommendCouponsHorizontalAdapter.notifyDataSetChanged();
    }

}
