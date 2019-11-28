package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReferralHistoryItemModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("status")
    @Expose
    private String status;

    public ReferralHistoryItemModel() {
    }

    public ReferralHistoryItemModel(String name, String createdDate, String amount) {
        this.name = name;
        this.created = createdDate;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }


}
