package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BalanceModel implements Serializable {
    @SerializedName("tec_balance")
    @Expose
    private String tec_balance;
    @SerializedName("fiat_amount")
    @Expose
    private String fiat_amount;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    public String getTec_balance() {
        return tec_balance;
    }

    public String getFiat_amount() {
        return fiat_amount;
    }

    public String getCurrency_code() {
        return currency_code;
    }
}

