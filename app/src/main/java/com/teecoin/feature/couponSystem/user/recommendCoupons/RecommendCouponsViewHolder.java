package com.teecoin.feature.couponSystem.user.recommendCoupons;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class RecommendCouponsViewHolder extends ItemViewHolder<UserCouponCatalogueModel> {

    @BindView(R.id.item_horizontal_list_tv_name)
    TextView tvName;

    @BindView(R.id.ll_view_all)
    View vAll;

    @BindView(R.id.ll_all_tv_all)
    TextView tvAll;

    @BindView(R.id.item_horizontal_list_rcv)
    TCRecyclerView rcvRecommendCoupons;

    public RecommendCouponsViewHolder(View itemView) {
        super(itemView);
    }
}
