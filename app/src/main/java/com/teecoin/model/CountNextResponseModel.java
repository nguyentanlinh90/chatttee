package com.teecoin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountNextResponseModel {
    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName(value = "review_count", alternate = "reviews_count")
    @Expose
    private String review_count;

    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("can_get_giveaway_today")
    @Expose
    private boolean can_get_giveaway_today;

    @SerializedName("current_date")
    @Expose
    private int current_date;


    public int getCount() {
        return count;
    }

    public String getReview_count() {
        return review_count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public boolean isCan_get_giveaway_today() {
        return can_get_giveaway_today;
    }

    public int getCurrent_date() {
        return current_date;
    }
}
