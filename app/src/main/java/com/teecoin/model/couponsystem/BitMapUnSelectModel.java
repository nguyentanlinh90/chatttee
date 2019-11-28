package com.teecoin.model.couponsystem;

import android.graphics.Bitmap;

public class BitMapUnSelectModel {

    private String id;

    private Bitmap bitmap;

    public BitMapUnSelectModel(String id, Bitmap bitmap) {
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
