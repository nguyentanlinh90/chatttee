package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenStatusModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("open_time")
    @Expose
    private String timeOpen;

    @SerializedName("close_time")
    @Expose
    private String timeClose;

    public String getStatus() {
        return status;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }

}
