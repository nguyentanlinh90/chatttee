package com.teecoin.feature.reviewSystem.vendorDetail;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.TagPricesModel;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class PricesAdapter extends RecycleAdapter<TagPricesModel> {
    PricesAdapter(LayoutInflater inflater, ArrayList<TagPricesModel> items, RecycleListener<TagPricesModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return PricesAdapterViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_prices_vendor_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<TagPricesModel> holder, TagPricesModel data, int position) {
        if (holder instanceof PricesAdapterViewHolder) {
            PricesAdapterViewHolder viewHolder = (PricesAdapterViewHolder) holder;
            viewHolder.tv_name_price.setText(data.getName());
            viewHolder.tv_price.setText(data.getPrice() + " " + data.getCode());
        }
    }
}
