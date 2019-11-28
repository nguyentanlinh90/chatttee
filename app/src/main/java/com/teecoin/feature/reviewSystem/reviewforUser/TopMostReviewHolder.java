package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ListReviewsModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TopMostReviewHolder extends ItemViewHolder<ListReviewsModel> {
    @BindView(R.id.frag_review_top_ten_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_review_top_ten_iv_avatar_user)
    ImageView iv_avatar_user;
    @BindView(R.id.frag_review_top_ten_tv_name_user)
    TextView tv_name_user;
    @BindView(R.id.frag_review_top_ten_rb_rating)
    RatingBar rb_rating;
    @BindView(R.id.frag_review_top_ten_tv_time_ago)
    TextView tv_time_ago;
    @BindView(R.id.frag_review_top_ten_tv_content)
    TextView tv_content;

    public TopMostReviewHolder(View itemView) {
        super(itemView);
    }
}
