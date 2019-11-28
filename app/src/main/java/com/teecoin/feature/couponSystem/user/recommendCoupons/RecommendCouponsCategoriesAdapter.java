package com.teecoin.feature.couponSystem.user.recommendCoupons;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.couponCatalogue.RecommendCouponsHorizontalAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class RecommendCouponsCategoriesAdapter extends RecycleAdapter<UserCouponCatalogueModel> {
    private RecommendCouponsHorizontalAdapter adapter;

    RecommendCouponsCategoriesAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueModel> items, RecycleListener<UserCouponCatalogueModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return RecommendCouponsViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_horizontal_list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueModel> holder, UserCouponCatalogueModel data, int position) {
        if (holder instanceof RecommendCouponsViewHolder) {

            RecommendCouponsViewHolder viewHolder = (RecommendCouponsViewHolder) holder;

            viewHolder.tvName.setText(data.getName());
            viewHolder.tvAll.setText(String.format("%s (%s)", TCUtils.getString(R.string.text_all), data.getCount()));
            viewHolder.vAll.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.ViewAll));

            if (data.getCatalogues() != null && data.getCatalogues().size() > 0) {
                adapter = new RecommendCouponsHorizontalAdapter(
                        LayoutInflater.from(getActiveActivity()), data.getCatalogues(), false,
                        (view, item, position1, clickType) -> ((TCMainActivity) getActiveActivity()).addFragment(UserMyCouponDetailScreen.newInstance(item)));

                viewHolder.rcvRecommendCoupons.setAdapter(adapter);
            }
        }
    }

}
