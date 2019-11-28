package com.teecoin.model.couponsystem;

import android.graphics.Bitmap;

public class BitMapSelectModel {

    private String id;

    private Bitmap bitmap;

    public BitMapSelectModel(String id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
