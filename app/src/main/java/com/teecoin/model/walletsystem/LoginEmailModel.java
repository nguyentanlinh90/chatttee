package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.TCUtils;

public class LoginEmailModel {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("device_id")
    @Expose
    private String device_id;

    @SerializedName("languge")
    @Expose
    private String languge;

    public LoginEmailModel(String email, String password, String device_id) {
        this.email = email;
        this.password = password;
        this.device_id = device_id;
        this.languge = TCUtils.getLanguageCode();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}

