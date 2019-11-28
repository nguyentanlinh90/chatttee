package com.teecoin.feature.walletSystem.paymentThreshold;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class PaymentThresholdScreen extends TCWalletBaseFragment {

    @BindView(R.id.frg_payment_threshold_sw_payment)
    Switch sw_payment;
    @BindView(R.id.frg_payment_threshold_ll_switch)
    LinearLayout ll_switch;
    @BindView(R.id.frg_paymet_threshold_ll_maximum)
    RelativeLayout ll_maximum;
    @BindView(R.id.frg_payment_threshold_tv_maximum)
    TextView tv_maximum;

    private PaymentThresholdModel paymentThresholdModel;

    public static PaymentThresholdScreen getInstance() {
        return new PaymentThresholdScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_threshold, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.text_payment_threshold).toUpperCase());
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
        paymentThresholdModel = new PaymentThresholdModel(RealmController.getInstance().getData(PaymentThresholdModel.class));
        tv_maximum.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentThresholdModel.getMaximum()));
        sw_payment.setChecked(paymentThresholdModel.isPayment_threshold());
    }

    private void initClickEvent() {
        sw_payment.setOnCheckedChangeListener((v, hasFocus) -> showPaymentThresholdDialog());
        ll_maximum.setOnClickListener(v -> showMaximumDialog());
    }

    private void showPaymentThresholdDialog() {
        if (sw_payment.isChecked()) {
            PaymentThresholdDialog paymentThresholdDialog = new PaymentThresholdDialog(getActiveActivity(), new PaymentThresholdListener() {
                @Override
                public void onPaymentThresholdConfirm() {
                    updatePaymentThreshold(true);
                }

                @Override
                public void onPaymentThresholdCancel() {
                    setPaymentSwitch(false);
                }
            });
            paymentThresholdDialog.setCanceledOnTouchOutside(false);
            paymentThresholdDialog.show();
        } else {
            updatePaymentThreshold(false);
        }
    }

    private void showMaximumDialog() {

        MaximumPaymentThresholdDialog maximumPaymentThresholdDialog = new MaximumPaymentThresholdDialog(getActiveActivity(), tv_maximum.getText().toString(), new MaximumPaymentThresholdListener() {
            @Override
            public void onMaximumThresholdFinish(String number) {
                paymentThresholdModel.setMaximum(TCUtils.convertToDouble(number));
                RealmController.getInstance().updatePaymentThreshold(paymentThresholdModel);
                tv_maximum.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, number));
            }

            @Override
            public void onMaximumThresholdCancel() {

            }
        });
        maximumPaymentThresholdDialog.setCanceledOnTouchOutside(false);
        maximumPaymentThresholdDialog.show();
    }

    private void setPaymentSwitch(boolean isCheck) {
        sw_payment.setOnCheckedChangeListener(null);
        sw_payment.setChecked(isCheck);
        sw_payment.setOnCheckedChangeListener((v, hasFocus) -> showPaymentThresholdDialog());
    }

    private void updatePaymentThreshold(boolean isThreshold) {
        paymentThresholdModel.setPayment_threshold(isThreshold);
        RealmController.getInstance().updatePaymentThreshold(paymentThresholdModel);
    }
}
