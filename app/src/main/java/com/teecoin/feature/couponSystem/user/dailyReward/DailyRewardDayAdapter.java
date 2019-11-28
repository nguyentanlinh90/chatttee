package com.teecoin.feature.couponSystem.user.dailyReward;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.DailyRewardDayModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class DailyRewardDayAdapter extends RecycleAdapter<DailyRewardDayModel> {
    private boolean can_get_giveaway_today;
    private int current_date = 1;// default

    public DailyRewardDayAdapter(LayoutInflater inflater, ArrayList<DailyRewardDayModel> items, RecycleListener<DailyRewardDayModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return DailyRewardDayViewHolder.class;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_daily_reward_day;
    }

    @Override
    protected void bindItemView(ItemViewHolder<DailyRewardDayModel> holder, DailyRewardDayModel data, int position) {
        if (holder instanceof DailyRewardDayViewHolder) {

            DailyRewardDayViewHolder viewHolder = (DailyRewardDayViewHolder) holder;

            viewHolder.tv_day.setText(String.format(TCUtils.getString(R.string.daily_give_tec_day_format), data.getDate()));
            if (can_get_giveaway_today) {
                if (current_date == position) {
                    if (current_date < items.size() - 1) {
                        viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_key_for_day_hightlight));
                    } else {
                        viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_key_for_end_day_hightlight));
                    }
                } else {
                    updateUI(data.getAmount(), viewHolder.iv_icon, viewHolder.tv_tec, position);
                }

            }else{
                updateUI(data.getAmount(), viewHolder.iv_icon, viewHolder.tv_tec, position);

            }

        }
    }

    private void updateUI(String amount, ImageView iv_icon, TextView tv_tec, int pos) {
        if (TCUtils.isEmpty(amount)) {
            tv_tec.setVisibility(View.GONE);
            iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_key_for_day));
        } else {
            tv_tec.setVisibility(View.VISIBLE);
            tv_tec.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, amount));
            iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.bg_oval_transparent_solid_gold_border_2dp));
        }
        if (pos == items.size() - 1) {
            if (TCUtils.isEmpty(amount)) {
                iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_key_for_end_day));
            }
        }
    }

    public void setStatusDate(boolean can_get_giveaway_today, int current_date) {
        this.can_get_giveaway_today = can_get_giveaway_today;
        this.current_date = current_date;
    }
}
