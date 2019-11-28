package com.teecoin.feature.reviewSystem.listReviews;

import android.view.View;
import android.widget.ImageView;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import butterknife.BindView;
import core.view.ItemViewHolder;

public class ListImageReviewViewHolder extends ItemViewHolder<ImagesResponseModel> {

    @BindView(R.id.item_list_review_view_parent)
    View view_parent;
    @BindView(R.id.item_list_review_iv_photo)
    ImageView iv_image;
    @BindView(R.id.item_list_review_iv_play_video)
    ImageView iv_play_video;

    public ListImageReviewViewHolder(View itemView) {
        super(itemView);
    }
}
