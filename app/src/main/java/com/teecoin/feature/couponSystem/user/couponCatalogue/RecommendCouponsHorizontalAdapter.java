package com.teecoin.feature.couponSystem.user.couponCatalogue;

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

public class RecommendCouponsHorizontalAdapter extends RecycleAdapter<UserCouponCatalogueDataModel> {

    private boolean fromReviewCategories;

    public RecommendCouponsHorizontalAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueDataModel> items, boolean fromReviewCategories, RecycleListener<UserCouponCatalogueDataModel> listener) {
        super(inflater, items, listener);
        this.fromReviewCategories = fromReviewCategories;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponCatalogueDataViewHolder.class;
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
        if (holder instanceof UserCouponCatalogueDataViewHolder) {
            UserCouponCatalogueDataViewHolder viewHolder = (UserCouponCatalogueDataViewHolder) holder;

            viewHolder.tv_name.setText(data.getName());

            TCUtils.setTextPurchaseCount(data.getPurchase_times(), viewHolder.tv_purchases_count, viewHolder.tv_purchases);

            viewHolder.tv_price.setText(TCUtils.calculateToTecIncludeFeeAmount(data.getUsd_price()));
            if (data.getVendor() != null) {
                // viewHolder.rating.setRating(data.getVendor().getRating());
                viewHolder.rating.setRating(TCUtils.roundRating(data.getVendor().getRating()));
                viewHolder.tv_name_vendor.setText(data.getVendor().getName());
            }

            if (!fromReviewCategories) {
                // viewHolder.tv_reviews.setVisibility(View.GONE);
                if (!TCUtils.isEmpty(data.getCatalogue_end())) {
                    viewHolder.tv_end.setText(String.format(TCUtils.getString(R.string.coupon_end_at),
                            TCDateUtility.formatTimeForMyCoupon(data.getCatalogue_end(),
                                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                                    TCDateUtility.DateFormatDefinition.HH_MM,
                                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY)));
                }
            } else {
                viewHolder.tv_end.setVisibility(View.GONE);

            }
            if (data.getVendor() != null) {
                viewHolder.tv_reviews.setText(String.format("(%s %s)", data.getVendor().getReviewCount(), data.getVendor().getReviewCount() > 1 ? TCUtils.getString(R.string.text_reviews) : TCUtils.getString(R.string.text_review)).toLowerCase());
            } else {
                viewHolder.tv_reviews.setVisibility(View.GONE);
            }
            viewHolder.iv_is_good_deal.setVisibility(data.isIs_hot_coupon() ? View.VISIBLE : View.GONE);
            viewHolder.iv_is_new_arrivals.setVisibility(data.isIs_new_arrival() ? View.VISIBLE : View.GONE);

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getBanner())
                    .apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_7))).into(viewHolder.iv_banner);
        }
    }
}