package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckVerificationCodeModel {

    @SerializedName("verify_code")
    @Expose
    private String verificationCode;

    public CheckVerificationCodeModel(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
