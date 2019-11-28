package com.teecoin.feature.reviewSystem.photoMedia;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.MediaModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class MediaSelectionViewHolder extends ItemViewHolder<MediaModel> {

    @BindView(R.id.view)
    View view;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    @BindView(R.id.item_choose_photo_iv_play_video)
    ImageView iv_play_video;

    public MediaSelectionViewHolder(View itemView) {
        super(itemView);
    }
}
