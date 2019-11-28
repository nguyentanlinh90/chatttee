package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.PaymentsVendor;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class PaymentsViewHolder extends ItemViewHolder<PaymentsVendor> {
    @BindView(R.id.payments_iv_icon)
    ImageView iv_icon;

    public PaymentsViewHolder(View itemView) {
        super(itemView);
    }
}
