package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserCouponModel extends UserBaseCouponModel implements Serializable {

    @SerializedName("used_times")
    @Expose
    private String usedTimes;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("hashtags")
    @Expose
    private ArrayList<String> hashTags;
    @SerializedName("serial")
    @Expose
    private String serial;

    public UserCouponModel() {
    }

    public String getUsedTimes() {
        return usedTimes;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public ArrayList<String> getHashTags() {
        return hashTags;
    }

    public String getUrl() {
        return url;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSerial() {
        return serial;
    }

}
