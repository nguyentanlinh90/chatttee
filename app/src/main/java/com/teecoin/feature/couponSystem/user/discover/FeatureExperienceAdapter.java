package com.teecoin.feature.couponSystem.user.discover;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.FeatureExperienceModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class FeatureExperienceAdapter extends RecycleAdapter<FeatureExperienceModel> {

    public FeatureExperienceAdapter(LayoutInflater inflater, ArrayList<FeatureExperienceModel> items, RecycleListener<FeatureExperienceModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return FeatureExperienceViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_feature_experience;
    }

    @Override
    protected void bindItemView(ItemViewHolder<FeatureExperienceModel> holder, FeatureExperienceModel data, int position) {
        if (holder instanceof FeatureExperienceViewHolder) {

            FeatureExperienceViewHolder viewHolder = (FeatureExperienceViewHolder) holder;

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeature_image()) ?
                    TCUtils.getDrawable(R.drawable.ic_banner_default) : data.getFeature_image()).into(viewHolder.iv_image);
            viewHolder.tv_title.setText(data.getTitle());
            viewHolder.tv_preview.setText(data.getExcerpt());

        }
    }

}
