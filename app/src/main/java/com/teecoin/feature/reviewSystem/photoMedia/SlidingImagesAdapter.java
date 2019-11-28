package com.teecoin.feature.reviewSystem.photoMedia;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.utils.TCUtils;

import java.util.List;

public class SlidingImagesAdapter extends PagerAdapter {
    private List<MediaModel> listPhoto;
    private LayoutInflater inflater;
    private Context context;
    private boolean from_video;

    public SlidingImagesAdapter(Context context, List<MediaModel> listPhoto, boolean from_video) {
        this.context = context;
        this.listPhoto = listPhoto;
        this.from_video = from_video;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_view_zoom_images, view, false);
        if (imageLayout != null) {
            final ImageView imageView = imageLayout.findViewById(R.id.iv_photo);
            ImageView icPlayVideo = imageLayout.findViewById(R.id.item_view_zoom_images_iv_play_video_icon);
            MediaModel photoMediaModel = listPhoto.get(position);
            Glide.with(context).load(photoMediaModel.getPath()).into(imageView);
            //icPlayVideo.setVisibility(photoMediaModel.isVideo() ? View.VISIBLE : View.GONE);
            //imageView.setOnClickListener(v -> playVideo(photoMediaModel));

            ImageView iv_play_video = imageLayout.findViewById(R.id.item_list_review_iv_play_video);
            ImageView iv_cover_video = imageLayout.findViewById(R.id.iv_cover_video);
            VideoView video_view = imageLayout.findViewById(R.id.item_list_review_video_view);
            video_view.setBackgroundColor(Color.BLACK);
            RelativeLayout rl_play_video = imageLayout.findViewById(R.id.item_list_review_rl_play_video);
            RelativeLayout rl_parent_images = imageLayout.findViewById(R.id.rl_parent_images);
            RelativeLayout progress_bar = imageLayout.findViewById(R.id.rl_progress_bar);
            progress_bar.setVisibility(View.GONE);
            rl_play_video.setVisibility(View.GONE);
            if (photoMediaModel.isVideo()) {
                rl_play_video.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                if (video_view.isPlaying()) {
                    video_view.pause();
                    video_view.stopPlayback();
                }
                if (from_video) {
                    from_video = false;
                    // playVideo(photoMediaModel.getPath(),video_view,progress_bar,iv_cover_video,iv_play_video);
                    TCUtils.playVideoReview(photoMediaModel.getPath(), video_view, progress_bar, iv_cover_video, iv_play_video);

                }
                Glide.with(context).load(photoMediaModel.getPath()).apply(TCUtils.optionsSquareImage()).into(iv_cover_video);

            } else {
                imageView.setVisibility(View.VISIBLE);
                rl_play_video.setVisibility(View.GONE);
                if (video_view.isPlaying()) {
                    video_view.pause();
                    video_view.stopPlayback();
                }
            }
            iv_play_video.setOnClickListener(v -> TCUtils.playVideoReview(photoMediaModel.getPath(), video_view, progress_bar, iv_cover_video, iv_play_video));
        }
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
