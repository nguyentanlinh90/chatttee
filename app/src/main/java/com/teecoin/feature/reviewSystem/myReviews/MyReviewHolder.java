package com.teecoin.feature.reviewSystem.myReviews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.MyReviewsResponseModel;

import butterknife.BindView;
import core.view.ItemViewHolder;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MyReviewHolder extends ItemViewHolder<MyReviewsResponseModel> {
    @BindView(R.id.iv_review)
    ImageView iv_review;
    @BindView(R.id.tv_vendor_review)
    TextView tv_vendor;
    @BindView(R.id.rating_review)
    MaterialRatingBar rating;
    @BindView(R.id.tv_total_review)
    TextView tv_total_review;

    public MyReviewHolder(View itemView) {
        super(itemView);
    }
}
