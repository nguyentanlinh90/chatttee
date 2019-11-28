package com.teecoin.feature.reviewSystem.referralDetail;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ReferralHistoryItemModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class ReferralDetailListAdapter extends RecycleAdapter<ReferralHistoryItemModel> {


    public ReferralDetailListAdapter(LayoutInflater inflater, ArrayList<ReferralHistoryItemModel> items, RecycleListener<ReferralHistoryItemModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ReferralDetailViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_referral_detail;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ReferralHistoryItemModel> holder, ReferralHistoryItemModel data, int position) {
        if (holder instanceof ReferralDetailViewHolder) {
            ReferralDetailViewHolder viewHolder = (ReferralDetailViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_date.setText(
                    TCDateUtility.formatDate(
                            TCDateUtility.toDate(data.getCreated(),
                                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS),
                            TCDateUtility.DateFormatDefinition.DD_MM_YYYY));
            viewHolder.tv_amount.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                    TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, data.getAmount())));
            viewHolder.tv_status.setText(data.getStatus());
        }
    }
}
