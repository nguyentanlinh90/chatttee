package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConvertToTecPostModel implements Serializable {

    @SerializedName("convert_cash_amount")
    @Expose
    private String convert_cash_amount;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    public String getConvert_cash_amount() {
        return convert_cash_amount;
    }

    public void setConvert_cash_amount(String convert_cash_amount) {
        this.convert_cash_amount = convert_cash_amount;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }
}