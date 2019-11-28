package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordByPublicKeyRequestModel {

    @SerializedName("public_key")
    @Expose
    private String public_key;

    public ResetPasswordByPublicKeyRequestModel(String public_key) {
        this.public_key = public_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
