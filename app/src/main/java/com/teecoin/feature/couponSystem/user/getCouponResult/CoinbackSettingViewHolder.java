package com.teecoin.feature.couponSystem.user.getCouponResult;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.CoinBackModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class CoinbackSettingViewHolder extends ItemViewHolder<CoinBackModel> {

    @BindView(R.id.item_coin_back_redeem_iv_icon)
    ImageView iv_icon;

    @BindView(R.id.item_coin_back_redeem_tv_amount)
    TextView tv_amount;

    @BindView(R.id.item_coin_back_redeem_tv_level)
    TextView tv_level;

    public CoinbackSettingViewHolder(View itemView) {
        super(itemView);
    }
}
