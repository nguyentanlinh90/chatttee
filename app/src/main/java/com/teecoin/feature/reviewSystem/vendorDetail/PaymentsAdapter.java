package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.PaymentsVendor;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class PaymentsAdapter extends RecycleAdapter<PaymentsVendor> {
    public PaymentsAdapter(LayoutInflater inflater, ArrayList<PaymentsVendor> items, RecycleListener<PaymentsVendor> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return PaymentsViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_payments;
    }

    @Override
    protected void bindItemView(ItemViewHolder<PaymentsVendor> holder, PaymentsVendor data, int position) {
        if (holder instanceof PaymentsViewHolder) {
            if(data!=null){
                if(!TCUtils.isEmpty(data.getUrl())){
                    PaymentsViewHolder viewHolder = (PaymentsViewHolder) holder;
                    Glide.with(getActiveActivity()).load(data.getUrl()).into(viewHolder.iv_icon);
                }
            }
        }
    }
}
