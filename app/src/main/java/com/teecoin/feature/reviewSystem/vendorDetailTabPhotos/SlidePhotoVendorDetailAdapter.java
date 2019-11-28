package com.teecoin.feature.reviewSystem.vendorDetailTabPhotos;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.animation.TCTouchZooImageView;
import com.teecoin.feature.reviewSystem.listReviews.SlideImageListener;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static core.base.BaseApplication.getActiveActivity;

public class SlidePhotoVendorDetailAdapter extends PagerAdapter {
    private ArrayList<ImagesVendor> listData;
    private LayoutInflater inflater;
    private Context context;
   // private SlideImageListener listener;
    private boolean zoom;
    private TCTouchZooImageView imageView_zoom;
    private boolean from_video;
    private int sizeImageHeight;
    private int sizeImageWidth;
    public SlidePhotoVendorDetailAdapter(Context context, ArrayList<ImagesVendor> listImages, boolean zoom, boolean from_video) {
        this.context = context;
        this.listData = listImages;
        inflater = LayoutInflater.from(context);
        this.zoom = zoom;
        this.from_video = from_video;
        this.sizeImageWidth = (TCScreenSize.getWidth(getActiveActivity()));
        this.sizeImageHeight = sizeImageWidth;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_photo_vendor_slider, view, false);
//        ButterKnife.bind(this, view);
        // View page = inflater.inflate(R.layout.item_images_list_review, null);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImagesVendor item = listData.get(position);
            ImageView imageView = imageLayout.findViewById(R.id.item_list_review_iv_photo);

            ImageView iv_play_video = imageLayout.findViewById(R.id.item_list_review_iv_play_video);
            ImageView iv_cover_video = imageLayout.findViewById(R.id.iv_cover_video);
            VideoView video_view = imageLayout.findViewById(R.id.item_list_review_video_view);
            video_view.setBackgroundColor(Color.BLACK);
            RelativeLayout rl_play_video = imageLayout.findViewById(R.id.item_list_review_rl_play_video);
            RelativeLayout rl_parent_images = imageLayout.findViewById(R.id.rl_parent_images);
            RelativeLayout progress_bar = imageLayout.findViewById(R.id.rl_progress_bar);


            progress_bar.setVisibility(View.GONE);
            rl_play_video.setVisibility(View.GONE);
            imageView_zoom = imageLayout.findViewById(R.id.item_list_review_iv_photo_zoom);
            ViewGroup.LayoutParams params = rl_play_video.getLayoutParams();
            params.width = sizeImageWidth;
            params.height = sizeImageHeight;
            rl_play_video.setLayoutParams(params);
            if (zoom) {
                imageView_zoom.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            } else {
                imageView_zoom.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            TCUtils.checkURLisVideo(item.getUrl());
            if(TCUtils.checkURLisVideo(item.getUrl())){
                rl_play_video.setVisibility(View.VISIBLE);
                rl_parent_images.setVisibility(View.GONE);
                if (video_view.isPlaying()) {
                    video_view.pause();
                    video_view.stopPlayback();
                }
                  if (from_video) {
                    from_video = false;
                    TCUtils.playVideoReview(item.getUrl(), video_view, progress_bar, iv_cover_video, iv_play_video);
                }
                //Glide.with(context).load(item.getThumbnail_500()).apply(TCUtils.optionsSquareImage()).into(iv_cover_video);
                Glide.with(context).load(item.getUrl()).apply(TCUtils.optionsSquareImage()).into(iv_cover_video);
            }else{
                //Glide.with(getActiveActivity()).load(item.getThumbnail_500()).apply(TCUtils.optionsSquareImage()).into(zoom ? imageView_zoom : imageView);
                Glide.with(getActiveActivity()).load(item.getUrl()).apply(TCUtils.optionsSquareImage()).into(zoom ? imageView_zoom : imageView);
                rl_parent_images.setVisibility(View.VISIBLE);
                rl_play_video.setVisibility(View.GONE);
                    if (video_view.isPlaying()) {
                    video_view.pause();
                    video_view.stopPlayback();
                }
            }
            iv_play_video.setOnClickListener(v -> TCUtils.playVideoReview(item.getUrl(), video_view, progress_bar, iv_cover_video, iv_play_video));

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
