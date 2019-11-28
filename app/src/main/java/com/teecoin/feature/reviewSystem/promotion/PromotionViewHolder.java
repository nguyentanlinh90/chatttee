package com.teecoin.feature.reviewSystem.promotion;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.PromotionModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class PromotionViewHolder extends ItemViewHolder<PromotionModel> {
    @BindView(R.id.frag_review_promotion_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_review_promotion_frame)
    View frame;
    @BindView(R.id.frag_review_promotion_tv_name)
    TextView tv_name;

    public PromotionViewHolder(View itemView) {
        super(itemView);
    }

}
