package com.teecoin.feature.reviewSystem.promotion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.PromotionModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class PromotionAdapter extends RecycleAdapter<PromotionModel> {
    boolean isColumns;
    private int sizeImage;

    public PromotionAdapter(LayoutInflater inflater, ArrayList<PromotionModel> items, RecycleListener<PromotionModel> listener, boolean isColumns) {
        super(inflater, items, listener);
        sizeImage = (TCScreenSize.getWidth(getActiveActivity()) / TCConstant.COLUMN_TW0_IMAGE);
        this.isColumns = isColumns;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return PromotionViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {

        return isColumns ? R.layout.item_promotion_vertical : R.layout.item_promotion_horizontal;
    }

    @Override
    protected void bindItemView(ItemViewHolder<PromotionModel> holder, PromotionModel data, int position) {
        if (holder instanceof PromotionViewHolder) {
            PromotionViewHolder viewHolder = (PromotionViewHolder) holder;
            if (isColumns) {
                ViewGroup.LayoutParams params = viewHolder.iv_image.getLayoutParams();
                params.width = sizeImage;
                params.height = sizeImage;
                viewHolder.iv_image.setLayoutParams(params);
            }
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getImages().get(0).getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getImages().get(0).getUrl()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);
            viewHolder.tv_name.setText(data.getTitle());

        }
    }
}
