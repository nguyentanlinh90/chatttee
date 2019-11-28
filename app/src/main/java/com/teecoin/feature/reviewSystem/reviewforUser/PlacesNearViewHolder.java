package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class PlacesNearViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.frag_review_places_near_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_review_places_near_rb_rating)
    RatingBar rb_rating;
    @BindView(R.id.frag_review_places_near_tv_name)
    TextView tv_name;

    public PlacesNearViewHolder(View itemView) {
        super(itemView);
    }
}
