package com.teecoin.feature.reviewSystem.reviewforUser;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class TopMostReviewAdapter extends RecycleAdapter<ListReviewsModel> {

    TopMostReviewAdapter(LayoutInflater inflater, ArrayList<ListReviewsModel> items, RecycleListener<ListReviewsModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TopMostReviewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_top_ten;
    }

    @Override
    protected void bindItemView(ItemViewHolder<ListReviewsModel> holder, ListReviewsModel data, int position) {
        if (holder instanceof TopMostReviewHolder) {
            TopMostReviewHolder viewHolder = (TopMostReviewHolder) holder;
            if (data.getImages() != null) {
                if (data.getImages().size() > 0) {
                    Glide.with(getActiveActivity()).load(data.getImages().get(0).getUrl()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);
                } else if (data.getVendor().getFeatured_image() != null) {
                    Glide.with(getActiveActivity()).load(data.getVendor().getFeatured_image()).apply(TCUtils.optionsSquareImage()).into(viewHolder.iv_image);

                } else {
                    Glide.with(getActiveActivity()).load(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).into(viewHolder.iv_image);
                }
            }
            if (data.getAuthor() != null) {
                Glide.with(getActiveActivity()).load(!TCUtils.isEmpty(data.getAuthor().getAvatar()) ? data.getAuthor().getAvatar() : TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)).apply(TCUtils.optionsCircleImageAvatar()).into(viewHolder.iv_avatar_user);
                viewHolder.tv_name_user.setText(!TCUtils.isEmpty(data.getAuthor().getFull_name()) ? data.getAuthor().getFull_name() : "");
            }

            viewHolder.rb_rating.setRating(Float.parseFloat(data.getRating()));
            viewHolder.rb_rating.setIsIndicator(true);
            if (!TCUtils.isEmpty(data.getCreated())) {
                viewHolder.tv_time_ago.setText(String.format(TCUtils.getString(R.string.review_reviewed_format_time), DateTimeAgo.timeAgo(
                        TCDateUtility.toDate(data.getCreated(),
                                TCDateUtility.TCTimeZone.GMT,
                                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime())));
            }
            viewHolder.tv_content.setText(data.getComment());
        }
    }
}
