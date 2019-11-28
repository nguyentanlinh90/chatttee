package com.teecoin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResultsResponseModel<T> extends CountNextResponseModel {

    @SerializedName("unread")
    @Expose
    private String unread;

    @SerializedName(value = "results", alternate = "result")
    @Expose
    private ArrayList<T> results;

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }


}
