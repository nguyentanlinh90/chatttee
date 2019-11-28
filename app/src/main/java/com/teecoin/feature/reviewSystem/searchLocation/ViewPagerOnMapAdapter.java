package com.teecoin.feature.reviewSystem.searchLocation;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.ui.SimpleRatingBar;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.RecycleListener;
import core.view.ViewPagerAdapter;

import static com.teecoin.utils.TCUtils.setCuisineList;
import static com.teecoin.utils.TCUtils.setStatusTimeVendor;
import static core.base.BaseApplication.getActiveActivity;

public class ViewPagerOnMapAdapter extends ViewPagerAdapter<VendorModel> {

    ViewPagerOnMapAdapter(LayoutInflater inflater, ArrayList<VendorModel> items, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
//        Log.d("linhnt page size: ", String.valueOf(items.size()));
        return items.size();

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View holder = inflater.inflate(R.layout.item_vendor_in_map_search, container, false);

        container.addView(holder, 0);

        VendorModel data = items.get(position);

        if (holder != null) {

            View vIndicatorSelect = holder.findViewById(R.id.item_vendor_v_indicator_select);
            ImageView ivLogo = holder.findViewById(R.id.item_vendor_iv_image);
            ImageView ivWriteReviewVendor = holder.findViewById(R.id.item_vendor_iv_write);
            ImageView ivShareVendor = holder.findViewById(R.id.item_vendor_iv_share);
            ImageView ivFavoriteVendor = holder.findViewById(R.id.item_vendor_iv_favorite);
            TextView tvName = holder.findViewById(R.id.item_vendor_tv_name);
            SimpleRatingBar ratingBar = holder.findViewById(R.id.item_vendor_rating);
            TextView tvReview = holder.findViewById(R.id.item_vendor_tv_review);
            TextView tvPriceRange = holder.findViewById(R.id.item_vendor_tv_price_range);
            TextView tvDistance = holder.findViewById(R.id.item_vendor_tv_distance);
            TextView tvCuisine = holder.findViewById(R.id.item_vendor_tv_cuisine);
            TextView tvOpenOrClose = holder.findViewById(R.id.item_vendor_tv_open_or_close);
            TextView tvDot = holder.findViewById(R.id.item_vendor_tv_dot);
            TextView tvOpenOrCloseTime = holder.findViewById(R.id.item_vendor_tv_open_or_close_time);
            ImageView ivIsCoupon = holder.findViewById(R.id.item_vendor_iv_is_coupon);
            ImageView ivIsCash = holder.findViewById(R.id.item_vendor_iv_is_cash);
            View parentView = holder.findViewById(R.id.ll_container);

            // todo
            ImageView ivDirection = holder.findViewById(R.id.item_vendor_iv_direction);
            View vTecCheckin = holder.findViewById(R.id.item_vendor_view_tec_check_in);
            TextView tvNumTec = holder.findViewById(R.id.item_vendor_tv_num_tec);
            TextView tvNumCheckin = holder.findViewById(R.id.item_vendor_tv_num_check_in);

            ivDirection.setOnClickListener(v -> {
                if(!TCUtils.isEmpty(data.getDistance())){
                    TCUtils.gotoDirection(TCUtils.vendorGetLocation(data.getLocation()));
                }
            });

            vIndicatorSelect.setVisibility(data.isSelectVendor() ? View.VISIBLE : View.INVISIBLE);

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ?
                    TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getFeaturedImage()).into(ivLogo);

            ivFavoriteVendor.setSelected(data.isFavorite());

            tvName.setText(data.getName());

            ratingBar.setRating(data.getRating());

            TCUtils.setTextReviewCount(data.getReviewCount(), tvReview);

            tvPriceRange.setText(TCUtils.isEmpty(data.getAvgPrice()) ? "" : data.getAvgPrice() + " ・ ");

            tvDistance.setText(String.format(TCUtils.getString(R.string.distance_to_vendor),
                    TCUtils.formatMoney(TCConstant.ONE_DECIMAL_FORMAT, data.getDistance())));

            setCuisineList(data, tvCuisine);

            setStatusTimeVendor(data, tvOpenOrClose, tvOpenOrCloseTime, tvDot);

            ivIsCoupon.setVisibility(data.isHaveCatalogueCoupon() ? View.VISIBLE : View.INVISIBLE);

            ivIsCash.setVisibility(data.isHaveCashVoucher() ? View.VISIBLE : View.INVISIBLE);

            ivWriteReviewVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_WriteReview));

            ivShareVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Share));

            ivFavoriteVendor.setOnClickListener(v -> listener.onItemClick(v, data, position,
                    EnumMgr.ClickType
                            .Vendor_Favourite));

            parentView.setOnClickListener(v -> listener.onItemClick(v, data, position,
                    EnumMgr.ClickType
                            .None));
        }
        return holder;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.6f;
    }

    void highlightVendorSlide(int pos) {

        for (int i = 0; i < items.size(); i++) {
            if (pos == i) {
                items.get(pos).setSelectVendor(true);
            } else {
                items.get(i).setSelectVendor(false);
            }
        }

        notifyDataSetChanged();

    }

    void setStateIconFavorite(int pos, boolean isFavor) {

        items.get(pos).setFavorite(isFavor);

        notifyDataSetChanged();

    }

}