package com.teecoin.feature.reviewSystem.restaurant;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class RestaurantAdapter extends RecycleAdapter<VendorModel> {
    RestaurantAdapter(LayoutInflater inflater, ArrayList<VendorModel> items, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return RestaurantViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_restaurant;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorModel> holder, VendorModel data, int position) {
        if (holder instanceof RestaurantViewHolder) {
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) holder;
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getFeaturedImage()).into(viewHolder.iv_image);
            viewHolder.rb_rating.setRating(data.getRating());
            viewHolder.rb_rating.setIsIndicator(true);
            viewHolder.tv_name.setText(data.getName());

            //todo release Nov 15
            /*if (data.getCouponCuisines() != null && data.getCouponCuisines().size() > 0) {
                viewHolder.tv_cuisines.setVisibility(View.VISIBLE);
                StringBuilder listCuisines = new StringBuilder();
                for (int i = 0; i < data.getCouponCuisines().size(); i++) {
                    listCuisines.append(i == 0 ? "" : ", ").append(data.getCouponCuisines().get(i).getName());
                }
                viewHolder.tv_cuisines.setText(listCuisines.toString());
            }*/

            viewHolder.tv_review_count.setText(data.getReviewCount());
            viewHolder.tv_reviews.setText(data.getReviewCount() > 1 ?
                    TCUtils.getString(R.string.text_reviews).toLowerCase()
                    : TCUtils.getString(R.string.text_review).toLowerCase());

            //viewHolder.tv_reward.setText(String.format("%s", data.getRewardCount()));
            viewHolder.tv_reward.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getRewardCount()));
            viewHolder.tv_address.setText(data.getAddress());
        }
    }
}
