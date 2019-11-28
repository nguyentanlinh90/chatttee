package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class FeeConfigModel extends RealmObject {

    public static final String PRIMARY_KEY = "publicKey";
    public static final String FEE_RATE = "fee_rate";
    public static final String USER_FEE_AMOUNT = "user_fee_amount";
    public static final String USER_REVIEW_AMOUNT = "review_reward_amount";
    public static final String USER_REVIEW_AMOUNT_ATTACHMENT = "review_reward_amount_attachment";

    @SerializedName("public_key")
    @Expose
    private String publicKey;

    @SerializedName("fee_rate")
    @Expose
    private String feeRate;

    @SerializedName("user_fee_amount")
    @Expose
    private String userFeeAmount;

    @SerializedName("review_reward_amount")
    @Expose
    private String review_reward_amount;
    @SerializedName("review_reward_amount_attachment")
    @Expose
    private String review_reward_amount_attachment;

    @SerializedName("imageFloating")
    @Expose
    private String imageFloating;
    @SerializedName("urlFloating")
    @Expose
    private String urlFloating;
    @SerializedName("titleFloating")
    @Expose
    private String titleFloating;

    public FeeConfigModel() {
    }

    public FeeConfigModel(String feeRate, String userFeeAmount, String review_reward_amount, String review_reward_amount_attachment) {
        this.feeRate = feeRate;
        this.userFeeAmount = userFeeAmount;
        this.review_reward_amount = review_reward_amount;
        this.review_reward_amount_attachment = review_reward_amount_attachment;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getUserFeeAmount() {
        return userFeeAmount;
    }

    public void setUserFeeAmount(String userFeeAmount) {
        this.userFeeAmount = userFeeAmount;
    }

    public String getReview_reward_amount() {
        return review_reward_amount;
    }

    public void setReview_reward_amount(String review_reward_amount) {
        this.review_reward_amount = review_reward_amount;
    }

    public String getReview_reward_amount_attachment() {
        return review_reward_amount_attachment;
    }

    public void setReview_reward_amount_attachment(String review_reward_amount_attachment) {
        this.review_reward_amount_attachment = review_reward_amount_attachment;
    }

    public String getImageFloating() {
        return imageFloating;
    }

    public void setImageFloating(String imageFloating) {
        this.imageFloating = imageFloating;
    }

    public String getUrlFloating() {
        return urlFloating;
    }

    public void setUrlFloating(String urlFloating) {
        this.urlFloating = urlFloating;
    }

    public String getTitleFloating() {
        return titleFloating;
    }

    public void setTitleFloating(String titleFloating) {
        this.titleFloating = titleFloating;
    }
}
