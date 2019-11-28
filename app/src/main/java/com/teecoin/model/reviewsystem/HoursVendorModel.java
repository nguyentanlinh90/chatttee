package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HoursVendorModel {
    @SerializedName("open_time")
    @Expose
    private String open_time;

    @SerializedName("close_time")
    @Expose
    private String close_time;

    public String getOpen_time() {
        return open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    @Override
    public String toString() {
        return "HoursVendorModel{" +
                "open_time='" + open_time + '\'' +
                ", close_time='" + close_time + '\'' +
                '}';
    }
}
