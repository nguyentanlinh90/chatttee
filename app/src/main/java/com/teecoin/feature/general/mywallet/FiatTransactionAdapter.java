package com.teecoin.feature.general.mywallet;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.general.FiatWalletModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class FiatTransactionAdapter extends RecycleAdapter<FiatWalletModel> {

    public FiatTransactionAdapter(LayoutInflater inflater, ArrayList<FiatWalletModel> items, RecycleListener<FiatWalletModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return FiatViewHolder.class;
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
    protected void bindItemView(ItemViewHolder<FiatWalletModel> holder, FiatWalletModel data, int position) {
        if (holder instanceof FiatViewHolder) {
            FiatViewHolder viewHolder = (FiatViewHolder) holder;
            if(data.getType().equals(EnumMgr.FiatTransactionType.Payment.getValue())){
                viewHolder.tv_name.setText(TCUtils.getString(R.string.payment_received));
            }else if(data.getType().equals(EnumMgr.FiatTransactionType.ConvertToTEC.getValue())){
                viewHolder.tv_name.setText(TCUtils.getString(R.string.convert_to_tec));
            }
            else if(data.getType().equals(EnumMgr.FiatTransactionType.WithdrawRequest.getValue())){
                if(data.getStatus().toLowerCase().equals(TCConstant.TAG_SUCCESS.toLowerCase())){
                    viewHolder.tv_name.setText(TCUtils.getString(R.string.withdraw_completed));
                }else{
                    viewHolder.tv_name.setText(TCUtils.getString(R.string.withdraw_request));
                }
            }

            viewHolder.tv_day.setText(TCDateUtility.convertToCurrentTimeZoneDate(data.getCreated(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM)));

            if (!TCUtils.isEmpty(data.getCash_amount())) {
                viewHolder.tv_coin.setText(String.format(TCUtils.getString(R.string.string_format_1),
                        TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, data.getCash_amount()), data.getCurrency_code()));
            }

            viewHolder.iv_icon.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_30);
            viewHolder.iv_icon.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_30);

            if (!TCUtils.isEmpty(data.getIcon())) {
                Glide.with(getActiveActivity()).load(data.getIcon()).into(viewHolder.iv_icon);
            }


        }
    }


}
