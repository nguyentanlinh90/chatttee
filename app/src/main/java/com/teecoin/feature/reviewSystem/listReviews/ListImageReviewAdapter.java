package com.teecoin.feature.reviewSystem.listReviews;

import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class ListImageReviewAdapter extends RecycleAdapter<ImagesResponseModel> {
    private int sizeImageHeight;
    private int sizeImageWidth;
    public ListImageReviewAdapter(LayoutInflater inflater, ArrayList<ImagesResponseModel> items, RecycleListener<ImagesResponseModel> listener) {
        super(inflater, items, listener);
        sizeImageWidth = (TCScreenSize.getWidth(getActiveActivity())*12)/14;
        sizeImageHeight = (sizeImageWidth*4)/7;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ListImageReviewViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_slide_image_video_on_list;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ImagesResponseModel> holder, ImagesResponseModel data, int position) {
        ListImageReviewViewHolder viewHolder = (ListImageReviewViewHolder) holder;
        viewHolder.view_parent.getLayoutParams().width=sizeImageWidth;
        viewHolder.view_parent.getLayoutParams().height=sizeImageHeight;
        Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : data.getUrl()).into(viewHolder.iv_image);
        viewHolder.iv_play_video.setVisibility(TCUtils.isEmpty(data.getVideo_url()) ? View.GONE : View.VISIBLE);
        viewHolder.iv_image.setOnClickListener(v -> listener.onItemClick(viewHolder.iv_image, data, position, EnumMgr.ClickType.ImageReview));
    }
}
