package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FiatWalletModel implements Serializable {
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("cash_amount")
    @Expose
    private String cash_amount;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("icon")
    @Expose
    private String icon;

    public String getCreated() {
        return created;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }
}
