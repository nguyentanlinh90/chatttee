package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class CouponsConfigModel extends TeeCoinModel {
    public static final String FLOATING_ICON = "floating_icon";
    @SerializedName("floating_icon")
    @Expose
    private FloatingIconModel floating_icon;

    public FloatingIconModel getFloating_icon() {
        return floating_icon;
    }

    public void setFloating_icon(FloatingIconModel floating_icon) {
        this.floating_icon = floating_icon;
    }
}
