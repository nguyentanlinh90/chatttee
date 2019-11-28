package com.teecoin.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.teecoin.R;
import com.teecoin.utils.TCUtils;


public class TCSwipeRefreshLayout extends SwipeRefreshLayout {

    private static final int MAX_ALPHA = 255;

    private static final int CIRCLE_DIAMETER = 40;

    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
//    private CircleImageView circle;
//    private MaterialProgressDrawable progress;
    private ProgressBar progressBar;
    private Animation in;
    private Animation out;

    public TCSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public TCSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return !isRefreshing() && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    private void init() {
        setEnablePagingProgress(true);
//        setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        setColorSchemeColors(TCUtils.getColor(R.color.c_a08220));
        in = AnimationUtils.loadAnimation(getContext(), R.anim.scale_bottom_in);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.scale_bottom_out);
        in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.setVisibility(VISIBLE);
//                progress.start();
//                circle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(GONE);
//                circle.clearAnimation();
            }

        });
        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(GONE);
//                progress.stop();
//                circle.clearAnimation();
//                circle.setVisibility(View.GONE);
            }

        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        createProgressView();
    }

    @Override
    public void setColorSchemeColors(int... colors) {
        super.setColorSchemeColors(colors);
//        if (progress != null)
//            progress.setColorSchemeColors(colors);
    }

    private void createProgressView() {
        RelativeLayout layout = new RelativeLayout(getContext());
        progressBar = new ProgressBar(getContext());
        progressBar.setVisibility(GONE);

//        progress = new MaterialProgressDrawable(getContext(), this);
//        progress.setBackgroundColor(CIRCLE_BG_LIGHT);
//        progress.setAlpha(MAX_ALPHA);
//        circle = new CircleImageView(getContext(), CIRCLE_BG_LIGHT, CIRCLE_DIAMETER / 2);
//        circle.setImageDrawable(progress);
//        circle.setVisibility(View.GONE);

        View original = getChildAt(1);
        removeViewAt(1);
        layout.addView(original, 0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        layout.addView(circle, 1, params);
        layout.addView(progressBar,params);
        addView(layout, 1);

        // disable ic_refresh progress when initialized
//        if (circle != null)
//            circle.setAlpha(0);
    }


    public void setEnablePagingProgress(boolean enable) {
//        if (circle != null)
//            circle.setAlpha(enable ? 1 : 0);
    }

    public void startLoadingAnimation() {
        progressBar.setVisibility(VISIBLE);
//        if (circle != null && circle.getVisibility() == View.GONE)
//            circle.startAnimation(in);
    }

    public void stopLoadingAnimation() {
        progressBar.setVisibility(GONE);
//        if (circle != null && circle.getVisibility() == View.VISIBLE)
//            circle.startAnimation(out);
    }

    public void setEnableRefreshProgress(boolean enable) {
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            if (child != null && child.getClass().getName().equals("android.support.v4.widget.CircleImageView"))
                child.setAlpha(enable ? 1 : 0);
        }
    }
}
