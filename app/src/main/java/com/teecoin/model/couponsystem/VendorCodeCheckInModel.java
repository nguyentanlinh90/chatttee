package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorCodeCheckInModel implements Serializable {
    private boolean fromHomeCheckIn;
    @SerializedName("vendor_code")
    @Expose
    private String vendor_code;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String lng;

    public VendorCodeCheckInModel() {

    }

    public VendorCodeCheckInModel(String vendor_code, String lat, String lng) {
        this.vendor_code = vendor_code;
        this.lat = lat;
        this.lng = lng;
    }

    public VendorCodeCheckInModel(String vendor_code) {
        this.vendor_code = vendor_code;
    }

    public String getVendor_code() {
        return vendor_code;
    }

    public boolean isFromHomeCheckIn() {
        return fromHomeCheckIn;
    }

    public void setFromHomeCheckIn(boolean fromHomeCheckIn) {
        this.fromHomeCheckIn = fromHomeCheckIn;
    }
}
