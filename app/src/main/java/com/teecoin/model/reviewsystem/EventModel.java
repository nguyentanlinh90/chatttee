package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventModel implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("date")
    @Expose
    private String date;

    public EventModel() {
    }

    public EventModel(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
