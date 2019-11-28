package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentImageModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
