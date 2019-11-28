package com.teecoin.feature.reviewSystem.listReviews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class ListReviewAdapter extends RecycleAdapter<ListReviewsModel> {

    private RecycleListener<ListReviewsModel> listener;
    ListReviewAdapter(LayoutInflater inflater, ArrayList<ListReviewsModel> items, RecycleListener<ListReviewsModel> listener) {
        super(inflater, items, listener);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return ListReviewsViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_list_reviews;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<ListReviewsModel> holder, ListReviewsModel data, int position) {
        if (holder instanceof ListReviewsViewHolder) {
            ListReviewsViewHolder viewHolder = (ListReviewsViewHolder) holder;
            viewHolder.tv_name_user.setText(!TCUtils.isEmpty(data.getAuthor().getFull_name()) ?
                    data.getAuthor().getFull_name() : "");

            viewHolder.tv_name.setText(data.getVendor().getName());
            viewHolder.rb_rating.setRating(Float.parseFloat(data.getRating()));
            viewHolder.rb_rating.setIsIndicator(true);
            if (!TCUtils.isEmpty(data.getCreated())) {
                viewHolder.tv_time_ago.setText(String.format(TCUtils.getString(R.string.review_reviewed_format_time), DateTimeAgo.timeAgo(
                        TCDateUtility.toDate(data.getCreated(),
                                TCDateUtility.TCTimeZone.GMT,
                                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime())).trim());
            }
            viewHolder.tv_content.setText(data.getComment());
            if (!TCUtils.isEmpty(data.getLike_count())) {
                viewHolder.tv_total_like.setText(data.getLike_count());
                viewHolder.tv_like.setText(Integer.parseInt(data.getLike_count()) > 1 ? TCUtils.getString(R.string.review_likes) : TCUtils.getString(R.string.review_like));
            }
            if (!TCUtils.isEmpty(data.getTip_amount())) {
                viewHolder.tv_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getTip_amount()));
            }
            Glide.with(getActiveActivity()).load(
                    TCUtils.isEmpty(data.getAuthor().getAvatar()) ?
                            TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) :
                            data.getAuthor().getAvatar())
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold))
                            .error(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)))
                    .into(viewHolder.iv_avatar_user);


            if (data.getImages() != null && data.getImages().size() > 0) {
                viewHolder.rcv_image.setVisibility(View.VISIBLE);
                ListImageReviewAdapter adapter = new ListImageReviewAdapter(LayoutInflater.from(getActiveActivity()), data.getImages(), new RecycleListener<ImagesResponseModel>() {
                    @Override
                    public void onItemClick(View view, ImagesResponseModel item, int position, EnumMgr.ClickType clickType) {
                        listener.onItemClick(viewHolder.rcv_image, data, position, EnumMgr.ClickType.Review_ShowReviewDetail);
                    }
                });
                viewHolder.rcv_image.setAdapter(adapter);
            } else {
                viewHolder.rcv_image.setVisibility(View.GONE);
            }
            viewHolder.ll_like.setOnClickListener(v -> {
                boolean tipBefore = false;
                if (!TCUtils.isEmpty(data.getTip_of_user())) {
                    float tip = Float.parseFloat(data.getTip_of_user());
                    if (tip > 0)
                        tipBefore = true;
                }
                if (!TCUtils.isMyselfReview(data.getAuthor().getPublic_key())) {
                    showPopupTip(tipBefore, data.getAuthor().getPublic_key(), data.getId(), position);
                } else {
                    ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
                }
                listener.onItemClick(v, data, position, EnumMgr.ClickType.ReviewList_LikeClicked);
            });
        }
    }

    private void showPopupTip(boolean tipBefore, String destinationPublicKey, String commentId, int position) {
        DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipBefore, destinationPublicKey, commentId, amount -> {
            if (amount > 0) {
                if (items.get(position).getTip_amount() != null) {
                    float tip_amount = Float.parseFloat(items.get(position).getTip_amount());
                    items.get(position).setTip_amount((amount + tip_amount) + "");
                }
                if (items.get(position).getTip_of_user() != null) {
                    float tip_user = Float.parseFloat(items.get(position).getTip_of_user());
                    items.get(position).setTip_of_user((amount + tip_user) + "");
                }
                notifyItemChanged(position);
            }
        });
        dialogTipVendor.show();
        dialogTipVendor.setCanceledOnTouchOutside(false);
    }

    public void updateLikeItem(int position) {
        if (position < items.size()) {
            if (items.get(position).isIs_like()) {
                return;
            }
            items.get(position).setIs_like(true);
            if (items.get(position).getLike_count() != null) {
                int like_count = Integer.parseInt(items.get(position).getLike_count());
                if (like_count >= 0) {
                    like_count += 1;
                    items.get(position).setLike_count(like_count + "");
                }
            }
        }
        notifyItemChanged(position);
    }
}
