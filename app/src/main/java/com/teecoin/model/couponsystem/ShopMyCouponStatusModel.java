package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopMyCouponStatusModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("used")
    @Expose
    private int used;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("got")
    @Expose
    private int got;
    @SerializedName("sold")
    @Expose
    private int sold;

    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getUsed() {
        return used;
    }

    public int getTotal() {
        return total;
    }

    public int getStatus() {
        return status;
    }

    public int getGot() {
        return got;
    }

    public int getSold() {
        return sold;
    }
}
