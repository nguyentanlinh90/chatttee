package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PromotionModel {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("images")
    @Expose
    private ArrayList<ImageModel> images = null;

    @SerializedName("detail_url")
    @Expose
    private String detail_url;

    public ArrayList<ImageModel> getImages() {
        return images;
    }


    public String getTitle() {
        return title;
    }

    public String getDetail_url() {
        return detail_url;
    }
}
