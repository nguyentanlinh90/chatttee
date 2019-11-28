package com.teecoin.feature.reviewSystem.userReviewShop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.SessionType;
import com.sokolov.androidsizes.ISize;
import com.sokolov.androidsizes.SizeFromVideoFile;
import com.teecoin.R;
import com.teecoin.utils.CameraUtils;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;

import java.io.File;
import java.io.IOException;

public class CaptureVideoActivity extends AppCompatActivity {

    public static final String VIDEO_PATH_INTENT = "VIDEO_PATH_INTENT";
    public static final String CROP_VIDEO_SIZE_INTENT = "CROP_SIZE_INTENT";
    private static final int PROGRESS_TOTAL_TIME = TCConstant.MAX_VIDEO_RECORD_TIME + TCConstant.ONE_SECOND_IN_MILLISECOND;
    private static final int PROGRESS_STEP = TCConstant.MAX_VIDEO_RECORD_TIME / TCConstant.ONE_SECOND_IN_MILLISECOND;
    private TCSquareCameraView camera;
    private ImageView iv_record_video;
    private ImageView iv_cancel;
    private ProgressBar pb_progress_record_time;
    private TextView tv_progress_count;
    private CountDownTimer countDownTimer;
    private boolean isRecording = false;
    private int stepIndex = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);
        init();
    }

    private void init() {
        isRecording = false;
        stepIndex = 0;
        camera = findViewById(R.id.activity_capture_view_scv_camera);
        pb_progress_record_time = findViewById(R.id.activity_capture_view_pb_progress_record_time);
        iv_record_video = findViewById(R.id.activity_capture_view_iv_record_video);
        tv_progress_count = findViewById(R.id.activity_capture_view_tv_progress_count);
        iv_cancel = findViewById(R.id.activity_capture_view_iv_cancel);
        pb_progress_record_time.setVisibility(View.INVISIBLE);

        camera.setLifecycleOwner(this);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
                TCLog.d("videoTaken:" + video.getAbsolutePath());
                finishAndBackToApp(video);
            }
        });

        iv_record_video.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
            } else {
                stopRecording();
            }
        });
        iv_cancel.setOnClickListener(v -> {
            stopRecording();
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    private void startRecording() {
        Glide.with(this).load(R.drawable.ic_stop_record_video).into(iv_record_video);
        pb_progress_record_time.setVisibility(View.VISIBLE);
        captureVideo();
        countDownProgress();
        isRecording = true;
    }

    private void stopRecording() {
        camera.stopCapturingVideo();
        isRecording = false;
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    private void captureVideo() {
        camera.setSessionType(SessionType.VIDEO);
        File photoFile = null;
        try {
            photoFile = CameraUtils.createVideoFileWith();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setVideoMaxDuration(TCConstant.MAX_VIDEO_RECORD_TIME);
        camera.startCapturingVideo(photoFile);
    }

    private void countDownProgress() {
        countDownTimer = new CountDownTimer(PROGRESS_TOTAL_TIME, TCConstant.ONE_SECOND_IN_MILLISECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                pb_progress_record_time.setProgress(stepIndex * PROGRESS_STEP);
                tv_progress_count.setText(String.valueOf(stepIndex));
                stepIndex++;
            }

            @Override
            public void onFinish() {
                tv_progress_count.setText(String.valueOf(stepIndex));
                pb_progress_record_time.setProgress(100);
            }
        };
        countDownTimer.start();
    }

    private void finishAndBackToApp(File video) {
        try {
            ISize size = new SizeFromVideoFile(video.getAbsolutePath());
            TCLog.d("hung width:" + size.width());
            TCLog.d("hung height:" + size.height());
            Intent returnIntent = new Intent();
            returnIntent.putExtra(VIDEO_PATH_INTENT, video.getAbsolutePath());
            returnIntent.putExtra(CROP_VIDEO_SIZE_INTENT, String.valueOf(Math.min(size.width(), size.height())));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } catch (Exception e) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}
