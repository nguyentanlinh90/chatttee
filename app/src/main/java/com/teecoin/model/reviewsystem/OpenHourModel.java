package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenHourModel {

    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("weekday")
    @Expose
    private int weekday;
    @SerializedName("open_time")
    @Expose
    private String openTime;

    public String getCloseTime() {
        return closeTime;
    }

    public int getWeekday() {
        return weekday;
    }

    public String getOpenTime() {
        return openTime;
    }

}
