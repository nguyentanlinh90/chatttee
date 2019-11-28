package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoinBackModel implements Serializable {
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("amount")
    @Expose
    private String amount;

    private boolean selected;

    public String getLevel() {
        return level;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
