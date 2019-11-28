package com.teecoin.model.reviewsystem;

public class ReviewVendorDetailModel extends BaseReviewResponseModel {
    // todo: if add some field
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private GoogleReviewModel googleReviewModel;

    public GoogleReviewModel getGoogleReviewModel() {
        return googleReviewModel;
    }

    public void setGoogleReviewModel(GoogleReviewModel googleReviewModel) {
        this.googleReviewModel = googleReviewModel;
    }

    public ReviewVendorDetailModel(GoogleReviewModel googleReviewModel) {
        this.googleReviewModel = googleReviewModel;
    }
}
