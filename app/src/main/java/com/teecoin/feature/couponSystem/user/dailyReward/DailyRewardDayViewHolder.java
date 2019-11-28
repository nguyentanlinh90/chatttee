package com.teecoin.feature.couponSystem.user.dailyReward;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.DailyRewardDayModel;

import butterknife.BindView;
import core.view.ItemViewHolder;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DailyRewardDayViewHolder  extends ItemViewHolder<DailyRewardDayModel> {

    @BindView(R.id.item_daily_reward_iv_icon)
    ImageView iv_icon;

    @BindView(R.id.item_daily_reward_tv_tec)
    TextView tv_tec;

    @BindView(R.id.item_daily_reward_tv_day)
    TextView tv_day;

    public DailyRewardDayViewHolder(View itemView) {
        super(itemView);
    }
}
