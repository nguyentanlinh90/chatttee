package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class NewMerchantsViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.frag_review_new_merchant_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_review_new_merchant_rb_rating)
    RatingBar rb_rating;
    @BindView(R.id.frag_review_new_merchant_tv_name)
    TextView tv_name;
    @BindView(R.id.frag_review_new_merchant_tv_total_reviews)
    TextView tv_total_reviews;

    public NewMerchantsViewHolder(View itemView) {
        super(itemView);
    }
}