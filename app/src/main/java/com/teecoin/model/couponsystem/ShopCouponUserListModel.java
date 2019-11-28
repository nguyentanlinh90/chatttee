package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCouponUserListModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count_used")
    @Expose
    private String count_used;
    @SerializedName("count_review")
    @Expose
    private String count_review;
    @SerializedName("count_check_in")
    @Expose
    private String count_check_in;

    public ShopCouponUserListModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount_used() {
        return count_used;
    }

    public void setCount_used(String count_used) {
        this.count_used = count_used;
    }

    public String getCount_review() {
        return count_review;
    }

    public void setCount_review(String count_review) {
        this.count_review = count_review;
    }

    public String getCount_check_in() {
        return count_check_in;
    }

    public void setCount_check_in(String count_check_in) {
        this.count_check_in = count_check_in;
    }
}
