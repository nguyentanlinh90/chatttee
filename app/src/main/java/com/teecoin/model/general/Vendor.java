package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class Vendor extends RealmObject implements Serializable {
    public static final String ID = "id";
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("bank_name")
    @Expose
    private String bank_name;

    @SerializedName("bank_account_number")
    @Expose
    private String bank_account_number;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("coinback_percentage")
    @Expose
    private String coinback_percentage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getAddress() {
        return address;
    }

    public String getCoinback_percentage() {
        return coinback_percentage;
    }

    public void update(Vendor vendor){
        this.id =vendor.getId();
        this.name =vendor.getName();
        this.email =vendor.getEmail();
        this.phone =vendor.getPhone();
        this.logo =vendor.getLogo();
        this.website =vendor.getWebsite();
        this.bank_name =vendor.getBank_name();
        this.bank_account_number =vendor.getBank_account_number();
        this.address = vendor.getAddress();
        this.coinback_percentage = vendor.getCoinback_percentage();

    }
}
