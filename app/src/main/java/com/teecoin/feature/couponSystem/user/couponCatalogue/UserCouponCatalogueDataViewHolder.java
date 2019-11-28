package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.ui.SimpleRatingBar;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class UserCouponCatalogueDataViewHolder extends ItemViewHolder<UserCouponCatalogueDataModel> {

    @BindView(R.id.view_tem_coupon_user_catalogue_rl_parent)
    RelativeLayout rl_parent;

    @BindView(R.id.view_tem_coupon_user_catalogue_rl_info)
    LinearLayout rl_info;


    @BindView(R.id.view_tem_coupon_user_catalogue_rl_banner)
    View rl_banner;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_end)
    TextView tv_end;

    @BindView(R.id.view_item_coupon_user_catalogue_iv_banner)
    ImageView iv_banner;

    @BindView(R.id.view_item_coupon_user_catalogue_tv_name_vendor)
    TextView tv_name_vendor;

    @BindView(R.id.view_item_coupon_user_catalogue_mrb_rating)
    SimpleRatingBar rating;

    @BindView(R.id.view_item_coupon_user_catalogue_tv_reviews)
    TextView tv_reviews;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_name)
    TextView tv_name;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_purchases_count)
    TextView tv_purchases_count;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_purchases)
    TextView tv_purchases;

    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_price)
    TextView tv_price;

    @BindView(R.id.view_item_coupon_user_iv_is_new_arrivals)
    ImageView iv_is_new_arrivals;

    @BindView(R.id.view_item_coupon_user_iv_is_good_deal)
    ImageView iv_is_good_deal;

    @BindView(R.id.view_item_coupon_user_iv_is_cash_voucher)
    ImageView iv_is_cash_voucher;


    @BindView(R.id.view_item_coupon_user_catalogue_view_favourite)
    View view_favourite;

    @BindView(R.id.view_item_coupon_user_catalogue_iv_favorite)
    ImageView iv_favorite;

    @BindView(R.id.view_item_coupon_user_catalogue_tv_favorite)
    TextView tv_favorite;

    public UserCouponCatalogueDataViewHolder(View itemView) {
        super(itemView);
    }
}