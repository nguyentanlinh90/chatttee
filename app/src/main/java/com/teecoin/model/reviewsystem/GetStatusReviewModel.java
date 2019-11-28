package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetStatusReviewModel implements Serializable {// check reward when user review vendor
    @SerializedName("can_get_review_reward")
    @Expose
    private boolean can_get_review_reward;

    public boolean isCan_get_review_reward() {
        return can_get_review_reward;
    }
}
