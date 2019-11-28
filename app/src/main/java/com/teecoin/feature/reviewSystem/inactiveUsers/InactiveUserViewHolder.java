package com.teecoin.feature.reviewSystem.inactiveUsers;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class InactiveUserViewHolder extends ItemViewHolder<InactiveUserModel> {
    @BindView(R.id.item_inactive_user_tv_date)
    TextView tv_date;
    @BindView(R.id.item_inactive_user_tv_year)
    TextView tv_year;
    @BindView(R.id.item_inactive_user_tv_purchase_name)
    TextView tv_purchase_name;
    @BindView(R.id.item_inactive_user_tv_valid_date)
    TextView tv_valid_date;
    @BindView(R.id.item_inactive_user_tv_user_number_paid)
    TextView tv_user_number_paid;

    public InactiveUserViewHolder(View itemView) {
        super(itemView);
    }

}
