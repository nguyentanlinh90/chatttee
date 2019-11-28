package com.teecoin.feature.payment.shopPaymentQRCode;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.firebase.MyFireBase;
import com.teecoin.feature.general.popup.DialogConfirmGeneral;
import com.teecoin.feature.payment.paymentUnSuccess.PaymentUnSuccessScreen;
import com.teecoin.feature.payment.shopPaymentSuccess.ShopPaymentSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.InvoiceModel;
import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopCheckPaymentRequest;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopPaymentInvoiceRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.Objects;

import butterknife.BindView;

public class ShopPaymentQRCodeScreen extends TCWalletBaseFragment implements MyFireBase.PaymentListener {
    private static final String PAYMENT_INVOICE_MODEL = "PAYMENT_INVOICE_MODEL";
    private static final String PAYMENT_INVOICE_SUBMIT_MODEL = "PAYMENT_INVOICE_SUBMIT_MODEL";
    @BindView(R.id.frag_shop_payment_iv_qr_code)
    ImageView iv_qr_code;
    @BindView(R.id.frag_shop_payment_qr_code_tv_renew_in)
    TextView tv_renew_in;
    @BindView(R.id.frag_shop_payment_qr_code_tv_renew)
    TextView tv_renew;
    private InvoiceModel invoiceModel;
    private CountDownTimer countDownTimer;
    private PaymentInvoiceSubmitModel paymentInvoiceSubmitModel;
    private MyFireBase myFireBase;
    private boolean paymentFailed;

    public static ShopPaymentQRCodeScreen getInstance(InvoiceModel invoiceModel, PaymentInvoiceSubmitModel paymentInvoiceSubmitModel) {
        ShopPaymentQRCodeScreen screen = new ShopPaymentQRCodeScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_INVOICE_MODEL, invoiceModel);
        bundle.putSerializable(PAYMENT_INVOICE_SUBMIT_MODEL, paymentInvoiceSubmitModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_payment_qr_code, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.qr_code));
        showFooter();
        showTabMenuBottom();
        showButtonBackToolbar();
        hideViewQrCode();
        hideButtonAddCoupon();
        handleOnbackDevice();
    }

    @Override
    public void onBindView() {
        tv_renew.setVisibility(View.GONE);
        myFireBase = new MyFireBase(getActiveActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            invoiceModel = (InvoiceModel) bundle.getSerializable(PAYMENT_INVOICE_MODEL);
            paymentInvoiceSubmitModel = (PaymentInvoiceSubmitModel) bundle.getSerializable(PAYMENT_INVOICE_SUBMIT_MODEL);
            createQRCode();
        }
        registerSingleClick(R.id.frag_shop_payment_qr_code_tv_renew, R.id.frag_shop_payment_qr_code_tv_check_payment);

    }

    private void createQRCode() {
        if (invoiceModel == null)
            return;
        iv_qr_code.setImageBitmap(TCUtils.createQRCode(TCUtils.convertToJson(invoiceModel)));
        paymentListener(invoiceModel.getInvoice());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer();
            }
        }, 400);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_shop_payment_qr_code_tv_check_payment:
                checkStatusPayment();
                break;
            case R.id.frag_shop_payment_qr_code_tv_renew:
                resendPaymentInvoice();
                break;

        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_shop_payment_qr_code_tv_renew_in, R.id.frag_shop_payment_qr_code_tv_check_payment);
    }

    private void resendPaymentInvoice() {
        if (paymentInvoiceSubmitModel == null)
            return;
        requestApi(new ShopPaymentInvoiceRequest(paymentInvoiceSubmitModel, this));
    }

    private void countDownTimer() {
        String resendString = TCUtils.getString(R.string.renew_in_s);
        // tv_renew_in.setEnabled(false);
        if (countDownTimer != null)
            countDownTimer = null;
        tv_renew.setVisibility(View.GONE);
        tv_renew_in.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(TCConstant.SIX_HUNDRED_SECOND_IN_MILLISECOND, TCConstant.ONE_SECOND_IN_MILLISECOND) {
            public void onTick(long millisUntilFinished) {
                tv_renew_in.setText(String.format(resendString, millisUntilFinished / 1000));
            }

            public void onFinish() {
                tv_renew_in.setVisibility(View.GONE);
                tv_renew.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    //private
    private void checkStatusPayment() {
        if (invoiceModel == null)
            return;
        this.paymentFailed = false;
        requestApi(new ShopCheckPaymentRequest(invoiceModel.getInvoice(), this));
    }

    public void onBack() {
        new DialogConfirmGeneral(getActiveActivity(), TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.payment_are_you_sure_you_want_to_back), TCUtils.getString(R.string.text_no), TCUtils.getString(R.string.text_yes), new TCConfirmListener() {
            @Override
            public void onConfirmed(int id, Object onWhat) {
                // handleBackPressed();
                onBackResult();
            }
        }).show();

    }

    public String getInvoidID() {
        return TCUtils.isEmpty(invoiceModel.getInvoice()) ? "" : invoiceModel.getInvoice();
    }

    public void userFinishPayment(TransactionDetailModel transactionDetailModel) {
        if (transactionDetailModel != null) {
            requestApi(new ShopCheckPaymentRequest(transactionDetailModel.getInvoice(), this));
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_PAYMENT_INVOICE) {
            invoiceModel = (InvoiceModel) response.getResult();
            if (invoiceModel != null) {
                iv_qr_code.setImageBitmap(TCUtils.createQRCode(TCUtils.convertToJson(invoiceModel)));
                paymentListener(invoiceModel.getInvoice());
                countDownTimer();
            }
        } else if (requestTarget == WalletRequestTarget.SHOP_CHECK_PAYMENT) {
            PaymentInvoiceSubmitModel payment = (PaymentInvoiceSubmitModel) response.getResult();
            //TCLog.e(" 1 "+appIsOpening());
            checkScreenPayment(payment);

        }

    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue() && finishedResultCode == RESULT_OK) {
            onBackResult();
        }
    }

    public void onBackResult() {
        Intent intent = new Intent();
        finishWithResult(RESULT_OK, intent);
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_PAYMENT_INVOICE) {
            new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok)).show();
        } else if (requestTarget == WalletRequestTarget.SHOP_CHECK_PAYMENT) {
            replaceFragment(PaymentUnSuccessScreen.getInstance(errorModel, invoiceModel.getInvoice(), this.paymentFailed), true);
        }
    }

    private void handleOnbackDevice() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBack();
                return true;
            }
            return false;
        });
    }

    private void paymentListener(String invoice) {
        // TCLog.e(" payment with invoice "+invoice);
        if (myFireBase != null)
            myFireBase.invoiceStreaming(invoice, this);
    }

    private void checkScreenPayment(PaymentInvoiceSubmitModel payment) {
        if (payment != null && appIsOpening()) {
            if (payment.getStatus().toLowerCase().equals(TCConstant.TAG_SUCCESS.toLowerCase())) {
                replaceFragment(ShopPaymentSuccessScreen.getInstance(payment), true);
            } else if (payment.getStatus().toLowerCase().equals(TCConstant.TAG_FAILED.toLowerCase())) {
                if (getTopFragment() != null) {
                    if (getTopFragment() instanceof ShopPaymentQRCodeScreen) {
                        tv_renew_in.setVisibility(View.GONE);
                        tv_renew.setVisibility(View.VISIBLE);
                    } else if (getTopFragment() instanceof PaymentUnSuccessScreen) {
                        PaymentUnSuccessScreen paymentUnSuccessScreen = (PaymentUnSuccessScreen) getTopFragment();
                        paymentUnSuccessScreen.updateStatusFail();
                    } else {
                        openPaymentUnSuccessScreen();
                    }
                }
            } else {
                openPaymentUnSuccessScreen();
            }
        }

    }

    private void openPaymentUnSuccessScreen() {
        addFragmentForResult(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue(), PaymentUnSuccessScreen.getInstance(null, invoiceModel.getInvoice(), this.paymentFailed));

    }

    @Override
    public void onPaymentStatus(String invoice, boolean failed) {
        this.paymentFailed = failed;
        requestApi(new ShopCheckPaymentRequest(invoice, this));
    }
}
