package com.teecoin.model.reviewsystem;


public class MediaModel {
    private String path;
    private boolean selected;
    private boolean isVideo;

    public MediaModel(String path, boolean selected, boolean isVideo) {
        this.path = path;
        this.selected = selected;
        this.isVideo = isVideo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isVideo() {
        return isVideo;
    }
}

