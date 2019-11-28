package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;


public class FireBaseMediaModel extends TeeCoinModel {

    public static final String ID = "id";
    public static final String THUMBNAIL = "thumbnail";
    public static final String URL = "url";

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("url")
    @Expose
    private String url;

    public FireBaseMediaModel(String id, String thumbnail, String url) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public String getId() {
        return id;
    }
}
