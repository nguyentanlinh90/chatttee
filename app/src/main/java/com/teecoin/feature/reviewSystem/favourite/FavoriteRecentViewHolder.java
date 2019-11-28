package com.teecoin.feature.reviewSystem.favourite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.ui.SimpleRatingBar;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class FavoriteRecentViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.item_review_main_single_category_rl_logo)
    View rl_logo;

    @BindView(R.id.item_category_single_iv_image)
    ImageView ivImage;

    @BindView(R.id.item_vendor_view_review)
    View view_review;

    @BindView(R.id.item_vendor_tv_name)
    TextView tvName;

    @BindView(R.id.item_vendor_rating)
    SimpleRatingBar materialRatingBar;

    @BindView(R.id.item_vendor_tv_review)
    TextView tvReview;

    @BindView(R.id.item_vendor_tv_price_range)
    TextView tvPriceRange;

    @BindView(R.id.item_vendor_tv_distance)
    TextView tvDistance;

    @BindView(R.id.item_vendor_tv_cuisine)
    TextView tvCuisine;

    @BindView(R.id.item_vendor_tv_open_or_close)
    TextView tvOpenOrClose;

    @BindView(R.id.item_vendor_tv_dot)
    TextView tvDot;

    @BindView(R.id.item_vendor_tv_open_or_close_time)
    TextView tvOpenOrCloseTime;

    @BindView(R.id.item_vendor_iv_is_coupon)
    ImageView ivIsCoupon;

    @BindView(R.id.item_vendor_iv_is_cash)
    ImageView ivIsCash;

    @BindView(R.id.item_category_single_iv_write)
    ImageView ivWriteReviewVendor;

    @BindView(R.id.item_category_single_iv_share)
    ImageView ivShareVendor;

    @BindView(R.id.item_category_single_iv_favorite)
    ImageView ivFavoriteVendor;

    @BindView(R.id.item_vendor_iv_direction)
    ImageView iv_direction;

    @BindView(R.id.item_vendor_view_tec_check_in)
    View view_tec_check_in;

    @BindView(R.id.item_vendor_tv_num_tec)
    TextView tv_num_tec;

    @BindView(R.id.item_vendor_tv_num_check_in)
    TextView tv_num_check_in;

    public FavoriteRecentViewHolder(View itemView) {
        super(itemView);
    }
}
