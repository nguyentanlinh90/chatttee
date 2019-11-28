package com.teecoin.feature.reviewSystem.restaurant;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class RestaurantViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.frag_restaurant_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_restaurant_rb_rating)
    RatingBar rb_rating;
    @BindView(R.id.frag_restaurant_tv_name)
    TextView tv_name;
    @BindView(R.id.frag_restaurant_tv_cuisines)
    TextView tv_cuisines;
    @BindView(R.id.frag_restaurant_tv_review_count)
    TextView tv_review_count;
    @BindView(R.id.frag_restaurant_tv_reviews)
    TextView tv_reviews;
    @BindView(R.id.frag_restaurant_tv_reward)
    TextView tv_reward;
    @BindView(R.id.frag_restaurant_tv_address)
    TextView tv_address;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
    }
}
