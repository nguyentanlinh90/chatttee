package com.teecoin.feature.payment.userReviewPayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.payment.paymentUnSuccess.PaymentUnSuccessScreen;
import com.teecoin.feature.payment.userPaymentSuccess.UserPaymentSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class UserReviewPaymentScreen extends TCWalletBaseFragment {
    private static final String PAYMENT_INVOICE_DETAIL_MODEL = "PAYMENT_INVOICE_DETAIL_MODEL";
    @BindView(R.id.frg_shop_input_payment_iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.frg_shop_input_payment_tv_name)
    TextView tv_name;
    @BindView(R.id.frg_shop_input_payment_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_user_review_payment_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frg_user_review_payment_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frg_user_review_payment_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frg_user_review_payment_tv_converd_to_tec)
    TextView tv_converd_to_tec;
    @BindView(R.id.frg_user_review_payment_tv_amount_cash)
    TextView tv_amount_cash;
    @BindView(R.id.frg_shop_review_payment_tv_amount)
    TextView tv_amount;
    @BindView(R.id.frg_user_review_payment_ll_coinback)
    View ll_coinback;

    @BindView(R.id.frg_shop_review_payment_tv_coinback)
    TextView tv_coinback;

    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView tv_confirm;
    // private InvoiceModel invoiceModel;
    private PaymentInvoiceDetailModel paymentInvoiceDetailModel;

    public static UserReviewPaymentScreen getInstance(PaymentInvoiceDetailModel paymentInvoiceDetailModel) {
        UserReviewPaymentScreen screen = new UserReviewPaymentScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_INVOICE_DETAIL_MODEL, paymentInvoiceDetailModel);
        screen.setArguments(bundle);
        return screen;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_review_payment, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.payment_review));
        hideFooter();
        showTabMenuBottom();
        showButtonBackToolbar();
        hideViewQrCode();
        hideButtonAddCoupon();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            paymentInvoiceDetailModel = (PaymentInvoiceDetailModel) bundle.getSerializable(PAYMENT_INVOICE_DETAIL_MODEL);
            //getPaymentInvoiceDetail();
            updateUI();
        }
        registerSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
        tv_cancel.setText(TCUtils.getString(R.string.text_cancel));
        tv_confirm.setText(TCUtils.getString(R.string.confirm));

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_cancel_next_bottom_tv_next:
                checkBalance();

                break;
            case R.id.view_cancel_next_bottom_tv_cancel:
                handleBackPressed();
                break;

        }
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
    }

    private void updateUI() {
        if (paymentInvoiceDetailModel == null)
            return;

        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInvoiceDetailModel.getInvoice_cash_amount()), TCUtils.getString(R.string.text_sgd)));
        tv_amount_wallet.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInvoiceDetailModel.getPaid_amount()));
        tv_converd_to_tec.setText(String.format("%s  %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInvoiceDetailModel.getPaid_cash_amount()), TCUtils.getString(R.string.text_sgd)));
        tv_fee.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentInvoiceDetailModel.getFee_amount()) + TCUtils.convertToDouble(paymentInvoiceDetailModel.getBasic_fee_amount())));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInvoiceDetailModel.getRemain_cash_amount()), TCUtils.getString(R.string.text_sgd)));
        tv_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentInvoiceDetailModel.getPaid_amount()) + TCUtils.convertToDouble(paymentInvoiceDetailModel.getFee_amount()) + TCUtils.convertToDouble(paymentInvoiceDetailModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_coinback.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInvoiceDetailModel.getCoinback_amount()), TCUtils.getString(R.string.tee_coin_symbol)));
        ll_coinback.setVisibility(TCUtils.convertToDouble(paymentInvoiceDetailModel.getCoinback_amount()) > 0 ? View.VISIBLE : View.GONE);
        if (paymentInvoiceDetailModel.getVendor() != null) {
            tv_name.setText(paymentInvoiceDetailModel.getVendor().getName());
            tv_address.setText(paymentInvoiceDetailModel.getVendor().getAddress());
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(paymentInvoiceDetailModel.getVendor().getLogo()) ? TCUtils.getDrawable(R.drawable.ic_shop_icon)
                    : paymentInvoiceDetailModel.getVendor().getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_avatar);
        }

    }

    private void checkBalance() {
        if (paymentInvoiceDetailModel == null)
            return;
        if (TCUtils.getBalance() < (TCUtils.convertToDouble(paymentInvoiceDetailModel.getPaid_amount()) + TCUtils.convertToDouble(paymentInvoiceDetailModel.getFee_amount()) + TCUtils.convertToDouble(paymentInvoiceDetailModel.getBasic_fee_amount()))) {
            showPopupBalanceEnough(TCUtils.getString(R.string.user_payment_message_enough_tec));
            return;
        }
        startPayment();
    }

    private void startPayment() {
        if (paymentInvoiceDetailModel == null)
            return;
        StellarBusinessProcess.getInstance().startPayment(paymentInvoiceDetailModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                PaymentInvoiceDetailModel paymentSuccessModel = (PaymentInvoiceDetailModel) response.getResult();
                addFragment(UserPaymentSuccessScreen.getInstance(paymentSuccessModel));
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                //  replaceFragment(PaymentUnSuccessScreen.getInstance(),false);
                addFragmentForResult(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue(), PaymentUnSuccessScreen.getInstance(errorModel, null, false));
            }
        });

    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue() && finishedResultCode == RESULT_OK) {
            Intent intent = new Intent();
            finishWithResult(RESULT_OK, intent);
        }
    }
}
