package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class DiscoverVendorCategoryIconAdapter extends RecycleAdapter<VendorCategoryModel> {

    String idCatalogueSelect;

    public DiscoverVendorCategoryIconAdapter(LayoutInflater inflater, ArrayList<VendorCategoryModel> items, RecycleListener<VendorCategoryModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return DiscoverVendorCategoryViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_coupon_user_catalogue_icon_horizontal;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorCategoryModel> holder, VendorCategoryModel data, int position) {
        if (holder instanceof DiscoverVendorCategoryViewHolder) {
            DiscoverVendorCategoryViewHolder viewHolder = (DiscoverVendorCategoryViewHolder) holder;

            viewHolder.tv_name.setText(data.getName());
            viewHolder.rl_background.setBackground(TCUtils.getDrawable(
                    data.isSelector() ? R.drawable.bg_oval_white_solid_gold_border : R.drawable.bg_circle_gray));

            Glide.with(getActiveActivity()).load((data.getIcon() != null && !TCUtils.isEmpty(data.getIcon())) ?
                    data.getIcon() : TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)).apply(RequestOptions.circleCropTransform()).into(viewHolder.iv_icon);

        }
    }

    public String getItemSelect() {
        return idCatalogueSelect;
    }

    void setItemSelect(UserCouponCatalogueModel catalogueSelect) {
        idCatalogueSelect = catalogueSelect.getId();
        for (VendorCategoryModel catalogueModel : items) {
            catalogueModel.setSelector(false);
        }
        catalogueSelect.setSelector(!catalogueSelect.isSelector());
        notifyDataSetChanged();
    }
}
