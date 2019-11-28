package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorReviewModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("public_key")
    @Expose
    private String public_key;

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public String getFull_name() {
        return full_name;
    }
}
