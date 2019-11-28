package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListReviewsModel extends BaseReviewResponseModel {
    @SerializedName("vendor")
    @Expose
    private VendorReviewsModel vendor;

    public VendorReviewsModel getVendor() {
        return vendor;
    }
}
