package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.view.LayoutInflater;
import android.view.View;

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

public class CheckInCatalogueResultAdapter extends RecycleAdapter<UserCouponCatalogueDataModel> {

    CheckInCatalogueResultAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueDataModel> items, RecycleListener<UserCouponCatalogueDataModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return CheckInCatalogueResultViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_recommend_coupon_horizontal;
    }

    @Override
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueDataModel> holder, UserCouponCatalogueDataModel data, int position) {
        if (holder instanceof CheckInCatalogueResultViewHolder) {

            CheckInCatalogueResultViewHolder viewHolder = (CheckInCatalogueResultViewHolder) holder;

            viewHolder.tvVendorName.setText(data.getVendor().getName());

            viewHolder.rbVendor.setRating(data.getVendor().getRating());

            if (data.getVendor().getReviewCount() > 1) {
                viewHolder.tvReviewCount.setText(String.format(TCUtils.getString(R.string.count_reviews), String.valueOf(data.getVendor().getReviewCount())));
            } else {
                viewHolder.tvReviewCount.setText(String.format(TCUtils.getString(R.string.count_review), String.valueOf(data.getVendor().getReviewCount())));
            }

            Glide.with(getActiveActivity())
                    .load(TCUtils.isEmpty(data.getBanner()) ?
                            TCUtils.getDrawable(R.drawable.ic_cover_chattee)
                            : data.getBanner())
                    .apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_7)))
                    .into(viewHolder.ivCouponImage);

            viewHolder.ivGoodDeal.setVisibility(data.isIs_hot_coupon() ? View.VISIBLE : View.GONE);

            viewHolder.ivNewArrival.setVisibility(data.isIs_new_arrival() ? View.VISIBLE : View.GONE);

            viewHolder.tvCouponName.setText(data.getName());

            viewHolder.tvCouponPrice.setText(TCUtils.calculateToTecIncludeFeeAmount(data.getUsd_price()));

            viewHolder.tvPurchaseCount.setText(data.getPurchase_times());

            if (Integer.parseInt(data.getPurchase_times()) > 1)
                viewHolder.tvPurchase.setText(TCUtils.getString(R.string.coupon_purchases));
            else
                viewHolder.tvPurchase.setText(TCUtils.getString(R.string.purchase_no_cap));

            viewHolder.tvCouponEnd.setText(String.format(TCUtils.getString(R.string.coupon_end_at),
                    TCDateUtility.formatTimeForMyCoupon(data.getCatalogue_end(),
                            TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                            TCDateUtility.DateFormatDefinition.HH_MM,
                            TCDateUtility.DateFormatDefinition.YYYY_MM_DD)));

        }
    }
}