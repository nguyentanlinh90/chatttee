package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuggestModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("options")
    @Expose
    private ArrayList<String> options = null;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

}
