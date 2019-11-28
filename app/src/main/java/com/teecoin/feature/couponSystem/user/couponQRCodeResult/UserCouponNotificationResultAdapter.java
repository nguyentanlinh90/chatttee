package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.NotificationCouponModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponNotificationResultAdapter extends RecycleAdapter<NotificationCouponModel> {

    public UserCouponNotificationResultAdapter(LayoutInflater inflater, ArrayList<NotificationCouponModel> items, RecycleListener<NotificationCouponModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponNotificationResultViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_coupon_user_result_horizontal;
    }

    @Override
    protected void bindItemView(ItemViewHolder<NotificationCouponModel> holder, NotificationCouponModel data, int position) {
        if (holder instanceof UserCouponNotificationResultViewHolder) {
            UserCouponNotificationResultViewHolder viewHolder = (UserCouponNotificationResultViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.view_purchase.setVisibility(View.GONE);
            viewHolder.view_purchase_tec.setVisibility(View.GONE);
            viewHolder.tv_end.setVisibility(View.GONE);
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getBanner()).apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_7))).into(viewHolder.iv_banner);
        }
    }
}