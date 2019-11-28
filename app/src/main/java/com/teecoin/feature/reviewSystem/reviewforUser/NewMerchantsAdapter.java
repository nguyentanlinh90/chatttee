package com.teecoin.feature.reviewSystem.reviewforUser;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class NewMerchantsAdapter extends RecycleAdapter<VendorModel> {

    NewMerchantsAdapter(LayoutInflater inflater, ArrayList<VendorModel> items, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return NewMerchantsViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_new_merchants;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void bindItemView(ItemViewHolder<VendorModel> holder, VendorModel data, int position) {
        if (holder instanceof NewMerchantsViewHolder) {
            NewMerchantsViewHolder viewHolder = (NewMerchantsViewHolder) holder;
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getFeaturedImage()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);
            viewHolder.rb_rating.setRating(data.getRating());
            viewHolder.tv_name.setText(data.getName());
            viewHolder.rb_rating.setIsIndicator(true);
            viewHolder.tv_total_reviews.setText(String.format(TCUtils.getString(R.string.string_format_1), data.getReviewCount(), TCUtils.getString(R.string.text_reviews)));
        }
    }
}
