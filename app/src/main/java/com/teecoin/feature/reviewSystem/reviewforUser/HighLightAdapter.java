package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.HighlightModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class HighLightAdapter extends RecycleAdapter<HighlightModel> {
    public HighLightAdapter(LayoutInflater inflater, ArrayList<HighlightModel> items, RecycleListener<HighlightModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return HighLightViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_highlight;
    }

    @Override
    protected void bindItemView(ItemViewHolder<HighlightModel> holder, HighlightModel data, int position) {
        if (holder instanceof HighLightViewHolder) {
            HighLightViewHolder viewHolder = (HighLightViewHolder) holder;
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeatured_image()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getFeatured_image()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);
        }
    }
}
