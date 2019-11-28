package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import java.io.Serializable;

public class VerifyOTPModel extends TeeCoinModel implements Serializable {

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("verify_code")
    @Expose
    private String verify_code;

    public VerifyOTPModel(String country_code, String phone, String verify_code) {
        this.country_code = country_code;
        this.phone = phone;
        this.verify_code = verify_code;
    }
}
