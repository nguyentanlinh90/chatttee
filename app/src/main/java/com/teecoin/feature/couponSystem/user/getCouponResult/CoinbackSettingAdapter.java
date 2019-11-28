package com.teecoin.feature.couponSystem.user.getCouponResult;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.couponsystem.CoinBackModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class CoinbackSettingAdapter extends RecycleAdapter<CoinBackModel> {
    public CoinbackSettingAdapter(LayoutInflater inflater, ArrayList<CoinBackModel> items, RecycleListener<CoinBackModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return CoinbackSettingViewHolder.class;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_coin_back_redeem;
    }

    @Override
    protected void bindItemView(ItemViewHolder<CoinBackModel> holder, CoinBackModel data, int position) {
        if (holder instanceof CoinbackSettingViewHolder) {
            CoinbackSettingViewHolder viewHolder = (CoinbackSettingViewHolder) holder;
            viewHolder.tv_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, data.getAmount()));
            if (!TCUtils.isEmpty(data.getLevel())) {
                String[] splited = data.getLevel().split("\\s+");
                String id = splited[0];
                String redemption = splited[1];
                // viewHolder.tv_level.setText(Html.fromHtml("<b>"+id+"</b>")+" "+redemption);
                SpannableString sid = new SpannableString(id);
                sid.setSpan(new StyleSpan(Typeface.BOLD), 0, id.length(), 0);
                viewHolder.tv_level.append(sid);
                viewHolder.tv_level.append(" ");
                viewHolder.tv_level.append(redemption);
            }

            viewHolder.iv_icon.setImageDrawable(data.isSelected() ? TCUtils.getDrawable(R.drawable.ic_redemption_selected) : TCUtils.getDrawable(R.drawable.ic_redemption_unselected));
        }
    }


}
