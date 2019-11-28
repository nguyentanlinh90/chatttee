package com.teecoin.feature.reviewSystem.detailReview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.TipDetailModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TipListViewHolder extends ItemViewHolder<TipDetailModel.Tip> {
    @BindView(R.id.frg_wallet_account_transaction_item_tv_name)
    TextView tv_name;

    @BindView(R.id.frg_wallet_account_transaction_item_tv_day)
    TextView tv_day;

    @BindView(R.id.frg_wallet_user_last_trans_item_tv_coin)
    TextView tv_coin;

    @BindView(R.id.frg_wallet_account_transaction_item_iv_icon)
    ImageView iv_icon;

    public TipListViewHolder(View itemView) {
        super(itemView);
    }
}
