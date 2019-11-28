package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThankForLikeScreen extends LinearLayout {

    @BindView(R.id.view_thank_for_like_tv_close)
    TextView view_thank_for_like_tv_close;
    private TipEventListener listener;

    public ThankForLikeScreen(Context context, TipEventListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    private ThankForLikeScreen(Context context) {
        super(context);
        init();
    }

    private ThankForLikeScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private ThankForLikeScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ThankForLikeScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        View view = inflate(getContext(), R.layout.view_thank_for_like, this);
        ButterKnife.bind(ThankForLikeScreen.this, view);
        initView();
        initClickEvent();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void initClickEvent() {
        view_thank_for_like_tv_close.setOnClickListener(v -> close());
    }

    private void close() {
        if (listener != null) {
            listener.onClose();
        }
    }
}
