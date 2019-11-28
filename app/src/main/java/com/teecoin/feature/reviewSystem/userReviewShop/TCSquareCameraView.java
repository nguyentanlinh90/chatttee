package com.teecoin.feature.reviewSystem.userReviewShop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.otaliastudios.cameraview.CameraView;

public class TCSquareCameraView extends CameraView {

    public TCSquareCameraView(@NonNull Context context) {
        super(context);
    }

    public TCSquareCameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);


    }
}
