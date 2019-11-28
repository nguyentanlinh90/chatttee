package com.teecoin.feature.reviewSystem.vendorReview;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class VendorReviewThumbnailHolder extends ItemViewHolder<ReviewVendorDetailModel> {
    @BindView(R.id.frag_review_vendor_item_parent)
    View parent;
    @BindView(R.id.frag_review_vendor_item_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_review_vendor_item_iv_more)
    ImageView iv_more;
    @BindView(R.id.item_list_review_iv_play_video)
    ImageView iv_play_video;
    @BindView(R.id.frag_review_vendor_item_rating_bar)
    RatingBar rating_bar;

    public VendorReviewThumbnailHolder(View itemView) {
        super(itemView);
    }
}
