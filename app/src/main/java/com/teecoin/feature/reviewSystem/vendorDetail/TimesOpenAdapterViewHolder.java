package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.OpenHoursVendorModel;
import com.teecoin.ui.TCRecyclerView;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TimesOpenAdapterViewHolder extends ItemViewHolder<OpenHoursVendorModel> {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rcv_day)
    TCRecyclerView rcv_day;

    @BindView(R.id.tv_close)
    TextView tv_close;

    public TimesOpenAdapterViewHolder(View itemView) {
        super(itemView);
    }
}
