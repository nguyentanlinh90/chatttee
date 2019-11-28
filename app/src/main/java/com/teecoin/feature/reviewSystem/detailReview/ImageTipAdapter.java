package com.teecoin.feature.reviewSystem.detailReview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class ImageTipAdapter extends RecycleAdapter<ImageModel> {
    private int sizeImage;

    ImageTipAdapter(LayoutInflater inflater, ArrayList<ImageModel> items, RecycleListener<ImageModel> listener) {
        super(inflater, items, listener);
        sizeImage = (int) ((TCScreenSize.getWidth(getActiveActivity()) - (TCUtils.convertDpToPx(getActiveActivity(), 25) * 2)) / 2);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ImageTipViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_image_tip_history_detail;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ImageModel> holder, ImageModel data, int position) {
        if (holder instanceof ImageTipViewHolder) {
            ImageTipViewHolder viewHolder = (ImageTipViewHolder) holder;
            ViewGroup.LayoutParams params = viewHolder.ll_container.getLayoutParams();
            params.width = sizeImage;
            params.height = sizeImage;
            viewHolder.ll_container.setLayoutParams(params);
            Glide.with(getActiveActivity()).asBitmap().load(data.getUrl()).apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_8))).into(viewHolder.imageView);
        }
    }
}
