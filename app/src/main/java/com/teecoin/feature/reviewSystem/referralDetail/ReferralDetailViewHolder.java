package com.teecoin.feature.reviewSystem.referralDetail;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ReferralHistoryItemModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class ReferralDetailViewHolder extends ItemViewHolder<ReferralHistoryItemModel> {

    @BindView(R.id.item_referral_detail_tv_name)
    TextView tv_name;

    @BindView(R.id.item_referral_detail_tv_date)
    TextView tv_date;

    @BindView(R.id.item_referral_detail_tv_amount)
    TextView tv_amount;

    @BindView(R.id.item_referral_detail_tv_status)
    TextView tv_status;


    public ReferralDetailViewHolder(View itemView) {
        super(itemView);
    }


}
