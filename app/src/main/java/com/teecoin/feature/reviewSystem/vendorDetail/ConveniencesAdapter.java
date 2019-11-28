package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ConveniencesVendorModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class ConveniencesAdapter extends RecycleAdapter<ConveniencesVendorModel> {
    public ConveniencesAdapter(LayoutInflater inflater, ArrayList<ConveniencesVendorModel> items, RecycleListener<ConveniencesVendorModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ConveniencesViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_conveniences;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ConveniencesVendorModel> holder, ConveniencesVendorModel data, int position) {
        if (holder instanceof ConveniencesViewHolder) {
            ConveniencesViewHolder viewHolder = (ConveniencesViewHolder) holder;
            viewHolder.tv_name_conveniences.setText(data.getName());
            if (!TCUtils.isEmpty(data.getIcon_name())) {
                String icon = TCConstant.ICON_CONVENIENCE_PREFIX + data.getIcon_name();
                viewHolder.iv_conveniences.setImageDrawable(IconConveniencesUtil.checkIconExits(icon) ? IconConveniencesUtil.getDrawable(icon) : TCUtils.getDrawable(R.drawable.ic_convenience_android_default));
            } else {
                // Default
                viewHolder.iv_conveniences.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_convenience_android_default));
            }

        }
    }
}
