package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.graphics.Typeface;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class DiscoverVendorCatalogueImageAdapter extends RecycleAdapter<VendorCategoryModel> {
    String idCatalogueSelect;
    public DiscoverVendorCatalogueImageAdapter(LayoutInflater inflater, ArrayList<VendorCategoryModel> items, RecycleListener<VendorCategoryModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return DiscoverVendorCatalogueImageViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_coupon_category_image;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorCategoryModel> holder, VendorCategoryModel data, int position) {
        if (holder instanceof DiscoverVendorCatalogueImageViewHolder) {
            DiscoverVendorCatalogueImageViewHolder viewHolder = (DiscoverVendorCatalogueImageViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_name.setSelected(data.isSelector());
            viewHolder.tv_name.setTypeface(
                    viewHolder.tv_name.getTypeface(), data.isSelector() ?
                            Typeface.BOLD : Typeface.NORMAL);
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getBanner()).into(viewHolder.iv_image);
        }
    }

    public void setAllSelector(boolean selected) {
        for (VendorCategoryModel item : items) {
            item.setSelector(selected);
        }
        notifyDataSetChanged();
    }
    public void unSelectedAllItem(){
        for(int pos=0;pos<items.size();pos++){
            if(items.get(pos).isSelector()){
                items.get(pos).setSelector(false);
                notifyItemChanged(pos);
                return;
            }

        }
    }
    public void setSelectedItem(int postion){
        items.get(postion).setSelector(true);
        notifyItemChanged(postion);
    }
    public String getItemSelect() {
        return idCatalogueSelect;
    }
    public void setItemSelect(VendorCategoryModel catalogueSelect) {
        if(catalogueSelect==null)
            return;
        idCatalogueSelect = catalogueSelect.getId();
        for (VendorCategoryModel catalogueModel : items) {
            catalogueModel.setSelector(false);
        }
        catalogueSelect.setSelector(!catalogueSelect.isSelector());
        notifyDataSetChanged();
    }
    public void setItemSelected(VendorCategoryModel catalogueSelect) {
        if(catalogueSelect==null)
            return;
        catalogueSelect.setSelector(true);
        notifyDataSetChanged();
    }
    public VendorCategoryModel getCatagorieSelected(){
        VendorCategoryModel catalogue = null;
        for (VendorCategoryModel catalogueModel : items) {
            if(catalogueModel.isSelector())
                catalogue =catalogueModel;
            return catalogue;
        }
        return catalogue;
    }

}
