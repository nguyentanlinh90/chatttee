package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.TipResponseModel;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipResultScreen extends LinearLayout {

    @BindView(R.id.view_tip_result_tv_transaction_id)
    TextView tv_transaction_id;
    @BindView(R.id.view_tip_result_tv_total_amount)
    TextView tv_total_amount;
    @BindView(R.id.view_tip_result_tv_close)
    TextView tv_close;
    private TipEventListener listener;
    private TipResponseModel tipResponseModel;

    public TipResultScreen(Context context, TipResponseModel tipResponseModel, TipEventListener listener) {
        super(context);
        this.listener = listener;
        this.tipResponseModel = tipResponseModel;
        init();
    }

    private TipResultScreen(Context context) {
        super(context);
        init();
    }

    private TipResultScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private TipResultScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TipResultScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        View view = inflate(getContext(), R.layout.view_tip_result, this);
        ButterKnife.bind(TipResultScreen.this, view);
        initView();
        initClickEvent();
        initData();
    }

    private void initView() {
        tv_transaction_id.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.transaction_success_you_can_track),
                        tipResponseModel.getTransactionId())));
        tv_total_amount.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), tipResponseModel.getTotalAmount()));
    }

    private void initData() {

    }

    private void initClickEvent() {
        tv_close.setOnClickListener(v -> close());
    }

    private void close() {
        if (listener != null) {
            listener.onClose();
        }
    }
}
