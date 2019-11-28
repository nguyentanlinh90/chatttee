package com.teecoin.feature.reviewSystem.location;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class MarkerViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.detail_marker_iv_image)
    ImageView marker_iv_image;
    @BindView(R.id.detail_marker_tv_name)
    TextView marker_tv_name;
    @BindView(R.id.detail_marker_tv_address)
    TextView detail_marker_tv_address;
    @BindView(R.id.detail_marker_tv_total_rate)
    TextView marker_tv_total_rate;
    @BindView(R.id.detail_marker_rb_rating)
    RatingBar marker_rb_rating;

    public MarkerViewHolder(View itemView) {
        super(itemView);
    }
}