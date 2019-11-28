package com.teecoin.feature.reviewSystem.favourite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class RecentViewHolder extends ItemViewHolder<VendorModel> {
    @BindView(R.id.item_category_single_iv_image)
    ImageView ivImage;
    @BindView(R.id.item_vendor_tv_name)
    TextView tvName;

    public RecentViewHolder(View itemView) {
        super(itemView);
    }
}
