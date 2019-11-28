package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.MenuCouponModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponResultMenuAdapter extends RecycleAdapter<MenuCouponModel> {


    public UserCouponResultMenuAdapter(LayoutInflater inflater, ArrayList<MenuCouponModel> items, RecycleListener<MenuCouponModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponResultMenuViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_coupon_user_result_menu_image;
    }

    @Override
    protected void bindItemView(ItemViewHolder<MenuCouponModel> holder, MenuCouponModel data, int position) {
        if (holder instanceof UserCouponResultMenuViewHolder) {
            UserCouponResultMenuViewHolder viewHolder = (UserCouponResultMenuViewHolder) holder;
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getImage()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getImage()).into(viewHolder.iv_image);
        }
    }
}