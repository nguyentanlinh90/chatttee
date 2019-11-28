package com.teecoin.feature.walletSystem.paymentThreshold;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class PaymentThresholdDialog extends TCBaseDialog {
    @BindView(R.id.dialog_payment_threshold_iv_close)
    ImageView iv_close;
    @BindView(R.id.ll_bt_cancel)
    View ll_bt_cancel;
    @BindView(R.id.ll_bt_ok)
    View ll_bt_ok;
    @BindView(R.id.dialog_payment_threshold_tv_maximum)
    TextView tv_maximum;


    private PaymentThresholdListener listener;

    PaymentThresholdDialog(Context context, PaymentThresholdListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment_threshold);
    }

    @Override
    protected void initContentView() {
        PaymentThresholdModel paymentThresholdModel = new PaymentThresholdModel(RealmController.getInstance().getData(PaymentThresholdModel.class));
        tv_maximum.setText(String.format(
                TCUtils.getString(R.string.payment_threshold_dialog_will_not_be_asked_when_make_transaction_below),
                paymentThresholdModel.getMaximum()));
    }

    @Override
    protected void onViewClick() {
        iv_close.setOnClickListener(v -> onCancel());
        ll_bt_cancel.setOnClickListener(v -> onCancel());
        ll_bt_ok.setOnClickListener(v -> onConfirm());
    }

    private void onCancel() {
        dismiss();
        listener.onPaymentThresholdCancel();
    }

    private void onConfirm() {
        dismiss();
        listener.onPaymentThresholdConfirm();
    }
}
