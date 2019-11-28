package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class CreateAccountModel extends TeeCoinModel {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public CreateAccountModel() {
    }

    public String getToken() {
        return token;
    }

    public String getUuid() {
        return uuid;
    }

}
