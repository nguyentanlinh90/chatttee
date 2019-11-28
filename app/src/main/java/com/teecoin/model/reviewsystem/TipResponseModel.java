package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TipResponseModel implements Serializable {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("is_coinback_success")
    @Expose
    private Boolean isCoinbackSuccess;
    @SerializedName("is_review_success")
    @Expose
    private Boolean is_review_success;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("receiver")
    @Expose
    private String receiver;

    private String totalAmount;

    public TipResponseModel() {
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
