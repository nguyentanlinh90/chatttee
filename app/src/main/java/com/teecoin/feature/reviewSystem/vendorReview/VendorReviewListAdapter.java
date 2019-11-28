package com.teecoin.feature.reviewSystem.vendorReview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.reviewSystem.listReviews.ListImageReviewAdapter;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
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

public class VendorReviewListAdapter extends RecycleAdapter<ReviewVendorDetailModel> {
    private RecycleListener<ReviewVendorDetailModel> listener;
    private boolean limit;
    public VendorReviewListAdapter(LayoutInflater inflater, ArrayList<ReviewVendorDetailModel> items,boolean limit, RecycleListener<ReviewVendorDetailModel> listener) {
        super(inflater, items, listener);
        this.listener = listener;
        this.limit =limit;
    }
    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return VendorReviewListHolder.class;
    }
    @Override
    public int getItemCount() {
        if(limit){// using vendor detail screen
            if(items.size()>=3){
                return 3;
            }else{
                return items.size();
            }

        }else{
            return items.size();
        }

    }
    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_vendor_detail;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<ReviewVendorDetailModel> holder, ReviewVendorDetailModel data, int position) {
        if (holder instanceof VendorReviewListHolder) {
            VendorReviewListHolder viewHolder = (VendorReviewListHolder) holder;
            if (data != null) {
                if (data.getGoogleReviewModel() == null) {
                    viewHolder.tv_vendor.setText(data.getName());
                    viewHolder.tv_from_google.setVisibility(View.GONE);
                    if (data.getAuthor() != null) {
                        viewHolder.tv_name_user.setText(data.getAuthor().getFull_name());
                        Glide.with(getActiveActivity()).load(data.getAuthor().getAvatar()).apply(TCUtils.optionsCircleImageAvatar()).into(viewHolder.iv_avatar_user);
                    }

                    viewHolder.rb_rating.setRating(Float.parseFloat(data.getRating()));
                    if(TCUtils.isEmpty(data.getTitle())){
                        viewHolder.tv_title.setVisibility(View.GONE);
                    }else{
                        viewHolder.tv_title.setText(data.getTitle());
                    }
                    viewHolder.tv_content.setText(data.getComment());
                    viewHolder.tv_content.setText(data.getComment());
                    viewHolder.rb_rating.setIsIndicator(true);
//                    viewHolder.tv_name.setText(data.getName());
                    if (!TCUtils.isEmpty(data.getCreated())) {
                           viewHolder.tv_time_ago.setText(DateTimeAgo.timeAgo(
                                TCDateUtility.toDate(data.getCreated(),
                                        TCDateUtility.TCTimeZone.GMT,
                                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime()));
                    }
                    if (!TCUtils.isEmpty(data.getLike_count())) {
                        viewHolder.tv_total_like.setText(data.getLike_count());
                        int like = Integer.parseInt(data.getLike_count());
                        viewHolder.tv_like.setText(like > 1 ? TCUtils.getString(R.string.review_likes) : TCUtils.getString(R.string.review_like));
                    }
                    if (!TCUtils.isEmpty(data.getTip_amount())) {
                        viewHolder.tv_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getTip_amount()));
                        //  viewHolder.tv_tip_amount.setText(data.getTip_amount());
                    }

                    if (data.getImages() != null && data.getImages().size() > 0) {
                        ListImageReviewAdapter adapter = new ListImageReviewAdapter(LayoutInflater.from(getActiveActivity()), data.getImages(), new RecycleListener<ImagesResponseModel>() {
                            @Override
                            public void onItemClick(View view, ImagesResponseModel item, int positionImage, EnumMgr.ClickType clickType) {
                                if(clickType==EnumMgr.ClickType.ImageReview){
                                    ((TCMainActivity)getActiveActivity()).addFragment(ReviewDetailScreen.newInstance(data, position,positionImage));
                                }
//                                else{
//                                    listener.onItemClick(viewHolder.rcv_image, data, position, EnumMgr.ClickType.Review_ShowReviewDetail);
//                                }

                            }
                        });
                        viewHolder.rcv_image.setAdapter(adapter);
                    } else {
                        viewHolder.rcv_image.setVisibility(View.GONE);
                    }
                    viewHolder.ll_like.setOnClickListener(v -> {
                        if(!TCUtils.isUserApp())
                            return;
                        listener.onItemClick(v, data, position, EnumMgr.ClickType.ReviewList_LikeClicked);
                    });
                } else {
                    viewHolder.rcv_image.setVisibility(View.GONE);
                    viewHolder.ll_tip.setVisibility(View.GONE);
                    viewHolder.tv_from_google.setVisibility(View.VISIBLE);
                    viewHolder.tv_title.setVisibility(View.GONE);
                    viewHolder.tv_content.setText(data.getGoogleReviewModel().getText());
                    viewHolder.tv_name_user.setText(data.getGoogleReviewModel().getAuthor_name());
                    viewHolder.rb_rating.setRating(Float.parseFloat(data.getGoogleReviewModel().getRating()));
                    viewHolder.tv_time_ago.setText(data.getGoogleReviewModel().getRelative_time_description());
                    Glide.with(getActiveActivity()).
                            load(data.getGoogleReviewModel().getProfile_photo_url()).apply(TCUtils.optionsCircleImageAvatar()).into(viewHolder.iv_avatar_user);

                }
            }
        }
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