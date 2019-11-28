package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.VendorCategoryModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class DiscoverVendorCatalogueImageViewHolder extends ItemViewHolder<VendorCategoryModel> {
    @BindView(R.id.item_coupon_category_iv_image)
    ImageView iv_image;
    @BindView(R.id.item_coupon_category_tv_name)
    TextView tv_name;

    public DiscoverVendorCatalogueImageViewHolder(View itemView) {
        super(itemView);
    }
}
