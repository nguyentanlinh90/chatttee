package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagPricesModel {
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;

    public String getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
