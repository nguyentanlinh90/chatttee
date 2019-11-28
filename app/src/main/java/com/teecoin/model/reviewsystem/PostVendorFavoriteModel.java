package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostVendorFavoriteModel {
    @SerializedName("vendor_id")
    @Expose
    private String vendor_id;

    @SerializedName("is_favorite")
    @Expose
    private boolean is_favorite;

    public PostVendorFavoriteModel(String vendor_id, boolean is_favorite) {
        this.vendor_id = vendor_id;
        this.is_favorite = is_favorite;
    }
}
