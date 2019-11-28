package com.teecoin.feature.couponSystem.user.discover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import static core.base.BaseApplication.getActiveActivity;

public class SlideBannerAdapter extends PagerAdapter {
    private ArrayList<DiscoverBannerModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public SlideBannerAdapter(Context context, ArrayList<DiscoverBannerModel> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_discover_banner, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImageView imageView = imageLayout.findViewById(R.id.iv_banner);
            DiscoverBannerModel item = listData.get(position);
            Glide.with(context).load(TCUtils.isEmpty(item.getBanner()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : item.getBanner()).apply(TCUtils.optionsSquareImage()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TCMainActivity)getActiveActivity()).submitBannerClick(item);
                }
            });
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
