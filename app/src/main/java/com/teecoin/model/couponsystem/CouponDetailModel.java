package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponDetailModel extends UserCouponModel implements Serializable {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("fee_amount_destination")
    @Expose
    private String fee_amount_destination;
    @SerializedName("basic_fee_amount_destination")
    @Expose
    private String basic_fee_amount_destination;

    @SerializedName("term")
    @Expose
    private String term;

    @SerializedName("catalogue_start")
    @Expose
    private String catalogue_start;

    @SerializedName("catalogue_end")
    @Expose
    private String catalogue_end;

    @SerializedName("is_own")
    @Expose
    private boolean is_own;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("can_purchase_multiple")
    @Expose
    private boolean can_purchase_multiple;

    @SerializedName("purchase_times")
    @Expose
    private String purchase_times;

    @SerializedName("redeem_code")
    @Expose
    private String redeem_code;

    @SerializedName("redeem_time")
    @Expose
    private String redeem_time;

    @SerializedName("is_hot_coupon")
    @Expose
    private boolean is_hot_coupon;

    @SerializedName("guide")
    @Expose
    private String guide;

    @SerializedName("usd_price")
    @Expose
    private String usd_price;

    private double calculateToTecIncludeFeeAmount = 0;
    private double calculateToTecNoFeeAmount = 0;
    private double feeAmount = 0;

    public String getDescription() {
        return description;
    }

    public String getTerm() {
        return term;
    }

    public String getCatalogue_start() {
        return catalogue_start;
    }

    public String getCatalogue_end() {
        return catalogue_end;
    }

    public boolean isIs_own() {
        return is_own;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public String getTypeToRequestAPIRedeem() {
        return getType().equals("1") ? "notifications" : "catalogues";
    }

    public boolean isCan_purchase_multiple() {
        return can_purchase_multiple;
    }

    public String getPurchase_times() {
        return purchase_times;
    }

    public void setPurchase_times(String purchase_times) {
        this.purchase_times = purchase_times;
    }

    public String getRedeem_code() {
        return redeem_code;
    }

    public String getRedeem_time() {
        return redeem_time;
    }

    public boolean isIs_hot_coupon() {
        return is_hot_coupon;
    }

    public String getGuide() {
        return guide;
    }

    public double getCalculateToTecNoFeeAmount() {
        return calculateToTecNoFeeAmount;
    }

    public void setCalculateToTecNoFeeAmount(double calculateToTecNoFeeAmount) {
        this.calculateToTecNoFeeAmount = calculateToTecNoFeeAmount;
    }

    public double getCalculateToTecIncludeFeeAmount() {
        return calculateToTecIncludeFeeAmount;
    }

    public void setCalculateToTecIncludeFeeAmount(double calculateToTecIncludeFeeAmount) {
        this.calculateToTecIncludeFeeAmount = calculateToTecIncludeFeeAmount;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getUsd_price() {
        return usd_price;
    }

    public String getFee_amount_destination() {
        return fee_amount_destination;
    }

    public String getBasic_fee_amount_destination() {
        return basic_fee_amount_destination;
    }

}
