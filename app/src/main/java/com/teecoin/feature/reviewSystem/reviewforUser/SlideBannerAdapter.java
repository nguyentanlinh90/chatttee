package com.teecoin.feature.reviewSystem.reviewforUser;

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
import com.teecoin.model.general.AppBannerModel;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.List;

public class SlideBannerAdapter extends PagerAdapter {
    private List<AppBannerModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public SlideBannerAdapter(Context context, List<AppBannerModel> listData) {
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
        View imageLayout = inflater.inflate(R.layout.item_image_banner_app, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImageView imageView = imageLayout.findViewById(R.id.iv_banner);
            AppBannerModel item = listData.get(position);
            Glide.with(context).load(TCUtils.isEmpty(item.getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : item.getUrl()).apply(TCUtils.optionsSquareImage()).into(imageView);
            imageLayout.setOnClickListener(v -> {
                ArrayList<MediaModel> listPhoto = new ArrayList<>();
//                for (int i = 0; i < listData.size(); i++) {
//                    listPhoto.add(new PhotoMediaModel(listData.get(i).getFeatured_image(), false, true, true));
//                }
//                listPhoto.add(new PhotoMediaModel(listData.get(position).getFeatured_image(), false, true, true));
//
//                DialogViewImageSliding dialogViewImageSliding = new DialogViewImageSliding(context, listPhoto, position);
//                dialogViewImageSliding.show();
                ((TCMainActivity) context).openCouponUserScreen();
            });
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
