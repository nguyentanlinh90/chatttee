package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import java.io.Serializable;

public class ReferralEventModel extends TeeCoinModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("referral_amount")
    @Expose
    private String referral_amount;

    @SerializedName("register_amount")
    @Expose
    private String register_amount;

    @SerializedName("release_date")
    @Expose
    private String release_date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image")
    @Expose
    private ReferralImage referralImage;

    private String referralCode;

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getReferral_amount() {
        return referral_amount;
    }

    public String getRegister_amount() {
        return register_amount;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getId() {
        return id;
    }

    public ReferralImage getReferralImage() {
        return referralImage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public class ReferralImage implements Serializable {
        @SerializedName("height")
        @Expose
        private int height;

        @SerializedName("width")
        @Expose
        private int width;

        @SerializedName("url")
        @Expose
        private String url;

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getUrl() {
            return url;
        }
    }
}
