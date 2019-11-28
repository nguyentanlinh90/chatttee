package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImagesResponseModel implements Serializable {
    public static String TAG_VIDEO = "ImagesResponseModel";
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnail_100")
    @Expose
    private String thumbnail_100;

    @SerializedName("thumbnail_300")
    @Expose
    private String thumbnail_300;

    @SerializedName("video_url")
    @Expose
    private String video_url;

    public ImagesResponseModel() {
    }

    public ImagesResponseModel(String url, String video_url) {
        this.url = url;

        this.video_url = video_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_100() {
        return thumbnail_100;
    }

    public void setThumbnail_100(String thumbnail_100) {
        this.thumbnail_100 = thumbnail_100;
    }

    public String getThumbnail_300() {
        return thumbnail_300;
    }

    public void setThumbnail_300(String thumbnail_300) {
        this.thumbnail_300 = thumbnail_300;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
