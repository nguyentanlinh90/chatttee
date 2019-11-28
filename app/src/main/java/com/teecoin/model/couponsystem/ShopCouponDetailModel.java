package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopCouponDetailModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("hashtags")
    @Expose
    private ArrayList<String> hashtags = null;
    @SerializedName("discount_type")
    @Expose
    private String discount_type;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTerm() {
        return term;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getBanner() {
        return banner;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public String getDiscount_type() {
        return discount_type;
    }
}