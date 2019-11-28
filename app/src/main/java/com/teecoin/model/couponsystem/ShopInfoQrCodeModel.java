package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoQrCodeModel {
    @SerializedName("vendor_code")
    @Expose
    private String vendor_code;
    @SerializedName("qr_url")
    @Expose
    private String qr_url;

    public String getVendor_code() {
        return vendor_code;
    }

    public String getQr_url() {
        return qr_url;
    }
}
