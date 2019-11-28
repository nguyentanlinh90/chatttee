package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponCatalogueVerticalAdapter extends RecycleAdapter<UserCouponCatalogueDataModel> {
    private int sizeBannerHeight;
    private int sizeBannerWidth;
    private boolean showExpired;
    private boolean resizeBanner;

    public UserCouponCatalogueVerticalAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueDataModel> items, boolean showExpired, boolean resizeBanner, RecycleListener<UserCouponCatalogueDataModel> listener) {
        super(inflater, items, listener);
        sizeBannerWidth = (TCScreenSize.getWidth(getActiveActivity()));
        sizeBannerHeight = TCScreenSize.frameBannerCouponHeight(sizeBannerWidth);
        this.showExpired = showExpired;
        this.resizeBanner = resizeBanner;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? ITEM_TYPE : LOADING_TYPE;
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
        return R.layout.view_item_coupon_user_catalogue_vertical;
    }

    @Override
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueDataModel> holder, UserCouponCatalogueDataModel data, int position) {
        if (holder instanceof UserCouponCatalogueDataViewHolder) {
            UserCouponCatalogueDataViewHolder viewHolder = (UserCouponCatalogueDataViewHolder) holder;

            viewHolder.tv_name.setText(data.getName());

            viewHolder.tv_reviews.setText(String.valueOf(data.getVendor().getReviewCount()));

            TCUtils.setTextPurchaseCount(data.getPurchase_times(), viewHolder.tv_purchases_count, viewHolder.tv_purchases);

            viewHolder.tv_price.setText(TCUtils.calculateToTecIncludeFeeAmount(data.getUsd_price()));
            if (data.getVendor() != null) {

                // viewHolder.rating.setRating(data.getVendor().getRating());
                viewHolder.rating.setRating(TCUtils.roundRating(data.getVendor().getRating()));

                TCUtils.setTextReviewCount(data.getVendor().getReviewCount(), viewHolder.tv_reviews);

                viewHolder.tv_name_vendor.setText(data.getVendor().getName());
            }

            if (showExpired) {
                viewHolder.tv_end.setVisibility(View.VISIBLE);
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
            viewHolder.iv_is_good_deal.setVisibility(data.isIs_hot_coupon() ? View.VISIBLE : View.GONE);
            viewHolder.iv_is_new_arrivals.setVisibility(data.isIs_new_arrival() ? View.VISIBLE : View.GONE);

            // todo: viewHolder.iv_favorite
            // todo: viewHolder.tv_favorite


            if (resizeBanner) {
                viewHolder.rl_banner.getLayoutParams().height = sizeBannerHeight;


                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.rl_parent.getLayoutParams();
                params.setMargins(25, 0, 0, 16);
                viewHolder.rl_parent.setLayoutParams(params);

                viewHolder.iv_is_good_deal.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_38);
                viewHolder.iv_is_good_deal.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_38);

                viewHolder.iv_is_new_arrivals.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_38);
                viewHolder.iv_is_new_arrivals.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_38);

                viewHolder.iv_is_cash_voucher.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_38);
                viewHolder.iv_is_cash_voucher.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_38);
                Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getBanner()).apply(new RequestOptions().centerCrop()).into(viewHolder.iv_banner);

            } else {
                Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getBanner()).into(viewHolder.iv_banner);

            }

            //   viewHolder.rl_banner.getLayoutParams().height = sizeBannerHeight;
        }
    }
}
