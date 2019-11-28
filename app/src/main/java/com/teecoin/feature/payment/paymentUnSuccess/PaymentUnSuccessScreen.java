package com.teecoin.feature.payment.paymentUnSuccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.payment.shopInputPayment.ShopInputPaymentScreen;
import com.teecoin.feature.payment.shopPaymentSuccess.ShopPaymentSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopCheckPaymentRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class PaymentUnSuccessScreen extends TCWalletBaseFragment {
    private static final String ERROR_MODEL = "ERROR_MODEL";
    private static final String FAILED = "FAILED";
    private static final String INVOICE_ID = "INVOICE_ID";
    @BindView(R.id.frg_transaction_iv_fail)
    ImageView iv_fail;
    @BindView(R.id.frg_payment_unsuccess_tv_title)
    TextView tv_title;
    @BindView(R.id.frg_payment_unsuccess_tv_message)
    TextView tv_message;

    @BindView(R.id.frag_payment_unsuccess_tv_try_again)
    TextView tv_try_again;
    private ErrorModel errorModel;
    private boolean failed;
    private String invoiceID;

    public static PaymentUnSuccessScreen getInstance(ErrorModel errorModel, String invoiceID, boolean failed) {
        PaymentUnSuccessScreen screen = new PaymentUnSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ERROR_MODEL, errorModel);
        bundle.putSerializable(FAILED, failed);
        bundle.putSerializable(INVOICE_ID, invoiceID);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_unsuccess, container, false);
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
            errorModel = (ErrorModel) bundle.getSerializable(ERROR_MODEL);
            failed = bundle.getBoolean(FAILED);
            invoiceID = bundle.getString(INVOICE_ID);
            updateUI();
        }
        registerSingleClick(R.id.frag_payment_unsuccess_tv_try_again, R.id.frag_payment_success_tv_cancel);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_payment_unsuccess_tv_try_again:
                onTryAgain();
                break;
            case R.id.frag_payment_success_tv_cancel:
                if (!isAppUser())
                    replaceFragment(ShopInputPaymentScreen.getInstance(), true);
                else
                    // handleBackPressed();
                    openWalletScreen(false, true);
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_payment_unsuccess_tv_try_again, R.id.frag_payment_success_tv_cancel);
    }

    private void updateUI() {
        //  TCLog.d("failed "+failed);
        if (errorModel != null) {
            tv_message.setText(errorModel.getErrorMessage());
            if (errorModel.getError_code().equals(EnumMgr.ErrorCode.ERR081.getValue())) {
                tv_try_again.setVisibility(View.GONE);
            }
        }
        if (!isAppUser()) {
            if (!failed) {
                updateStatusProcessing();
            } else {
                tv_try_again.setText(TCUtils.getString(R.string.try_again));
            }

        }
    }

    public void updateStatusFail() {
        this.failed = true;
        tv_title.setText(TCUtils.getString(R.string.coupon_unsuccessful));
        tv_message.setText(TCUtils.getString(R.string.shop_payment_unsuccess_title));
        iv_fail.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_fail));
        tv_try_again.setText(TCUtils.getString(R.string.try_again));
    }

    private void checkStatusPayment() {
        if (!TCUtils.isEmpty(invoiceID))
            requestApi(new ShopCheckPaymentRequest(invoiceID, this));
    }

    private void updateStatusProcessing() {
        iv_fail.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_message));
        tv_title.setText(TCUtils.getString(R.string.payment_processing));
        tv_message.setText(TCUtils.getString(R.string.the_payment_is_being_processed));
        tv_try_again.setText(TCUtils.getString(R.string.check_payment_title));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_CHECK_PAYMENT) {
            PaymentInvoiceSubmitModel payment = (PaymentInvoiceSubmitModel) response.getResult();
            if (payment != null) {
                if (payment.getStatus().toLowerCase().equals(TCConstant.TAG_SUCCESS.toLowerCase())) {
                    replaceFragment(ShopPaymentSuccessScreen.getInstance(payment), true);
                } else if (payment.getStatus().toLowerCase().equals(TCConstant.TAG_FAILED.toLowerCase())) {
                    updateStatusFail();
                } else if (payment.getStatus().toLowerCase().equals(TCConstant.TAG_PAYMENT_UNKNOWN.toLowerCase())) {
                    updateStatusProcessing();
                }

            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_CHECK_PAYMENT) {
            tv_message.setText(errorModel.getErrorMessage());
        }
    }

    private void onTryAgain() {
        if (isAppUser()) {
            onBack();
        } else {
            if (failed) {
                onBack();
            } else {
                checkStatusPayment();
            }
        }
    }

    private void onBack() {
        Intent intent = new Intent();
        finishWithResult(RESULT_OK, intent);
    }
}
