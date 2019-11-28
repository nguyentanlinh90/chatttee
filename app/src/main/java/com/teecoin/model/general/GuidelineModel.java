package com.teecoin.model.general;

import com.teecoin.model.TeeCoinModel;

public class GuidelineModel extends TeeCoinModel {

    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";


    private String image;
    private String title;
    private String content;

    public GuidelineModel() {
    }

    public GuidelineModel(String image, String title, String content) {
        this.image = image;
        this.title = title;
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}