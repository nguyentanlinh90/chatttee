package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumbnail_100")
    @Expose
    private String thumbnail100;
    @SerializedName("thumbnail_300")
    @Expose
    private String thumbnail300;

    public String getUrl() {
        return url;
    }

    public String getThumbnail100() {
        return thumbnail100;
    }

    public String getThumbnail300() {
        return thumbnail300;
    }
}