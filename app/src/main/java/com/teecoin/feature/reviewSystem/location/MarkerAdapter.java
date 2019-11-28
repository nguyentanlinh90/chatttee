package com.teecoin.feature.reviewSystem.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static core.base.BaseApplication.getActiveActivity;

public class MarkerAdapter extends PagerAdapter {
    private ArrayList<VendorModel> markerList;
    private LayoutInflater inflater;

    public MarkerAdapter(Context context, ArrayList<VendorModel> markerList) {
        this.markerList = markerList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return markerList.size();

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View viewHolder = inflater.inflate(R.layout.item_marker, view, false);

        view.addView(viewHolder, 0);
        if (viewHolder != null) {
            ImageView detail_marker_iv_image = viewHolder.findViewById(R.id.detail_marker_iv_image);
            TextView detail_marker_tv_name = viewHolder.findViewById(R.id.detail_marker_tv_name);
            TextView detail_marker_tv_address = viewHolder.findViewById(R.id.detail_marker_tv_address);
            TextView detail_marker_tv_cuisine = viewHolder.findViewById(R.id.detail_marker_tv_cuisine);
            TextView detail_marker_tv_open_status = viewHolder.findViewById(R.id.detail_marker_tv_open_status);
            TextView detail_marker_tv_total_rate = viewHolder.findViewById(R.id.detail_marker_tv_total_rate);
            MaterialRatingBar detail_marker_rb_rating = viewHolder.findViewById(R.id.detail_marker_rb_rating);
            detail_marker_rb_rating.setIsIndicator(true);

            VendorModel item = markerList.get(position);

            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(item.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) : item.getFeaturedImage()).apply(RequestOptions.circleCropTransform()).into(detail_marker_iv_image);
            detail_marker_tv_name.setText(item.getName());
            detail_marker_tv_address.setText(item.getAddress());

            if (item.getCuisines() != null && item.getCuisines().size() > 0) {
                StringBuilder cuisine = new StringBuilder();
                for (int i = 0; i < item.getCuisines().size(); i++) {
                    if (i != item.getCuisines().size() - 1)
                        cuisine.append(item.getCuisines().get(i).getName()).append(", ");
                    else cuisine.append(item.getCuisines().get(i).getName()).append(".");
                }
                detail_marker_tv_cuisine.setText(cuisine.toString());
            }
            if (TCConstant.VENDOR_UNKNOWN.equals(item.getOpenStatus())) {
                detail_marker_tv_open_status.setVisibility(View.GONE);
            } else {
                detail_marker_tv_open_status.setVisibility(View.VISIBLE);
                detail_marker_tv_open_status.setText((CharSequence) item.getOpenStatus());
            }

            detail_marker_tv_total_rate.setText(item.getReviewCount() + " Reviews");
            detail_marker_rb_rating.setRating(item.getRating());
        }
        return viewHolder;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}