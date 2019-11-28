package com.teecoin.model.general;

import io.realm.RealmObject;

public class AppBannerModel extends RealmObject {

    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String URL = "url";


    private long height;
    private String url;
    private long width;

    public AppBannerModel() {
    }


    public AppBannerModel(long height, String url, long width) {
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
