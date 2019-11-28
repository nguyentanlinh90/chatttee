package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighlightModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("featured_image")
    @Expose
    private String featured_image;
    @SerializedName("reviews_count")
    @Expose
    private String reviews_count;

    public String getFeatured_image() {
        return featured_image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getReviews_count() {
        return reviews_count;
    }
}
