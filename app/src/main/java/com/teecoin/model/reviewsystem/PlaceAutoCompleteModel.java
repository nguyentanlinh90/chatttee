package com.teecoin.model.reviewsystem;

import com.google.android.gms.maps.model.LatLng;

public class PlaceAutoCompleteModel {
    public CharSequence placeId;
    public CharSequence description;
    public LatLng latLng;
    private int page;

    public PlaceAutoCompleteModel(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }
    public PlaceAutoCompleteModel(int page,LatLng latLng) {
        this.page = page;
        this.latLng = latLng;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return description.toString();
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
