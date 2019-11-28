package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class UserCouponCatalogueImageViewHolder extends ItemViewHolder<UserCouponCatalogueModel> {
    @BindView(R.id.item_coupon_category_iv_image)
    ImageView iv_image;
    @BindView(R.id.item_coupon_category_tv_name)
    TextView tv_name;

    public UserCouponCatalogueImageViewHolder(View itemView) {
        super(itemView);
    }
}
