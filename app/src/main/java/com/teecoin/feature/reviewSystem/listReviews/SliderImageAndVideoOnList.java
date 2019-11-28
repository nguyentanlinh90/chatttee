package com.teecoin.feature.reviewSystem.listReviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.ImagesResponseModel;
import com.teecoin.utils.TCUtils;

import java.util.List;

public class SliderImageAndVideoOnList extends PagerAdapter {
    private List<ImagesResponseModel> listData;
    private LayoutInflater inflater;
    private Context context;
    private SlideImageListener listener;

    public SliderImageAndVideoOnList(Context context, List<ImagesResponseModel> listData, SlideImageListener listener) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_slide_image_video_on_list, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImagesResponseModel item = listData.get(position);
            ImageView imageView = imageLayout.findViewById(R.id.item_list_review_iv_photo);
            ImageView iv_play_video = imageLayout.findViewById(R.id.item_list_review_iv_play_video);
            iv_play_video.setVisibility(TCUtils.isEmpty(item.getVideo_url()) ? View.GONE : View.VISIBLE);
            Glide.with(context).load(item.getUrl()).apply(TCUtils.optionsSquareImage().diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageView);
            imageView.setOnClickListener(v -> listener.onClickPosition(position));
        }
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
