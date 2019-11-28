package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureExperienceModel implements Serializable {

    @SerializedName("link_post")
    @Expose
    private String link_post;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("excerpt")
    @Expose
    private String excerpt;

    @SerializedName("feature_image")
    @Expose
    private String feature_image;

    public String getLink_post() {
        return link_post;
    }

    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getFeature_image() {
        return feature_image;
    }
}
