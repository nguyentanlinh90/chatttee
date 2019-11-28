package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.reviewsystem.VendorModel;

import java.io.Serializable;

public class CheckinNotificationResultModel implements Serializable {
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("shop_id")
    @Expose
    private String shop_id;
    @SerializedName("coupons")
    @Expose
    private CouponsCheckinNotificationModel coupons;
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;

    public CheckinNotificationResultModel() {
    }

    public CheckinNotificationResultModel(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCreated() {
        return created;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getDestination() {
        return destination;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public CouponsCheckinNotificationModel getCoupons() {
        return coupons;
    }

    public VendorModel getVendor() {
        return vendor;
    }
}
