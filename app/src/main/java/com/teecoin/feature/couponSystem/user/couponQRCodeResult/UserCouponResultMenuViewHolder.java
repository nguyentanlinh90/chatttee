package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.MenuCouponModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class UserCouponResultMenuViewHolder extends ItemViewHolder<MenuCouponModel> {
    @BindView(R.id.view_item_coupon_user_result_menu_image_iv_image)
    ImageView iv_image;

    public UserCouponResultMenuViewHolder(View itemView) {
        super(itemView);
    }
}
