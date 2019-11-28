package com.teecoin.model.couponsystem;

import com.teecoin.model.reviewsystem.VendorModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupCategoryModel implements Serializable {
    private String name;
    private ArrayList<VendorModel> listVendor;

    public GroupCategoryModel() {
    }

    public GroupCategoryModel(String name, ArrayList<VendorModel> listVendor) {
        this.name = name;
        this.listVendor = listVendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VendorModel> getListVendor() {
        return listVendor;
    }

    public void setListVendor(ArrayList<VendorModel> listVendor) {
        this.listVendor = listVendor;
    }
}
