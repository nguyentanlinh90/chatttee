package com.teecoin.feature.reviewSystem.userReviewShop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.feature.reviewSystem.photoMedia.DialogViewImageSliding;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class AddImagesHorizontalAdapter extends RecycleAdapter<MediaModel> {

    private RecycleListener<MediaModel> listener;
    private Context context;
    private ArrayList<MediaModel> listPhoto;

    AddImagesHorizontalAdapter(Context context, LayoutInflater inflater, ArrayList<MediaModel> items, RecycleListener<MediaModel> listener) {
        super(inflater, items, listener);
        this.context = context;
        this.listener = listener;
        listPhoto = items;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ImageHorizontalViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_add_image_horizontion;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindItemView(ItemViewHolder<MediaModel> holder, MediaModel photo, int position) {
        if (holder instanceof ImageHorizontalViewHolder) {
            ImageHorizontalViewHolder viewHolder = (ImageHorizontalViewHolder) holder;

            if (!TCUtils.isEmpty(photo.getPath())) {
                Glide.with(context).asBitmap().load(photo.getPath()).apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_5))).into(viewHolder.iv_photo);
            }
            viewHolder.iv_play_video.setVisibility(photo.isVideo() ? View.VISIBLE : View.GONE);

            viewHolder.ic_remove.setOnClickListener(v -> listener.onItemClick(viewHolder.ic_remove, photo, position, EnumMgr.ClickType.RemovePhoto));
            viewHolder.iv_photo.setOnClickListener(v -> viewImageSliding(position));
        }
    }

    private void viewImageSliding(int position) {
        DialogViewImageSliding dialogViewImageSliding = new DialogViewImageSliding(context, listPhoto, position);
        dialogViewImageSliding.show();
    }
}
