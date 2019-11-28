package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CouponCoinBackRedemptionResponse {
    @SerializedName("redemption_count")
    @Expose
    private String redemption_count;
    @SerializedName("coin_back_amount")
    @Expose
    private String coin_back_amount;
    @SerializedName("coin_back_setting")
    @Expose
    private ArrayList<CoinBackModel> coin_back_setting;

    public String getRedemption_count() {
        return redemption_count;
    }

    public String getCoin_back_amount() {
        return coin_back_amount;
    }

    public ArrayList<CoinBackModel> getCoin_back_setting() {
        return coin_back_setting;
    }
}
