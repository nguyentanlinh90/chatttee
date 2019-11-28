package com.teecoin.feature.reviewSystem.vendorReview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class VendorReviewListHolder extends ItemViewHolder<ReviewVendorDetailModel> {
    @BindView(R.id.frag_review_list_review_ll_parent)
    LinearLayout ll_parent;
//    @BindView(R.id.frag_review_list_review_view_image)
//    View view_image;
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
    @BindView(R.id.frag_review_list_review_tv_title)
    TextView tv_title;
    @BindView(R.id.frag_review_list_review_tv_content)
    TextView tv_content;
    //    @BindView(R.id.frag_review_top_review_tv_name)
//    TextView tv_name;
    @BindView(R.id.frag_review_top_review_ll_like)
    View ll_like;
    @BindView(R.id.frag_list_review_tv_total_like)
    TextView tv_total_like;
    @BindView(R.id.frag_list_review_tv_like)
    TextView tv_like;
    @BindView(R.id.frag_list_review_tv_tip_amount)
    TextView tv_tip_amount;
    @BindView(R.id.frag_vendor_detail_ll_tip)
    View ll_tip;
    @BindView(R.id.frag_list_review_tv_vendor)
    TextView tv_vendor;

    @BindView(R.id.frag_vendor_detail_tv_from_google)
    TextView tv_from_google;
    @BindView(R.id.frag_vendor_detail_view_diver)
    View view_diver;


    public VendorReviewListHolder(View itemView) {
        super(itemView);
    }
}
