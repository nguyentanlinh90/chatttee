package com.teecoin.feature.reviewSystem.vendorDetailTabPhotos;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesVendor;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class PhotoVendorViewHolder extends ItemViewHolder<ImagesVendor> {
    @BindView(R.id.item_list_image_vendor_iv_image)
    ImageView iv_image;
    @BindView(R.id.item_list_review_iv_play_video)
    ImageView iv_play_video;

    public PhotoVendorViewHolder(View itemView) {
        super(itemView);
    }
}
