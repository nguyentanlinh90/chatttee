package com.teecoin.feature.reviewSystem.userReviewShop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.feature.walletSystem.paymentThreshold.YesNoListener;

import butterknife.BindView;

public class DialogStatusReview extends TCBaseDialog {

    @BindView(R.id.dialog_status_review_rl_container)
    View vContainer;

    @BindView(R.id.button_no)
    TextView tvNo;

    @BindView(R.id.button_yes)
    TextView tvYes;

    private YesNoListener listener;

    DialogStatusReview(Context context, YesNoListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_status_review);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        tvYes.setOnClickListener(v -> dismiss());

        tvNo.setOnClickListener(view -> {
            listener.onCancel();
            dismiss();
        });
    }
}
