package com.teecoin.feature.general.locationtracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentLocationModel implements Serializable {

    @SerializedName("lat")
    @Expose
    private double latitude;

    @SerializedName("long")
    @Expose
    private double longitude;

    public CurrentLocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
