package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserCouponCatalogueDataModel extends UserBaseCouponModel implements Serializable {

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("usd_price")
    @Expose
    private String usd_price;

    @SerializedName("purchase_times")
    @Expose
    private String purchase_times;

    @SerializedName("catalogue_start")
    @Expose
    private String catalogue_start;

    @SerializedName("catalogue_end")
    @Expose
    private String catalogue_end;

    @SerializedName("can_purchase_multiple")
    @Expose
    private boolean can_purchase_multiple;

    @SerializedName("is_hot_coupon")
    @Expose
    private boolean is_hot_coupon;

    @SerializedName("is_new_arrival")
    @Expose
    private boolean is_new_arrival;


    public String getPrice() {
        return price;
    }

    public String getPurchase_times() {
        return purchase_times;
    }

    public String getCatalogue_start() {
        return catalogue_start;
    }

    public String getCatalogue_end() {
        return catalogue_end;
    }

    public boolean isCan_purchase_multiple() {
        return can_purchase_multiple;
    }

    public boolean isIs_hot_coupon() {
        return is_hot_coupon;
    }

    public boolean isIs_new_arrival() {
        return is_new_arrival;
    }

    public String getUsd_price() {
        return usd_price;
    }
}
