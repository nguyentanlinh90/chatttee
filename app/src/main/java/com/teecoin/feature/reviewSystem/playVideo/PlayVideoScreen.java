package com.teecoin.feature.reviewSystem.playVideo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCBaseActivity;
import com.teecoin.model.reviewsystem.ImagesResponseModel;

import butterknife.BindView;


public class PlayVideoScreen extends TCBaseActivity {
    @BindView(R.id.item_list_review_video_view)
    VideoView video_view;
    @BindView(R.id.activity_play_video_iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_cover_video)
    ImageView iv_cover_video;
    @BindView(R.id.item_list_review_iv_play_video)
    ImageView iv_play_video;

    @BindView(R.id.rlt_progress_view_base)
    View progress_bar;

    private ImagesResponseModel video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
    }

    @Override
    public void onBindView() {
        Bundle bundle = getIntent().getExtras();
        progress_bar.setVisibility(View.GONE);
        iv_cover_video.setVisibility(View.GONE);
        if (bundle != null) {
            video = (ImagesResponseModel) bundle.getSerializable(ImagesResponseModel.TAG_VIDEO);
        }
        loadVideo();
        onClick();
    }

    private void loadVideo() {
        if (video != null) {
            iv_play_video.setVisibility(View.GONE);
            //  TCLog.e("link "+video.getLink_video());
            Uri uri = Uri.parse(video.getVideo_url());
            video_view.setVideoURI(uri);
            video_view.start();
            progress_bar.setVisibility(View.VISIBLE);
            iv_cover_video.setVisibility(View.GONE);
            video_view.setOnPreparedListener(mp -> {
                mp.start();
                mp.setOnVideoSizeChangedListener((mp1, arg1, arg2) -> {
                    progress_bar.setVisibility(View.GONE);
                    mp1.start();
                });
            });
            video_view.setOnCompletionListener(mp -> {
                iv_cover_video.setVisibility(View.VISIBLE);
                iv_play_video.setVisibility(View.VISIBLE);
                Glide.with(getActiveActivity()).load(video.getVideo_url()).into(iv_cover_video);
            });
        }
    }

    private void onClick() {
        iv_back.setOnClickListener(v -> finish());
        iv_play_video.setOnClickListener(v -> {
            loadVideo();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (video_view != null) {
            video_view.stopPlayback();
        }
    }
}
