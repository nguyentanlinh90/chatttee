package com.teecoin.feature.reviewSystem.vendorReview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class VendorReviewThumbnailAdapter extends RecycleAdapter<ReviewVendorDetailModel> {
    private int sizeImage;
    private boolean isLoadMore = false;
    VendorReviewThumbnailAdapter(LayoutInflater inflater, ArrayList<ReviewVendorDetailModel> items, RecycleListener<ReviewVendorDetailModel> listener) {
        super(inflater, items, listener);
        sizeImage = ((TCScreenSize.getWidth(getActiveActivity()) / TCConstant.COLUMN_SHOW_PICK_IMAGE))-9;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return VendorReviewThumbnailHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_thumb_review_vendor;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<ReviewVendorDetailModel> holder, ReviewVendorDetailModel data, int position) {
        if (holder instanceof VendorReviewThumbnailHolder) {
            VendorReviewThumbnailHolder viewHolder = (VendorReviewThumbnailHolder) holder;
            viewHolder.iv_more.setVisibility(View.GONE);
            viewHolder.iv_play_video.setVisibility(View.GONE);
            viewHolder.rating_bar.setIsIndicator(true);
            ViewGroup.LayoutParams params = viewHolder.parent.getLayoutParams();
            params.width = sizeImage;
            params.height = sizeImage;
            viewHolder.parent.setLayoutParams(params);
            if(data.getImages()!=null&&data.getImages().size()>0){
                ImagesResponseModel imagesResponseModel = data.getImages().get(0);
                viewHolder.iv_more.setVisibility(data.getImages().size()>1?View.VISIBLE:View.GONE);
                Glide.with(getActiveActivity()).asBitmap().apply(new RequestOptions().override(sizeImage, sizeImage).diskCacheStrategy(DiskCacheStrategy.ALL)).load(imagesResponseModel.getUrl()).into(viewHolder.iv_image);
                viewHolder.iv_play_video.setVisibility(TCUtils.isEmpty(imagesResponseModel.getVideo_url()) ? View.GONE : View.VISIBLE);
//                viewHolder.iv_play_video.setOnClickListener(v -> ((TCMainActivity)getActiveActivity()).gotoPlayVideo(imagesResponseModel));
            } else {
                Glide.with(getActiveActivity()).asBitmap().apply(new RequestOptions().override(sizeImage, sizeImage).diskCacheStrategy(DiskCacheStrategy.ALL)).load(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).into(viewHolder.iv_image);
            }
            if(data.getRating()!=null){
                viewHolder.rating_bar.setRating(Float.parseFloat(data.getRating()));
            }

        }
    }
    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }
}