package com.teecoin.model.reviewsystem;

import java.util.ArrayList;

public class DetailReviewsModel {

    private String comment;
    private ArrayList<ImagesResponseModel> listImage;

    public DetailReviewsModel(String comment, ArrayList<ImagesResponseModel> listImage) {
        this.comment = comment;
        this.listImage = listImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<ImagesResponseModel> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<ImagesResponseModel> listImage) {
        this.listImage = listImage;
    }

}
