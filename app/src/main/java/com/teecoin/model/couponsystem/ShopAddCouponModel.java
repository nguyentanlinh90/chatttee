package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import okhttp3.MultipartBody;

public class ShopAddCouponModel implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("discount_type")
    @Expose
    private String discount_type;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("hashtags")
    @Expose
    private String hashtags;
    @SerializedName("tz")
    @Expose
    private String tz;
    @SerializedName("banner")
    @Expose
    private MultipartBody.Part banner;
    @SerializedName("cash")
    @Expose
    private String cash;

    private String bannerPath;

    public ShopAddCouponModel(String name, String discount_type, String percentage, String start, String end, String description, String term, String hashtags, String tz, MultipartBody.Part banner, String cash) {
        this.name = name;
        this.discount_type = discount_type;
        this.percentage = percentage;
        this.start = start;
        this.end = end;
        this.description = description;
        this.term = term;
        this.hashtags = hashtags;
        this.tz = tz;
        this.banner = banner;
        this.cash = cash;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public String getName() {
        return name;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public String getTerm() {
        return term;
    }

    public String getHashtags() {
        return hashtags;
    }

    public String getTz() {
        return tz;
    }

    public MultipartBody.Part getBanner() {
        return banner;
    }

    public String getCash() {
        return cash;
    }
}
