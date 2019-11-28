package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorGetWalletResponseModel {
    @SerializedName("public_key")
    @Expose
    private String public_key;
    @SerializedName("logo")
    @Expose
    private String logo;
    public String getPublic_key() {
        return public_key;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public String toString() {
        return "VendorGetWalletResponseModel{" +
                "public_key='" + public_key + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
