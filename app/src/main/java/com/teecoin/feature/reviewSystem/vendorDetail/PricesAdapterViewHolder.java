package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.TagPricesModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class PricesAdapterViewHolder extends ItemViewHolder<TagPricesModel> {
    @BindView(R.id.tv_name_price)
    TextView tv_name_price;
    @BindView(R.id.tv_price)
    TextView tv_price;

    public PricesAdapterViewHolder(View itemView) {
        super(itemView);
    }
}
