package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import java.io.Serializable;

public class SendOTPModel extends TeeCoinModel implements Serializable {

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("device_id")
    @Expose
    private String device_id;

    @SerializedName("recaptcha_token")
    @Expose
    private String recaptchaToken;

    public SendOTPModel(String country_code, String phone, String device_id, String recaptchaToken) {
        this.country_code = country_code;
        this.phone = phone;
        this.device_id = device_id;
        this.recaptchaToken = recaptchaToken;
    }
}
