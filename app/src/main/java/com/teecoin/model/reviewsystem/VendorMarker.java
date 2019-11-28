package com.teecoin.model.reviewsystem;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class VendorMarker implements Serializable {

    private String markerId;
    private LatLng latLng;

    public VendorMarker(String markerId, LatLng latLng) {
        this.markerId = markerId;
        this.latLng = latLng;
    }

    public String getMarkerId() {
        return markerId;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
