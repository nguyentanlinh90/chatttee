package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.HighlightModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class HighLightViewHolder extends ItemViewHolder<HighlightModel> {


    @BindView(R.id.frag_review_highlight_iv_image)
    ImageView iv_image;

    public HighLightViewHolder(View itemView) {
        super(itemView);
    }
}
