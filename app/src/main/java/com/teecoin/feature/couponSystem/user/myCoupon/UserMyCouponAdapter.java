package com.teecoin.feature.couponSystem.user.myCoupon;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.couponsystem.UserCouponModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserMyCouponAdapter extends RecycleAdapter<UserCouponModel> {
    private boolean showStatus;

    public UserMyCouponAdapter(LayoutInflater inflater, ArrayList<UserCouponModel> items, boolean showStatus, RecycleListener<UserCouponModel> listener) {
        super(inflater, items, listener);
        this.showStatus = showStatus;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserMyCouponViewHolder.class;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_coupon_user_my_coupon;
    }

    @Override
    protected void bindItemView(ItemViewHolder<UserCouponModel> holder, UserCouponModel data, int position) {
        if (holder instanceof UserMyCouponViewHolder) {

            UserMyCouponViewHolder viewHolder = (UserMyCouponViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_name_vendor.setText(data.getVendor().getName());
            viewHolder.rb_rating.setRating(data.getVendor().getRating());
            Glide.with(getActiveActivity()).asBitmap().load(data.getBanner())
                    .apply(TCUtils.radiusConnerImage(TCUtils.getDimension(R.dimen.fs_5))).into(viewHolder.iv_picture);

            viewHolder.tv_start_date_time.setText(TCDateUtility.formatTimeForMyCoupon(data.getStart(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCDateUtility.DateFormatDefinition.HH_MM,
                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY));

            viewHolder.tv_end_date_time.setText(TCDateUtility.formatTimeForMyCoupon(data.getEnd(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCDateUtility.DateFormatDefinition.HH_MM,
                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY));

            if (data.getHashTags() != null && data.getHashTags().size() > 0) {
                StringBuilder hash = new StringBuilder();
                for (String hashTag : data.getHashTags()) {
                    hash.append(String.format("#%s ", hashTag));
                }
                viewHolder.tv_hash_tags.setText(hash.toString());
            }
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) viewHolder.rl_tv_name.getLayoutParams();
            if (showStatus) {
                viewHolder.ll_status.setVisibility(View.VISIBLE);
                if (data.getStatus().equals(EnumMgr.CouponUserType.UsedUp.getValue())) {
                    viewHolder.tv_name.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                    viewHolder.tv_status.setBackground(TCUtils.getDrawable(R.drawable.ic_coupon_usedup));
                    viewHolder.tv_status.setText(TCUtils.getString(R.string.coupon_redeemed));
                } else if (data.getStatus().equals(EnumMgr.CouponUserType.Expired.getValue())) {
                    viewHolder.tv_name.setTextColor(TCUtils.getColor(R.color.c_9698a2));
                    viewHolder.tv_status.setBackground(TCUtils.getDrawable(R.drawable.ic_coupon_expired));
                    viewHolder.tv_status.setText(TCUtils.getString(R.string.coupon_expired));
                }
                viewHolder.rl_tv_name.getLayoutParams().width = (TCScreenSize.getWidth(getActiveActivity()));

            } else {
                viewHolder.ll_status.setVisibility(View.GONE);

                relativeParams.setMargins(TCUtils.getDimension(R.dimen.fs_9), 0, 0, 0);
                viewHolder.rl_tv_name.setLayoutParams(relativeParams);
            }

        }
    }
}