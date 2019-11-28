package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class CheckSecretResponseModel extends TeeCoinModel {

    @SerializedName("is_registered")
    @Expose
    private boolean is_registered;

    public boolean isIs_registered() {
        return is_registered;
    }
}
