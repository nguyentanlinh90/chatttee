package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class ChangeRecoveryPasswordModel extends TeeCoinModel {

    @SerializedName("old_password")
    @Expose
    private String old_password;

    @SerializedName("new_password")
    @Expose
    private String new_password;

    @SerializedName("confirm_password")
    @Expose
    private String confirm_password;


    public ChangeRecoveryPasswordModel(String old_password, String new_password, String confirm_password) {
        this.old_password = old_password;
        this.new_password = new_password;
        this.confirm_password = confirm_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }
}
