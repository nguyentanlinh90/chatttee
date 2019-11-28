package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CryptoModel implements Serializable {
    @SerializedName("crypto_name")
    @Expose
    private String crypto_name;

    @SerializedName("crypto_type")
    @Expose
    private String crypto_type;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("price_in_tec")
    @Expose
    private String price_in_tec;

    @SerializedName("remain_time")
    @Expose
    private long remain_time;

    @SerializedName("tec_topup_threshold")
    @Expose
    private String tec_topup_threshold;

    public String getCrypto_name() {
        return crypto_name;
    }

    public String getCrypto_type() {
        return crypto_type;
    }

    public String getIcon() {
        return icon;
    }

    public String getPrice_in_tec() {
        return price_in_tec;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getRemain_time() {
        return remain_time;
    }

    public void setRemain_time(long remain_time) {
        this.remain_time = remain_time;
    }

    public String getTec_topup_threshold() {
        return tec_topup_threshold;
    }
}
