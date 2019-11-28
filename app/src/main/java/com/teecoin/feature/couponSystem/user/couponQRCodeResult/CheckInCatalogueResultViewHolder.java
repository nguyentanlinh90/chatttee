package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.ui.SimpleRatingBar;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class CheckInCatalogueResultViewHolder extends ItemViewHolder<UserCouponCatalogueDataModel> {

    @BindView(R.id.view_item_coupon_user_catalogue_tv_name_vendor)
    TextView tvVendorName;

    @BindView(R.id.view_item_coupon_user_catalogue_mrb_rating)
    SimpleRatingBar rbVendor;

    @BindView(R.id.view_item_coupon_user_catalogue_tv_reviews)
    TextView tvReviewCount;

    @BindView(R.id.view_item_coupon_user_catalogue_iv_banner)
    ImageView ivCouponImage;

    @BindView(R.id.view_item_coupon_user_iv_is_good_deal)
    ImageView ivGoodDeal;

    @BindView(R.id.view_item_coupon_user_iv_is_new_arrivals)
    ImageView ivNewArrival;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_name)
    TextView tvCouponName;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_price)
    TextView tvCouponPrice;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_purchases_count)
    TextView tvPurchaseCount;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_purchases)
    TextView tvPurchase;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_end)
    TextView tvCouponEnd;


    public CheckInCatalogueResultViewHolder(View itemView) {
        super(itemView);
    }
}