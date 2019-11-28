package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DailyRewardDayModel implements Serializable {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
