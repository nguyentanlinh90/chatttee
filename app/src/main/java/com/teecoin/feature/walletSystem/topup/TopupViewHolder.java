package com.teecoin.feature.walletSystem.topup;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.walletsystem.CryptoModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class TopupViewHolder extends ItemViewHolder<CryptoModel> {
    @BindView(R.id.item_container)
    CardView cdContainer;

    @BindView(R.id.item_crypto_iv_icon)
    ImageView ivIcon;

    @BindView(R.id.item_crypto_tv_title)
    TextView tvTitle;

    @BindView(R.id.item_crypto_tv_timer)
    TextView tvTimer;

    @BindView(R.id.frg_top_up_tv_exchange_rate)
    TextView tvExchangeRate;

    @BindView(R.id.frg_top_up_tv_show_wallet)
    TextView tvShowWallet;

    @BindView(R.id.frg_top_up_rl_address)
    View rlAddress;

    @BindView(R.id.frg_top_up_ll_button)
    View vButton;

    @BindView(R.id.frg_top_up_tv_address_wallet)
    TextView tvAddressWallet;

    @BindView(R.id.fragment_top_up_iv_qr)
    ImageView ivQr;

    @BindView(R.id.fragment_top_up_iv_copy)
    ImageView ivCopy;

    @BindView(R.id.fragment_top_up_iv_share)
    ImageView ivShare;

    public TopupViewHolder(View itemView) {
        super(itemView);
    }
}
