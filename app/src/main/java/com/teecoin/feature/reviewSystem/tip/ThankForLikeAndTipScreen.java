package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.teecoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThankForLikeAndTipScreen extends LinearLayout {
    @BindView(R.id.view_thank_for_like_and_tip_tv_cancel)
    View tv_cancel;
    @BindView(R.id.view_thank_for_like_and_tip_tv_ok)
    View tv_ok;

    private TipEventListener listener;

    public ThankForLikeAndTipScreen(Context context, TipEventListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    private ThankForLikeAndTipScreen(Context context) {
        super(context);
        init();
    }

    private ThankForLikeAndTipScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private ThankForLikeAndTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ThankForLikeAndTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        View view = inflate(getContext(), R.layout.view_thank_for_like_and_tip, this);
        ButterKnife.bind(ThankForLikeAndTipScreen.this, view);
        initView();
        initClickEvent();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void initClickEvent() {
        tv_cancel.setOnClickListener(v -> close());
        tv_ok.setOnClickListener(v -> openInputTip());
    }

    private void close() {
        if (listener != null) {
            listener.onClose();
        }
    }

    private void openInputTip() {
        if (listener != null) {
            listener.onOkNext();
        }
    }
}
