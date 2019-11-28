package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordByEmailRequestModel {

    @SerializedName("email")
    @Expose
    private String email;

    public ResetPasswordByEmailRequestModel(String public_key) {
        this.email = public_key;
    }

    public String getPublic_key() {
        return email;
    }

    public void setPublic_key(String public_key) {
        this.email = public_key;
    }
}
