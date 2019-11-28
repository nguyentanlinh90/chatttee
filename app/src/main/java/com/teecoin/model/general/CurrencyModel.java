package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class CurrencyModel extends TeeCoinModel {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("symbol")
    @Expose
    private String symbol;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
