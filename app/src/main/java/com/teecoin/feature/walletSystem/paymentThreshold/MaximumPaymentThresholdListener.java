package com.teecoin.feature.walletSystem.paymentThreshold;

public interface MaximumPaymentThresholdListener {
    void onMaximumThresholdFinish(String number);

    void onMaximumThresholdCancel();
}
