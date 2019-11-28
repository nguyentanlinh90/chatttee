package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinBackPercentageModel {
    @SerializedName("coinback_percentage")
    @Expose
    private String coinback_percentage;

    public CoinBackPercentageModel(String coinback_percentage) {
        this.coinback_percentage = coinback_percentage;
    }
}
