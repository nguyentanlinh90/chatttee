package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentDetailModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("like_count")
    @Expose
    private String like_count;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("tip_amount")
    @Expose
    private String tip_amount;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;
    @SerializedName("images")
    @Expose
    private ArrayList<ImagesResponseModel> images;
    @SerializedName("author")
    @Expose
    private AuthorReviewModel author;
    @SerializedName("is_like")
    @Expose
    private boolean is_like;
    @SerializedName("tip_of_user")
    @Expose
    private String tip_of_user;
    @SerializedName("verified")
    @Expose
    private boolean verified;

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getTip_amount() {
        return tip_amount;
    }

    public float getRating() {
        return rating;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public ArrayList<ImagesResponseModel> getImages() {
        return images;
    }

    public AuthorReviewModel getAuthor() {
        return author;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public String getTip_of_user() {
        return tip_of_user;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getLike_count() {
        return like_count;
    }
}
