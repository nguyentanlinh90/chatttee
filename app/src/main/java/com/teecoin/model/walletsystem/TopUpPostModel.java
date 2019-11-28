package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUpPostModel {

    @SerializedName("crypto_type")
    @Expose
    private String crypto_type;

    public TopUpPostModel(String crypto_type) {
        this.crypto_type = crypto_type;
    }
}
