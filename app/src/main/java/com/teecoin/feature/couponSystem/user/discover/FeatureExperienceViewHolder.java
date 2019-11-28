package com.teecoin.feature.couponSystem.user.discover;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.couponsystem.FeatureExperienceModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class FeatureExperienceViewHolder extends ItemViewHolder<FeatureExperienceModel> {

    @BindView(R.id.item_feature_experience_iv_image)
    ImageView iv_image;

    @BindView(R.id.item_feature_experience_tv_title)
    TextView tv_title;

    @BindView(R.id.item_feature_experience_tv_preview)
    TextView tv_preview;

    public FeatureExperienceViewHolder(View itemView) {
        super(itemView);
    }
}
