package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorCategoryModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("vendors")
    @Expose
    private ArrayList<VendorModel> vendors;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public ArrayList<VendorModel> getVendors() {
        return vendors;
    }
}
