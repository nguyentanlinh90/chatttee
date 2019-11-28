package com.teecoin.feature.general.transactionConfig;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.general.CurrencyModel;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class CurrencyAdapter extends RecycleAdapter<CurrencyModel> {
    private RecycleListener<CurrencyModel> listener;

    public CurrencyAdapter(LayoutInflater inflater, ArrayList<CurrencyModel> items, RecycleListener<CurrencyModel> listener) {
        super(inflater, items, listener);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return CurrencyViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_currency;
    }

    @Override
    protected void bindItemView(ItemViewHolder<CurrencyModel> holder, CurrencyModel data, int position) {
        if (holder instanceof CurrencyViewHolder) {
            CurrencyViewHolder viewHolder = (CurrencyViewHolder) holder;
            viewHolder.tv_currency.setText(data.getCode());
        }
    }
}
