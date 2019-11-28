package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.HoursVendorModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TimesOpenHorizontalViewHolder extends ItemViewHolder<HoursVendorModel> {

    @BindView(R.id.tv_time_open)
    TextView tv_time_open;

    @BindView(R.id.tv_time_full)
    TextView tv_time_full;

    public TimesOpenHorizontalViewHolder(View itemView) {
        super(itemView);
    }
}
