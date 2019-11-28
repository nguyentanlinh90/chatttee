package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.reviewsystem.VendorModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CouponCataloguePurchaseResponseModel implements Serializable {
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;

    @SerializedName("coupon")
    @Expose
    private UserCouponModel coupon;

    @SerializedName("redemption_count")
    @Expose
    private String redemption_count;

    @SerializedName("coin_back_setting")
    @Expose
    private ArrayList<CoinBackModel> coin_back_setting;

    public VendorModel getVendor() {
        return vendor;
    }

    public UserCouponModel getCoupon() {
        return coupon;
    }

    public String getRedemption_count() {
        return redemption_count;
    }

    public ArrayList<CoinBackModel> getCoin_back_setting() {
        return coin_back_setting;
    }


    private boolean isSuccess;

    private String errorMessage;

    public CouponCataloguePurchaseResponseModel(boolean isSuccess, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
