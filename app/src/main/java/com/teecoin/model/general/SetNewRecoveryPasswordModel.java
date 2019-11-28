package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class SetNewRecoveryPasswordModel extends TeeCoinModel {

    @SerializedName("verify_code")
    @Expose
    private String verify_code;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirm_password")
    @Expose
    private String confirm_password;

    public SetNewRecoveryPasswordModel(String verify_code, String password, String confirm_password) {
        this.verify_code = verify_code;
        this.password = password;
        this.confirm_password = confirm_password;
    }
}
