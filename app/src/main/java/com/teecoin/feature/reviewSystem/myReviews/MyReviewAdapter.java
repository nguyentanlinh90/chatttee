package com.teecoin.feature.reviewSystem.myReviews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.MyReviewsResponseModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class MyReviewAdapter extends RecycleAdapter<MyReviewsResponseModel> {

    public MyReviewAdapter(LayoutInflater inflater, ArrayList<MyReviewsResponseModel> items, RecycleListener<MyReviewsResponseModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return MyReviewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_my_reviews;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<MyReviewsResponseModel> holder, MyReviewsResponseModel data, int position) {
        if (holder instanceof MyReviewHolder) {
            MyReviewHolder viewHolder = (MyReviewHolder) holder;

            viewHolder.tv_vendor.setText(data.getName());
            viewHolder.rating.setRating(Float.parseFloat(data.getRating()));
            if (!TCUtils.isEmpty(data.getReview_count())) {
                viewHolder.tv_total_review.setText(
                        String.format("%s %s", TCUtils.getString(R.string.text_reviews).toLowerCase(), data.getReview_count()));
            }
            Glide.with(getActiveActivity()).asBitmap().load(!TCUtils.isEmpty(data.getImage()) ? data.getImage() : TCUtils.getDrawable(R.drawable.ic_logo_chattee)).into(viewHolder.iv_review);
        }
    }

}
