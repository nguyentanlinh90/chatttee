package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoogleReviewResponseModel {
    @SerializedName("reviews")
    @Expose
    private ArrayList<GoogleReviewModel> reviews;

    public ArrayList<GoogleReviewModel> getReviews() {
        return reviews;
    }

}
