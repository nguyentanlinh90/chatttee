package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.VendorCategoryModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class DiscoverVendorCategoryViewHolder extends ItemViewHolder<VendorCategoryModel> {

    @BindView(R.id.view_item_coupon_user_catalogue_icon_horizontal_iv_icon)
    ImageView iv_icon;
    @BindView(R.id.view_item_coupon_user_catalogue_icon_horizontal_rl_background)
    View rl_background;

    @BindView(R.id.view_item_coupon_user_catalogue_icon_horizontal_tv_name)
    TextView tv_name;

    public DiscoverVendorCategoryViewHolder(View itemView) {
        super(itemView);
    }
}
