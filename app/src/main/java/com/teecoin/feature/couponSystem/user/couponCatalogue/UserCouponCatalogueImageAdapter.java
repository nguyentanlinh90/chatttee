package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponCatalogueImageAdapter extends RecycleAdapter<UserCouponCatalogueModel> {
    String idCatalogueSelect;
    public UserCouponCatalogueImageAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueModel> items, RecycleListener<UserCouponCatalogueModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponCatalogueImageViewHolder.class;
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
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueModel> holder, UserCouponCatalogueModel data, int position) {
        if (holder instanceof UserCouponCatalogueImageViewHolder) {
            UserCouponCatalogueImageViewHolder viewHolder = (UserCouponCatalogueImageViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_name.setSelected(data.isSelector());
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getImage()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getImage()).into(viewHolder.iv_image);
        }
    }

    public void setAllSelector(boolean selected) {
        for (UserCouponCatalogueModel item : items) {
            item.setSelector(selected);
        }
    }
    public void unSelectedItem(){
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
        for (UserCouponCatalogueModel catalogueModel : items) {
            catalogueModel.setSelector(false);
        }
        catalogueSelect.setSelector(!catalogueSelect.isSelector());
        notifyDataSetChanged();
    }
}
