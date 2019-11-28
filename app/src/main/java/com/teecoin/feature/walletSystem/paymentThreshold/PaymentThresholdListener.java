package com.teecoin.feature.walletSystem.paymentThreshold;

public interface PaymentThresholdListener {
    void onPaymentThresholdConfirm();

    void onPaymentThresholdCancel();
}
