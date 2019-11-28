package com.teecoin.feature.payment.shopPaymentSuccess;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.payment.shopInputPayment.ShopInputPaymentScreen;
import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ShopPaymentSuccessScreen extends TCWalletBaseFragment {
    private static final String PAYMENT_INVOICE_SUBMIT_MODEL = "PAYMENT_INVOICE_SUBMIT_MODEL";
    @BindView(R.id.frag_shop_payment_success_tv_customer)
    TextView tv_customer;
    @BindView(R.id.frag_shop_payment_success_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frag_shop_payment_success_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frag_shop_payment_success_ll_coinback)
    View ll_coinback;

    @BindView(R.id.frag_shop_payment_success_tv_coinback)
    TextView tv_coinback;
    @BindView(R.id.frag_shop_payment_success_tv_amount_cash)
    TextView tv_amount_cash;
    @BindView(R.id.frag_shop_payment_success_tv_new_payment)
    TextView tv_new_payment;

    private PaymentInvoiceSubmitModel payment;

    public static ShopPaymentSuccessScreen getInstance(PaymentInvoiceSubmitModel payment) {
        ShopPaymentSuccessScreen screen = new ShopPaymentSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_INVOICE_SUBMIT_MODEL, payment);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_payment_suceess, container, false);
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
            payment = (PaymentInvoiceSubmitModel) bundle.getSerializable(PAYMENT_INVOICE_SUBMIT_MODEL);
            setData();
        }
        registerSingleClick(R.id.frag_shop_payment_success_tv_new_payment);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_shop_payment_success_tv_new_payment:
                replaceFragment(ShopInputPaymentScreen.getInstance(), true);
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_shop_payment_success_tv_new_payment);
    }

    private void setData() {
        if (payment == null)
            return;
        tv_customer.setText(payment.getCustomer());
        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, payment.getInvoice_cash_amount()), payment.getCurrency_code()));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, payment.getPaid_cash_amount()), payment.getCurrency_code()));
        tv_coinback.setText(String.format("-%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, payment.getCoinback_cash_amount()), payment.getCurrency_code()));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, payment.getRemain_cash_amount()), payment.getCurrency_code()));
        ll_coinback.setVisibility(TCUtils.convertToDouble(payment.getCoinback_cash_amount()) > 0 ? View.VISIBLE : View.GONE);
    }
}
