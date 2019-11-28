package com.teecoin.feature.reviewSystem.photoMedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.reviewsystem.MediaModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DialogViewImageSliding extends TCBaseDialog {
    @BindView(R.id.dialog_view_image_sliding_iv_close)
    TextView iv_close;
    @BindView(R.id.view_pager)
    ViewPager view_pager;

    boolean isVideo = false;
    private Context context;
    private int position = 0;
    private List<MediaModel> listPhoto;

    public DialogViewImageSliding(Context context, ArrayList<MediaModel> listPhoto, int position) {
        super(context);
        this.listPhoto = listPhoto;
        this.context = context;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_images_slide);
    }

    @Override
    protected void initContentView() {
        setFullScreen(false);
        if (listPhoto != null && listPhoto.size() > 0) {
            isVideo = listPhoto.get(position).isVideo();

            view_pager.setAdapter(new SlidingImagesAdapter(context, listPhoto, isVideo));
            view_pager.setCurrentItem(position);
        }

    }

    @Override
    protected void onViewClick() {
        iv_close.setOnClickListener(v -> dismiss());
    }
}
