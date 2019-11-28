package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseReviewResponseModel implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("like_count")
    @Expose
    private String like_count;
    @SerializedName("tip_amount")
    @Expose
    private String tip_amount;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getTip_amount() {
        return tip_amount;
    }

    public void setTip_amount(String tip_amount) {
        this.tip_amount = tip_amount;
    }

    public ArrayList<ImagesResponseModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesResponseModel> images) {
        this.images = images;
    }

    public AuthorReviewModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorReviewModel author) {
        this.author = author;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public String getTip_of_user() {
        return tip_of_user;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public void setTip_of_user(String tip_of_user) {
        this.tip_of_user = tip_of_user;
    }

    public String getTitle() {
        return title;
    }
}
