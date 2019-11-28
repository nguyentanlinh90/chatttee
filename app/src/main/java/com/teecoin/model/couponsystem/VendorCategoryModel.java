package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorCategoryModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("banner")
    @Expose
    private String banner;

    @SerializedName("map_marker_selected")
    @Expose
    private String map_marker_selected;

    @SerializedName("map_marker_unselected")
    @Expose
    private String map_marker_unselected;


    private boolean selector;


    public VendorCategoryModel() {
    }

    public VendorCategoryModel(String id, String name, boolean selector) {
        this.id = id;
        this.name = name;
        this.selector = selector;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getBanner() {
        return banner;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

    public String getMap_marker_selected() {

        return map_marker_selected;
    }

    public String getMap_marker_unselected() {

        return map_marker_unselected;
    }
}
