package com.teecoin.feature.couponSystem.user.catalogues;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.couponsystem.FilterCatalogueModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;


public class FilterCatalogueAdapter extends RecycleAdapter<FilterCatalogueModel> {

    public FilterCatalogueAdapter(LayoutInflater inflater, ArrayList<FilterCatalogueModel> items, RecycleListener<FilterCatalogueModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return FilterCatalogueViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_filter_catalogues;
    }

    @Override
    protected void bindItemView(ItemViewHolder<FilterCatalogueModel> holder, FilterCatalogueModel data, int position) {
        if (holder instanceof FilterCatalogueViewHolder) {
            FilterCatalogueViewHolder viewHolder = (FilterCatalogueViewHolder) holder;

            viewHolder.tv_name.setText(data.getName());
            viewHolder.view_selector.setSelected(data.isSelector());
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(
                    data.getId().equals(EnumMgr.SortByTypeCoupon.GoodDeals.getValue()) ?
                            R.drawable.ic_is_good_deal :
                            data.getId().equals(EnumMgr.SortByTypeCoupon.Coupons.getValue()) ?
                                    R.drawable.ic_is_coupon : R.drawable.ic_is_new_arrivals));
            // todo icon
        }
    }
    public void updateSelector(FilterCatalogueModel item){
        for(FilterCatalogueModel filterCatalogueModel:items){
            if(filterCatalogueModel.isSelector()){
                filterCatalogueModel.setSelector(false);
            }
        }
        item.setSelector(true);
        notifyDataSetChanged();
    }
}
