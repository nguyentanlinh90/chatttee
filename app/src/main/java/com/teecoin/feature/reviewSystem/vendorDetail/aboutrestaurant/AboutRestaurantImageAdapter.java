package com.teecoin.feature.reviewSystem.vendorDetail.aboutrestaurant;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class AboutRestaurantImageAdapter extends RecycleAdapter<ImageModel> {

    AboutRestaurantImageAdapter(LayoutInflater inflater, ArrayList<ImageModel> items, RecycleListener<ImageModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return AboutRestaurantImageViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_about_restaurant_image;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ImageModel> holder, ImageModel data, int position) {
        if (holder instanceof AboutRestaurantImageViewHolder) {
            AboutRestaurantImageViewHolder viewHolder = (AboutRestaurantImageViewHolder) holder;
            Glide.with(getActiveActivity()).
                    load(TCUtils.isEmpty(data.getUrl()) ?
                            TCUtils.getDrawable(R.drawable.ic_logo_chattee)
                            : data.getUrl()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_restaurant_image);
//            if(listener != null) {
//                imageLayout.setOnClickListener(v -> listener.onItemClick(view, item, position, EnumMgr.ClickType.None));
//            }
        }
    }
}