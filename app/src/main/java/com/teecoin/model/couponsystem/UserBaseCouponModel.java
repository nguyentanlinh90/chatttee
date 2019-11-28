package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.reviewsystem.VendorModel;

public class UserBaseCouponModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("usage_times")
    @Expose
    private String usage_times;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("coupon_id")
    @Expose
    private String coupon_id;
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getUsage_times() {
        return usage_times;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public VendorModel getVendor() {
        return vendor;
    }
}
