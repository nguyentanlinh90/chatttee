package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.view.View;
import android.widget.TextView;

import com.google.type.LatLng;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorCategoryModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class VendorCategoryViewHolder extends ItemViewHolder<VendorCategoryModel> {

    @BindView(R.id.item_horizontal_list_tv_name)
    TextView tvName;

    @BindView(R.id.ll_view_all)
    View vAll;

    @BindView(R.id.ll_all_tv_all)
    TextView tvAll;

    @BindView(R.id.item_horizontal_list_rcv)
    TCRecyclerView rcvRecommendCoupons;

    public VendorCategoryViewHolder(View itemView) {
        super(itemView);
    }
}
