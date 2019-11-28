package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponCatalogueIconAdapter extends RecycleAdapter<UserCouponCatalogueModel> {

    String idCatalogueSelect;

    public UserCouponCatalogueIconAdapter(LayoutInflater inflater, ArrayList<UserCouponCatalogueModel> items, RecycleListener<UserCouponCatalogueModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponCatalogueIconViewHolder.class;
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
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueModel> holder, UserCouponCatalogueModel data, int position) {
        if (holder instanceof UserCouponCatalogueIconViewHolder) {
            UserCouponCatalogueIconViewHolder viewHolder = (UserCouponCatalogueIconViewHolder) holder;

            viewHolder.tv_name.setText(data.getName());
            viewHolder.rl_background.setBackground(TCUtils.getDrawable(
                    data.isSelector() ? R.drawable.bg_oval_white_solid_gold_border : R.drawable.bg_circle_gray));

            Glide.with(getActiveActivity()).load(!TCUtils.isEmpty(data.getIcon()) ?
                    data.getIcon() : TCUtils.getDrawable(R.drawable.ic_avatar_user_gold))
                    .apply(RequestOptions.circleCropTransform()).into(viewHolder.iv_icon);
        }
    }

    public String getItemSelect() {
        return idCatalogueSelect;
    }

    public void setItemSelect(UserCouponCatalogueModel catalogueSelect) {
        idCatalogueSelect = catalogueSelect.getId();
        for (UserCouponCatalogueModel catalogueModel : items) {
            catalogueModel.setSelector(false);
        }
        catalogueSelect.setSelector(!catalogueSelect.isSelector());
        notifyDataSetChanged();
    }
}
