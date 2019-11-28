package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopCouponModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("discount_type")
    @Expose
    private String discount_type;
    @SerializedName("catalogues")
    @Expose
    private ArrayList<ShopMyCouponStatusModel> catalogues = null;
    @SerializedName("notifications")
    @Expose
    private ArrayList<ShopMyCouponStatusModel> notifications = null;

    private String quantity;
    private String saleStart;
    private String saleEnd;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public ArrayList<ShopMyCouponStatusModel> getCatalogues() {
        return catalogues;
    }

    public ArrayList<ShopMyCouponStatusModel> getNotifications() {
        return notifications;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSaleStart() {
        return saleStart;
    }

    public void setSaleStart(String saleStart) {
        this.saleStart = saleStart;
    }

    public String getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(String saleEnd) {
        this.saleEnd = saleEnd;
    }
}
