package com.teecoin.feature.reviewSystem.inactiveUsers;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.utils.TCDateUtility;

import java.util.ArrayList;
import java.util.Date;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class InactiveUserAdapter extends RecycleAdapter<InactiveUserModel> {

    public InactiveUserAdapter(LayoutInflater inflater, ArrayList<InactiveUserModel> items, RecycleListener<InactiveUserModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return InactiveUserViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_inactive_user;
    }

    @Override
    protected void bindItemView(ItemViewHolder<InactiveUserModel> holder, InactiveUserModel data, int position) {
        if (holder instanceof InactiveUserViewHolder) {
            InactiveUserViewHolder viewHolder = (InactiveUserViewHolder) holder;
            Date date = TCDateUtility.toDate(data.getDate(), TCDateUtility.DateFormatDefinition.DD_MM_YYYY);
            if (date != null) {
                viewHolder.tv_date.setText(TCDateUtility.formatDate(date, TCDateUtility.DateFormatDefinition.DD_MMM));
                viewHolder.tv_year.setText(TCDateUtility.formatDate(date, TCDateUtility.DateFormatDefinition.YYYY));
            }
            viewHolder.tv_purchase_name.setText(data.getPurchase_name());
            viewHolder.tv_valid_date.setText(data.getValid_date());
            viewHolder.tv_user_number_paid.setText(data.getUser_number_paid());
        }
    }
}
