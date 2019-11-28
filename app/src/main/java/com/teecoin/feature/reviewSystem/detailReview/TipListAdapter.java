package com.teecoin.feature.reviewSystem.detailReview;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.TipDetailModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class TipListAdapter extends RecycleAdapter<TipDetailModel.Tip> {
    private String shop_name;

    TipListAdapter(LayoutInflater inflater, String shopName, ArrayList<TipDetailModel.Tip> items, RecycleListener<TipDetailModel.Tip> listener) {
        super(inflater, items, listener);
        shop_name = shopName;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TipListViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_last_transaction;
    }

    @Override
    protected void bindItemView(ItemViewHolder<TipDetailModel.Tip> holder, TipDetailModel.Tip data, int position) {
        if (holder instanceof TipListViewHolder) {
            TipListViewHolder viewHolder = (TipListViewHolder) holder;

            //hardcode "System"
            viewHolder.tv_name.setText("System".equals(data.getTippedBy()) ? shop_name : data.getTippedBy());

            viewHolder.tv_day.setText(TCDateUtility.convertToCurrentTimeZoneDate(data.getCreated(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM_SS)));

            viewHolder.tv_coin.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getAmount()), TCUtils.getString(R.string.tee_coin_symbol)));

            viewHolder.iv_icon.setImageDrawable(
                    TCUtils.getDrawable(
                            data.getStatus().equals(TransactionDetailModel.TransactionDetailStatus.FAILED.name()) ?
                                    R.drawable.ic_transaction_failed : R.drawable.ic_gift_gold));

        }
    }
}