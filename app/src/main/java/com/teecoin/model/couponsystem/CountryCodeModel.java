package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountryCodeModel implements Serializable {
    public static final String DEFAULT_COUNTRY_CODE = "SG";
    public static final String DEFAULT_COUNTRY_NAME = "Singapore";
    public static final boolean DEFAULT_SELECTED = true;
    public static final String DEFAULT_CURRENCY_CODE = "SGD";
    @SerializedName("country_code")
    @Expose
    private String country_code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;


    private boolean isSelected;

    public CountryCodeModel(String country_code, String name, String currency_code, boolean isSelected) {
        this.country_code = country_code;
        this.name = name;
        this.currency_code = currency_code;
        this.isSelected = isSelected;
    }

    public CountryCodeModel() {

    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getCurrency_code() {
        return currency_code;
    }

}

