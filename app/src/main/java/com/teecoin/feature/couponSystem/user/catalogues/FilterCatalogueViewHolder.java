package com.teecoin.feature.couponSystem.user.catalogues;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.FilterCatalogueModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class FilterCatalogueViewHolder extends ItemViewHolder<FilterCatalogueModel> {

    @BindView(R.id.item_list_filter_rl_parent)
    View view_parent;

    @BindView(R.id.item_list_filter_ll_selector)
    View view_selector;

    @BindView(R.id.item_list_filter_tv_name)
    TextView tv_name;

    @BindView(R.id.item_list_filter_iv_icon)
    ImageView iv_icon;

    public FilterCatalogueViewHolder(View itemView) {
        super(itemView);
    }
}
