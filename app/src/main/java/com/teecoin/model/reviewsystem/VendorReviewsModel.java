package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorReviewsModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("featured_image")
    @Expose
    private String featured_image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("place_id")
    @Expose
    private String place_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
