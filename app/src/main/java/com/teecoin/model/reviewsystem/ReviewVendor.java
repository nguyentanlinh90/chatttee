package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewVendor implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;
    @SerializedName("author")
    @Expose
    private AuthorReviewModel author;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("like_count")
    @Expose
    private String like_count;
    @SerializedName("tip_amount")
    @Expose
    private String tip_amount;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("is_like")
    @Expose
    private  boolean is_like;
    @SerializedName("tip_of_user")
    @Expose
    private  String tip_of_user;
    @SerializedName("verified")
    @Expose
    private  boolean verified;
    @SerializedName("title")
    @Expose
    private  String title;

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public AuthorReviewModel getAuthor() {
        return author;
    }

    public float getRating() {
        return rating;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getTip_amount() {
        return tip_amount;
    }

    public String getComment() {
        return comment;
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

    public String getTitle() {
        return title;
    }

    public void setTip_of_user(String tip_of_user) {
        this.tip_of_user = tip_of_user;
    }

    public void setTip_amount(String tip_amount) {
        this.tip_amount = tip_amount;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }
}
