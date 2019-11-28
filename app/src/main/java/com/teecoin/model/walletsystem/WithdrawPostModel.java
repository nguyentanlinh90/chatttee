package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawPostModel implements Serializable {

    @SerializedName("cash_amount")
    @Expose
    private String cash_amount;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    public WithdrawPostModel(String cash_amount, String currency_code) {
        this.cash_amount = cash_amount;
        this.currency_code = currency_code;
    }

    public String getCash_amount() {
        return cash_amount;
    }

}