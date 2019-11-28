package com.teecoin.model.reviewsystem;

public class SuggestItemModel {

    private String id;
    private String option;
    private String category;

    public SuggestItemModel(String id, String option, String category) {
        this.id = id;
        this.option = option;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getOption() {
        return option;
    }

    public String getCategory() {
        return category;
    }
}
