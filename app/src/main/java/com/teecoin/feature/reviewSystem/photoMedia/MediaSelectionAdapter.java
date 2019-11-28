package com.teecoin.feature.reviewSystem.photoMedia;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;

import java.io.File;
import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class MediaSelectionAdapter extends RecycleAdapter<MediaModel> {

    private Activity activity;
    private int sizeImage;

    MediaSelectionAdapter(Activity activity, LayoutInflater inflater, ArrayList<MediaModel> items, RecycleListener<MediaModel> listener) {
        super(inflater, items, listener);
        this.activity = activity;
        sizeImage = (TCScreenSize.getWidth(activity) / TCConstant.COLUMN_SHOW_PICK_IMAGE);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return MediaSelectionViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_choose_photo;
    }

    @Override
    protected void bindItemView(ItemViewHolder<MediaModel> holder, MediaModel item, int position) {
        MediaSelectionViewHolder viewHolder = (MediaSelectionViewHolder) holder;
        //TCLog.e("item: "+item.getPath());
        viewHolder.checkbox.setChecked(item.isSelected());
        ViewGroup.LayoutParams params = viewHolder.view.getLayoutParams();
        params.width = sizeImage;
        params.height = sizeImage;
        viewHolder.view.setLayoutParams(params);
        File file = new File(item.getPath());
        if (file.exists()) {
            Glide.with(activity).asBitmap().load(file).apply(new RequestOptions().override(sizeImage, sizeImage)).into((viewHolder).iv_thumbnail);
        }
        viewHolder.iv_play_video.setVisibility(item.isVideo() ? View.VISIBLE : View.GONE);

    }
}
