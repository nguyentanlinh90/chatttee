package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserCouponCatalogueModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("catalogues")
    @Expose
    private ArrayList<UserCouponCatalogueDataModel> catalogues;

    private boolean selector;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public ArrayList<UserCouponCatalogueDataModel> getCatalogues() {
        return catalogues;
    }

    public String getIcon() {
        return icon;
    }

    public String getImage() {
        return image;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

}
