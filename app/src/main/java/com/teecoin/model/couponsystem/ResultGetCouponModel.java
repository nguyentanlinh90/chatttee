package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultGetCouponModel {
    @SerializedName("serial")
    @Expose
    private String serial;

    public String getSerial() {
        return serial;
    }

    @Override
    public String toString() {
        return "ResultGetCouponModel{" +
                "serial='" + serial + '\'' +
                '}';
    }
}
