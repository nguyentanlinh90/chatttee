package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.EnumMgr;

import butterknife.BindView;

public class MediaOptionDialog extends TCBaseDialog {
    @BindView(R.id.tv_library)
    TextView tv_library;
    @BindView(R.id.tv_photo)
    TextView tv_photo;
    @BindView(R.id.tv_video)
    TextView tv_video;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    private TCConfirmListener listener;
    private boolean hasVideo;

    public MediaOptionDialog(Context context, boolean hasVideo, TCConfirmListener listener) {
        super(context);
        this.listener = listener;
        this.hasVideo = hasVideo;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_media_option);
    }

    @Override
    protected void initContentView() {
        tv_video.setVisibility(hasVideo ? View.GONE : View.VISIBLE);
        tv_cancel.setOnClickListener(v -> dismiss());
        tv_library.setOnClickListener(v -> libListener());
        tv_photo.setOnClickListener(v -> capturePhoto());
        tv_video.setOnClickListener(v -> captureVideo());
    }

    @Override
    protected void onViewClick() {

    }

    private void libListener() {
        listener.onConfirmed(EnumMgr.PhotoMediaFrom.Library.getValue(), "");
        dismiss();
    }

    private void capturePhoto() {
        listener.onConfirmed(EnumMgr.PhotoMediaFrom.CapturePhoto.getValue(), "");
        dismiss();
    }

    private void captureVideo() {
        listener.onConfirmed(EnumMgr.PhotoMediaFrom.CaptureVideo.getValue(), "");
        dismiss();
    }


}
