package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OpenStatusModel implements Serializable {
    @SerializedName("time_open")
    @Expose
    private String time_open;
    @SerializedName("time_close")
    @Expose
    private String time_close;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTime_open() {
        return time_open;
    }

    public String getTime_close() {
        return time_close;
    }

    public String getStatus() {
        return status;
    }
}
