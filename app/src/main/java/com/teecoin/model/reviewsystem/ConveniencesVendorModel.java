package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConveniencesVendorModel implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon_name")
    @Expose
    private String icon_name;

    public String getName() {
        return name;
    }

    public String getIcon_name() {
        return icon_name;
    }
}
