package com.teecoin.feature.couponSystem.user.myCoupon;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponModel;

import butterknife.BindView;
import core.view.ItemViewHolder;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class UserMyCouponViewHolder extends ItemViewHolder<UserCouponModel> {

    @BindView(R.id.view_item_coupon_user_my_coupon_ll_status)
    View ll_status;
    @BindView(R.id.view_item_coupon_user_my_coupon_tv_status)
    TextView tv_status;
    @BindView(R.id.view_item_coupon_user_my_coupon_rl_tv_name)
    RelativeLayout rl_tv_name;
    @BindView(R.id.view_item_coupon_user_my_coupon_tv_name)
    TextView tv_name;
    @BindView(R.id.item_coupon_user_my_coupon_rb_rating)
    MaterialRatingBar rb_rating;
    @BindView(R.id.item_coupon_user_my_coupon_iv_picture)
    ImageView iv_picture;
    @BindView(R.id.item_coupon_user_my_coupon_tv_name_vendor)
    TextView tv_name_vendor;
    @BindView(R.id.view_coupon_user_tv_start_date_time)
    TextView tv_start_date_time;
    @BindView(R.id.view_coupon_user_tv_end_date_time)
    TextView tv_end_date_time;
    @BindView(R.id.item_coupon_user_my_coupon_tv_hash_tags)
    TextView tv_hash_tags;

    public UserMyCouponViewHolder(View itemView) {
        super(itemView);
    }

}
