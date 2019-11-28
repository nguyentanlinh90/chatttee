package com.teecoin.feature.reviewSystem.inactiveUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InactiveUserModel {
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("purchase_name")
    @Expose
    private String purchase_name;

    @SerializedName("valid_date")
    @Expose
    private String valid_date;


    @SerializedName("user_number_paid")
    @Expose
    private String user_number_paid;

    public InactiveUserModel(String date, String purchase_name, String valid_date, String user_number_paid) {
        this.date = date;
        this.purchase_name = purchase_name;
        this.valid_date = valid_date;
        this.user_number_paid = user_number_paid;
    }

    public String getDate() {
        return date;
    }

    public String getPurchase_name() {
        return purchase_name;
    }

    public String getValid_date() {
        return valid_date;
    }

    public String getUser_number_paid() {
        return user_number_paid;
    }
}
