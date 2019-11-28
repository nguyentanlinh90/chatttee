package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ShareVendorModel implements Serializable {
    @SerializedName("url_share")
    @Expose
    private String urlShare;

    @SerializedName("content_share")
    @Expose
    private String contentShare;


    public ShareVendorModel() {
    }

    public String getUrlShare() {
        return urlShare;
    }

    public String getContentShare() {
        return contentShare;
    }
}
