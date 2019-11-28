package com.teecoin.feature.reviewSystem.userReviewShop;

public interface CropVideoListener {

    void onCropFinish(String cropVideoPath);

    void onCropFail(String message);

}
