package com.teecoin.feature.reviewSystem.userReviewShop;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.MediaModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class ImageHorizontalViewHolder extends ItemViewHolder<MediaModel> {
    @BindView(R.id.iv_photo)
    ImageView iv_photo;

    @BindView(R.id.ic_remove)
    ImageView ic_remove;

    @BindView(R.id.item_add_image_horizontal_iv_play_video)
    ImageView iv_play_video;

    public ImageHorizontalViewHolder(View itemView) {
        super(itemView);
    }
}
