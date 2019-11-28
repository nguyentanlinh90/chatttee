package com.teecoin.feature.reviewSystem.searchLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.TCConstant;

@SuppressLint("ViewConstructor")
public class CustomViewMarker extends RelativeLayout {

    public static int TYPE_DEFAULT = 1;
    public static int TYPE_CLICK = 2;

    public CustomViewMarker(Context context, Bitmap bitmap, float mapZoom, boolean isSetTitle, VendorModel vendorModel) {

        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_view_marker, this, true);

        ImageView ivMarker = findViewById(R.id.custom_view_marker_iv_marker);
        ivMarker.setImageBitmap(bitmap);

        if (mapZoom >= TCConstant.MAP_ZOOM_DEFAULT && isSetTitle) {
            TextView tvTitle = findViewById(R.id.custom_view_marker_tv_title);
            tvTitle.setText(vendorModel.getName());
        }

//        if (isSetTitle) {
            ImageView ivIsCoupon = findViewById(R.id.view_marker_iv_marker_coupon);
            ivIsCoupon.setVisibility(vendorModel.isHaveCatalogueCoupon() ? VISIBLE : GONE);

            ImageView ivIsCash = findViewById(R.id.view_marker_iv_marker_cash);
            ivIsCash.setVisibility(vendorModel.isHaveCashVoucher() ? VISIBLE : GONE);
//        }
    }
}
