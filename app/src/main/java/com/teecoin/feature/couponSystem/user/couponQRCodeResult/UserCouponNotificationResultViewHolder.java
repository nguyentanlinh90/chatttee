package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.NotificationCouponModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class UserCouponNotificationResultViewHolder extends ItemViewHolder<NotificationCouponModel> {
    @BindView(R.id.view_item_coupon_user_result_horizontal_rl_banner)
    View rl_banner;
    @BindView(R.id.item_coupon_user_info_item_catalogue_ll_purchase)
    View view_purchase;
    @BindView(R.id.item_coupon_user_info_item_catalogue_ll_purchase_tec)
    View view_purchase_tec;
    @BindView(R.id.view_item_coupon_user_result_horizontal_iv_banner)
    ImageView iv_banner;
    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_name)
    TextView tv_name;
    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_purchases_count)
    TextView tv_purchases;
    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_price)
    TextView tv_price;
    @BindView(R.id.item_coupon_user_info_item_catalogue_tv_end)
    TextView tv_end;

    public UserCouponNotificationResultViewHolder(View itemView) {
        super(itemView);
    }
}