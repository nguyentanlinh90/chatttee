package com.teecoin.feature.reviewSystem.listReviews;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class ListReviewsViewHolder extends ItemViewHolder<ListReviewsModel> {

    @BindView(R.id.frag_review_list_review_rcv_image)
    TCRecyclerView rcv_image;
    @BindView(R.id.frag_review_list_review_iv_avatar_user)
    ImageView iv_avatar_user;
    @BindView(R.id.frag_review_list_review_tv_name_user)
    TextView tv_name_user;

    @BindView(R.id.frag_review_list_review_rb_rating)
    RatingBar rb_rating;
    @BindView(R.id.frag_review_list_review_tv_time_ago)
    TextView tv_time_ago;
    @BindView(R.id.frag_review_list_review_tv_content)
    TextView tv_content;
    @BindView(R.id.frag_review_top_review_tv_name)
    TextView tv_name;
    @BindView(R.id.frag_review_top_review_ll_like)
    View ll_like;
    @BindView(R.id.frag_list_review_tv_total_like)
    TextView tv_total_like;
    @BindView(R.id.frag_list_review_tv_like)
    TextView tv_like;
    @BindView(R.id.frag_list_review_tv_tip_amount)
    TextView tv_tip_amount;


    public ListReviewsViewHolder(View itemView) {
        super(itemView);
    }
}
