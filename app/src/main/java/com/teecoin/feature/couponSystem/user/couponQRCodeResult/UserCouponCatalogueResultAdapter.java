package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponCatalogueResultAdapter extends RecycleAdapter<UserCouponCatalogueDataModel> {

    public UserCouponCatalogueResultAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueDataModel> items, RecycleListener<UserCouponCatalogueDataModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponCatalogueResultViewHolder.class;
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
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueDataModel> holder, UserCouponCatalogueDataModel data, int position) {
        if (holder instanceof UserCouponCatalogueResultViewHolder) {
            UserCouponCatalogueResultViewHolder viewHolder = (UserCouponCatalogueResultViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());

            TCUtils.setTextPurchaseCount(data.getPurchase_times(), viewHolder.tv_purchases_count, viewHolder.tv_purchases);

            viewHolder.tv_price.setText(TCUtils.calculateToTecIncludeFeeAmount(data.getUsd_price()));

            if (!TCUtils.isEmpty(data.getCatalogue_end())) {
                viewHolder.tv_end.setText(String.format(TCUtils.getString(R.string.coupon_end_at), TCDateUtility.formatTimeForMyCoupon(data.getCatalogue_end(),
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TCDateUtility.DateFormatDefinition.HH_MM,
                        TCDateUtility.DateFormatDefinition.DD_MM_YYYY)));
            }
            Glide.with(getActiveActivity())
                    .load(TCUtils.isEmpty(data.getBanner()) ?
                            TCUtils.getDrawable(R.drawable.ic_cover_chattee)
                            : data.getBanner())
                    .apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_7)))
                    .into(viewHolder.iv_banner);
        }
    }
}