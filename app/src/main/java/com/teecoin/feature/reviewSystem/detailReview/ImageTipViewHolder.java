package com.teecoin.feature.reviewSystem.detailReview;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImageModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class ImageTipViewHolder extends ItemViewHolder<ImageModel> {
    @BindView(R.id.ll_container)
    View ll_container;
    @BindView(R.id.image)
    ImageView imageView;

    public ImageTipViewHolder(View itemView) {
        super(itemView);
    }
}
