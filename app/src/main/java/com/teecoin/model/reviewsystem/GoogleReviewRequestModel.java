package com.teecoin.model.reviewsystem;

public class GoogleReviewRequestModel {
    private String placeid;
    private String key;

    public GoogleReviewRequestModel(String placeid, String key) {
        this.placeid = placeid;
        this.key = key;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
