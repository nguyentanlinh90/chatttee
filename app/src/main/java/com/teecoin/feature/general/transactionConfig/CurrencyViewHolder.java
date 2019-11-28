package com.teecoin.feature.general.transactionConfig;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.general.CurrencyModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class CurrencyViewHolder extends ItemViewHolder<CurrencyModel> {
    @BindView(R.id.tv_currency)
    TextView tv_currency;

    public CurrencyViewHolder(View itemView) {
        super(itemView);
    }

}
