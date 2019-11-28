package com.teecoin.feature.couponSystem.user.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static com.teecoin.utils.TCUtils.setCuisineList;
import static com.teecoin.utils.TCUtils.setStatusTimeVendor;
import static core.base.BaseApplication.getActiveActivity;

public class DiscoverVendorAdapter extends RecycleAdapter<VendorModel> {

    private boolean checkin;

    public DiscoverVendorAdapter(LayoutInflater inflater, ArrayList<VendorModel> items, boolean checkin, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);
        this.checkin = checkin;

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return RecommendForYouViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_vendor;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorModel> holder, VendorModel data, int position) {
        if (holder instanceof RecommendForYouViewHolder) {

            RecommendForYouViewHolder viewHolder = (RecommendForYouViewHolder) holder;

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ?
                    TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getFeaturedImage()).into(viewHolder.iv_image);

            viewHolder.ivFavoriteVendor.setSelected(data.isFavorite());

            viewHolder.tv_name.setText(data.getName());

            viewHolder.tvPriceRange.setText(TCUtils.isEmpty(data.getAvgPrice()) ? "" : data.getAvgPrice() + " ・ ");

            viewHolder.tv_distance.setText(String.format(TCUtils.getString(R.string.distance_to_vendor),
                    TCUtils.formatMoney(TCConstant.ONE_DECIMAL_FORMAT, data.getDistance())));


            setStatusTimeVendor(data, viewHolder.tvOpenOrClose, viewHolder.tvOpenOrCloseTime, viewHolder.tvDot);


            viewHolder.iv_is_coupon.setVisibility(data.isHaveCatalogueCoupon() ? View.VISIBLE : View.GONE);
            viewHolder.iv_is_cash.setVisibility(data.isHaveCashVoucher() ? View.VISIBLE : View.GONE);

            viewHolder.iv_direction.setVisibility(View.VISIBLE);
            if (checkin) {
                viewHolder.view_review.setVisibility(View.GONE);
                viewHolder.tvCuisine.setVisibility(View.GONE);
                viewHolder.view_tec_check_in.setVisibility(View.VISIBLE);

                viewHolder.tv_num_tec.setText(String.format(TCUtils.getString(R.string.text_addition), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getCheckin_amount())));
                viewHolder.tv_num_check_in.setText(data.getTotal_checkin());

            } else {
                viewHolder.view_review.setVisibility(View.VISIBLE);
                //viewHolder.iv_direction.setVisibility(View.GONE);
                viewHolder.tvCuisine.setVisibility(View.VISIBLE);
                viewHolder.view_tec_check_in.setVisibility(View.GONE);
                setCuisineList(data, viewHolder.tvCuisine);

                // viewHolder.rating.setRating(data.getRating());
                viewHolder.rating.setRating(TCUtils.roundRating(data.getRating()));
                TCUtils.setTextReviewCount(data.getReviewCount(), viewHolder.tv_review);

            }

            viewHolder.ivWriteReviewVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_WriteReview));

            viewHolder.ivShareVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Share));

            viewHolder.ivFavoriteVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Favourite));

            viewHolder.iv_direction.setOnClickListener(v -> {
                if(!TCUtils.isEmpty(data.getDistance())){
                    TCUtils.gotoDirection(TCUtils.vendorGetLocation(data.getLocation()));
                }
            });
        }
    }

    public void setStateIconFavorite(int position, ImageView ivFavorite, boolean isFavorite) {

        items.get(position).setFavorite(isFavorite);
        if (ivFavorite != null) {
            ivFavorite.setSelected(isFavorite);
        }
        //  notifyItemChanged(position);
    }

}
