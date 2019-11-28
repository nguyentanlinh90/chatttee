package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ConveniencesVendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class ConveniencesViewHolder extends ItemViewHolder<ConveniencesVendorModel> {
    @BindView(R.id.frag_vendor_detail_iv_conveniences)
    ImageView iv_conveniences;

    @BindView(R.id.frag_vendor_detail_tv_name_conveniences)
    TextView tv_name_conveniences;

    public ConveniencesViewHolder(View itemView) {
        super(itemView);
    }
}
