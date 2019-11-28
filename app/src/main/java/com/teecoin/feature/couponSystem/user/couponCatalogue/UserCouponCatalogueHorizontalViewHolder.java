package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class UserCouponCatalogueHorizontalViewHolder extends ItemViewHolder<UserCouponCatalogueModel> {
    @BindView(R.id.view_item_coupon_user_catalogue_horizontal_list_header_iv_icon)
    ImageView iv_icon;
    @BindView(R.id.view_item_coupon_user_catalogue_horizontal_list_header_tv_name)
    TextView tv_name;
    @BindView(R.id.ll_view_all)
    View view_all;
    @BindView(R.id.ll_all_tv_all)
    TextView tv_all;
    @BindView(R.id.view_item_coupon_user_catalogue_horizontal_list_rcv_catalogue_data)
    TCRecyclerView rcv_catalogue_data;
    @BindView(R.id.view_item_coupon_user_catalogue_horizontal_list_ll_parent_view)
    View ll_parent_view;

    public UserCouponCatalogueHorizontalViewHolder(View itemView) {
        super(itemView);
    }

}