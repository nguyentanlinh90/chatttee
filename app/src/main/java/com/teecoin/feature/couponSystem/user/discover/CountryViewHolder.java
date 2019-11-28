package com.teecoin.feature.couponSystem.user.discover;

import android.view.View;
import android.widget.RadioButton;

import com.teecoin.R;
import com.teecoin.model.couponsystem.CountryCodeModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class CountryViewHolder extends ItemViewHolder<CountryCodeModel> {
    @BindView(R.id.item_country_discover_cb_country)
    RadioButton cb_country;

    public CountryViewHolder(View itemView) {
        super(itemView);
    }
}
