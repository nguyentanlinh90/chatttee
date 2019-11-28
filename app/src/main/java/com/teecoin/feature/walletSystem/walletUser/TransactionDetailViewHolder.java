package com.teecoin.feature.walletSystem.walletUser;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.walletsystem.TransactionDetailModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TransactionDetailViewHolder extends ItemViewHolder<TransactionDetailModel> {

    @BindView(R.id.frg_wallet_account_transaction_item_tv_name)
    TextView tv_name;

    @BindView(R.id.frg_wallet_account_transaction_item_tv_day)
    TextView tv_day;

    @BindView(R.id.frg_wallet_user_last_trans_item_tv_coin)
    TextView tv_coin;

    @BindView(R.id.frg_wallet_account_transaction_item_iv_icon)
    ImageView iv_icon;



    public TransactionDetailViewHolder(View itemView) {
        super(itemView);
    }


}
