package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OpenHoursVendorModel {
    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("hours")
    @Expose
    private ArrayList<HoursVendorModel> hours;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<HoursVendorModel> getHours() {
        return hours;
    }

    public void setHours(ArrayList<HoursVendorModel> hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "OpenHoursVendorModel{" +
                "day='" + day + '\'' +
                ", hours=" + hours +
                '}';
    }
}
