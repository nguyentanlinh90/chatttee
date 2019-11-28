package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleReviewModel {
    @SerializedName("author_name")
    @Expose
    private String author_name;
    @SerializedName("author_url")
    @Expose
    private String author_url;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("profile_photo_url")
    @Expose
    private String profile_photo_url;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("relative_time_description")
    @Expose
    private String relative_time_description;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time")
    @Expose
    private String time;

    public String getAuthor_name() {
        return author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public String getLanguage() {
        return language;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public String getRating() {
        return rating;
    }

    public String getRelative_time_description() {
        return relative_time_description;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

}
