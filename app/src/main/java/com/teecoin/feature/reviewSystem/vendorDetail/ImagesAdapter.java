package com.teecoin.feature.reviewSystem.vendorDetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.List;

import core.view.RecycleListener;

public class ImagesAdapter extends PagerAdapter {
    private List<ImageModel> listImages;
    private LayoutInflater inflater;
    private Context context;
    private RecycleListener<ImageModel> listener;


    ImagesAdapter(Context context, List<ImageModel> listImages, RecycleListener<ImageModel> listener) {
        this.context = context;
        this.listImages = listImages;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_view_image_slide, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImageView imageView = imageLayout.findViewById(R.id.iv_photo);
            ImageModel item = listImages.get(position);
            Glide.with(context).load(TCUtils.isEmpty(item.getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : item.getUrl()).apply(TCUtils.optionsSquareImage()).into(imageView);
            imageLayout.setOnClickListener(v -> listener.onItemClick(view, item, position, EnumMgr.ClickType.None));
        }
        return imageLayout;
    }
}