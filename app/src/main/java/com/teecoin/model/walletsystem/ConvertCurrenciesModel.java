package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConvertCurrenciesModel {

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("converted_amount")
    @Expose
    private String convertedAmount;

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public String getTo() {
        return to;
    }
}