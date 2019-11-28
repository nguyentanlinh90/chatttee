package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterNotifyModel {
    @SerializedName("token")
    @Expose
    private String token;

    public RegisterNotifyModel() {
    }

    public RegisterNotifyModel(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
