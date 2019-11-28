package com.teecoin.feature.reviewSystem.vendorDetail.aboutrestaurant;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImageModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class AboutRestaurantImageViewHolder extends ItemViewHolder<ImageModel> {
    @BindView(R.id.view_item_about_restaurant_image_iv_restaurant_image)
    ImageView iv_restaurant_image;

    public AboutRestaurantImageViewHolder(View itemView) {
        super(itemView);
    }
}
