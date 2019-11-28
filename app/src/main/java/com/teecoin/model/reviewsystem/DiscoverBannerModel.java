package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import java.io.Serializable;

public class DiscoverBannerModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("banner")
    @Expose
    private String banner;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("url_type")
    @Expose
    private String url_type;


    // using for api click
    @SerializedName("banner_id")
    @Expose
    private String banner_id;

    @SerializedName("can_get_discover_reward")
    @Expose
    private boolean can_get_discover_reward;

    public DiscoverBannerModel(String id) {
        this.id = id;
    }

    //----------
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getBanner() {
        return banner;
    }

    public String getAmount() {
        return amount;
    }

    public String getUrl_type() {
        return url_type;
    }


    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public boolean isCan_get_discover_reward() {
        return can_get_discover_reward;
    }
}
