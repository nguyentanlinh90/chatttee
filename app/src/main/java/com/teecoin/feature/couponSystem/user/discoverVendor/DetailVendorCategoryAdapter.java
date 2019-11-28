package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static com.teecoin.utils.TCUtils.setCuisineList;
import static com.teecoin.utils.TCUtils.setStatusTimeVendor;
import static core.base.BaseApplication.getActiveActivity;

public class DetailVendorCategoryAdapter extends RecycleAdapter<VendorModel> {
    private int width;
    private int height;
    private boolean checkin;

    public DetailVendorCategoryAdapter(LayoutInflater inflater, ArrayList<VendorModel> items,boolean checkin, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);
        this.checkin =checkin;
        width =TCScreenSize.getScreenFavouriteWidth(TCScreenSize.getWidth(getActiveActivity()));
        height =TCScreenSize.getScreenFavouriteHeight(TCScreenSize.getWidth(getActiveActivity()));
//        TCLog.e("width "+width);
//        TCLog.e("height "+height);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return DetailVendorCategoryViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_main_single_category;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorModel> holder, VendorModel data, int position) {
        if (holder instanceof DetailVendorCategoryViewHolder) {

            DetailVendorCategoryViewHolder viewHolder = (DetailVendorCategoryViewHolder) holder;
//            viewHolder.ivImage.getLayoutParams().width = width * 2;
//            viewHolder.ivImage.getLayoutParams().height = height * 2;
//            viewHolder.ivImage.requestLayout();
            viewHolder.rl_logo.getLayoutParams().width = width;
            viewHolder.rl_logo.requestLayout();

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getFeaturedImage()).into(viewHolder.ivImage);

            viewHolder.ivFavoriteVendor.setSelected(data.isFavorite());

            viewHolder.tvName.setText(data.getName());



            viewHolder.tv_price_range.setText(TCUtils.isEmpty(data.getAvgPrice()) ? "" : data.getAvgPrice() + " ・ ");

            viewHolder.tvDistance.setText(String.format(TCUtils.getString(R.string.distance_to_vendor),
                    TCUtils.formatMoney(TCConstant.ONE_DECIMAL_FORMAT, data.getDistance())));

            setCuisineList(data, viewHolder.tvCuisine);

            setStatusTimeVendor(data, viewHolder.tvOpenOrClose, viewHolder.tvOpenOrCloseTime, viewHolder.tvDot);

            viewHolder.ivIsCoupon.setVisibility(data.isHaveCatalogueCoupon() ? View.VISIBLE : View.GONE);

            viewHolder.ivIsCash.setVisibility(data.isHaveCashVoucher() ? View.VISIBLE : View.GONE);

            viewHolder.ivWriteReviewVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_WriteReview));

            viewHolder.ivShareVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Share));

            viewHolder.ivFavoriteVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Favourite));
            viewHolder.iv_direction.setVisibility(View.VISIBLE);
            viewHolder.iv_direction.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_23);
            viewHolder.iv_direction.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_23);
            if(checkin){
                viewHolder.view_review.setVisibility(View.GONE);
                viewHolder.tvCuisine.setVisibility(View.GONE);
                viewHolder.view_tec_check_in.setVisibility(View.VISIBLE);
                viewHolder.tv_num_tec.setText(String.format(TCUtils.getString(R.string.text_addition), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getCheckin_amount())));
                viewHolder.tv_num_check_in.setText(data.getTotal_checkin());

            }else{
              //  viewHolder.iv_direction.setVisibility(View.GONE);
                viewHolder.view_review.setVisibility(View.VISIBLE);
                viewHolder.view_tec_check_in.setVisibility(View.GONE);
                viewHolder.tvCuisine.setVisibility(View.VISIBLE);
                setCuisineList(data, viewHolder.tvCuisine);
                viewHolder.materialRatingBar.setRating(data.getRating());
                TCUtils.setTextReviewCount(data.getReviewCount(), viewHolder.tvReview);
            }
            viewHolder.iv_direction.setOnClickListener(v -> {
                if(!TCUtils.isEmpty(data.getDistance())){
                    TCUtils.gotoDirection(TCUtils.vendorGetLocation(data.getLocation()));
                }
            });
            // viewHolder.view_review
            //  viewHolder.view_tec_check_in
            //  viewHolder.tv_num_tec
            // viewHolder.tv_num_check_in
        }
    }

    public void setStateIconFavorite(int position, boolean isFavorite) {
        items.get(position).setFavorite(isFavorite);
        notifyItemChanged(position);
    }

}
