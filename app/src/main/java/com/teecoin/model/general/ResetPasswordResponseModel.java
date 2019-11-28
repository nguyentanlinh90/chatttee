package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class ResetPasswordResponseModel extends TeeCoinModel{
    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }
}
