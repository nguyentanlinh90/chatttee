package com.teecoin.model.general;

import java.io.Serializable;

import io.realm.RealmObject;

public class PaymentThresholdModel extends RealmObject implements Serializable {
    public static final boolean DEFAULT_PAYMENT_THRESHOLD = false;
    public static final int DEFAULT_MAXIMUM = 100;
    public static final String PRIMARY_KEY = "publicKey";

    private String publicKey;
    private boolean payment_threshold;
    private double maximum;

    public PaymentThresholdModel() {
    }

    public PaymentThresholdModel(String publicKey, boolean payment_threshold, double maximum) {
        this.publicKey = publicKey;
        this.payment_threshold = payment_threshold;
        this.maximum = maximum;
    }

    public PaymentThresholdModel(PaymentThresholdModel paymentThreshold) {
        this.publicKey = paymentThreshold.publicKey;
        this.payment_threshold = paymentThreshold.payment_threshold;
        this.maximum = paymentThreshold.maximum;
    }


    public void update(PaymentThresholdModel paymentThreshold) {
        this.publicKey = paymentThreshold.publicKey;
        this.payment_threshold = paymentThreshold.payment_threshold;
        this.maximum = paymentThreshold.maximum;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isPayment_threshold() {
        return payment_threshold;
    }

    public void setPayment_threshold(boolean payment_threshold) {
        this.payment_threshold = payment_threshold;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }
}
