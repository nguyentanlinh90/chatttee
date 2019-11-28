package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImagesVendor implements Serializable {
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnail_500")
    @Expose
    private String thumbnail_500;

    @SerializedName("review")
    @Expose
    private ReviewVendor review;


    public String getUrl() {
        return url;
    }

    public String getThumbnail_500() {
        return thumbnail_500;
    }

    public ReviewVendor getReview() {
        return review;
    }
}
