package com.teecoin.feature.reviewSystem.vendorDetailTabPhotos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class PhotoVendorAdapter extends RecycleAdapter<ImagesVendor> {
    private int width;
    private int height;

    public PhotoVendorAdapter(LayoutInflater inflater, ArrayList<ImagesVendor> items, RecycleListener<ImagesVendor> listener) {
        super(inflater, items, listener);
        width = ((TCScreenSize.getWidth(getActiveActivity()) / TCConstant.COLUMN_TW0_IMAGE));// -9
        height = (width*5)/9;

    }
    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return PhotoVendorViewHolder.class;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_list_images_vendor;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<ImagesVendor> holder, ImagesVendor data, int position) {
        if (holder instanceof PhotoVendorViewHolder) {
            PhotoVendorViewHolder viewHolder = (PhotoVendorViewHolder) holder;
            ViewGroup.LayoutParams params = viewHolder.iv_image.getLayoutParams();
            params.width = width;
            params.height = height;
            viewHolder.iv_image.setLayoutParams(params);
            //Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getUrl()).into(viewHolder.iv_image);
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getThumbnail_500()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getThumbnail_500()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);
           viewHolder.iv_play_video.setVisibility(TCUtils.checkURLisVideo(data.getUrl())? View.VISIBLE:View.GONE);
        }
    }


}
