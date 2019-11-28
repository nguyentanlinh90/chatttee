package com.teecoin.base;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;

public class TCSuccessfulDialog extends TCBaseDialog {

    @BindView(R.id.dialog_successful_rl_container)
    View vContainer;

    @BindView(R.id.dialog_successful_iv_icon)
    ImageView iv_icon;

    @BindView(R.id.dialog_successful_tv_title)
    TextView tvTitle;

    @BindView(R.id.dialog_successful_tv_message)
    TextView tvMessage;

    @BindView(R.id.dialog_successful_tv_done)
    TextView tvTryAgain;
    TCConfirmListener tcConfirmListener;
    private String title;
    private String message;
    private String textButton;


    public TCSuccessfulDialog(Context context, String title, String message, String textButton, TCConfirmListener tcConfirmListener) {
        super(context);
        this.title = title;
        this.message = message;
        this.textButton = textButton;
        this.tcConfirmListener = tcConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_successful);
    }

    @Override
    protected void initContentView() {
        tvTitle.setText(title);
        tvMessage.setText(Html.fromHtml(message));
        tvTryAgain.setText(textButton);
        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {
        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());
        registerSingleClick(R.id.dialog_successful_rl_container, R.id.dialog_successful_tv_done);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.dialog_successful_rl_container:
            case R.id.dialog_successful_tv_done:
                dismiss();
                if (null != tcConfirmListener) {
                    tcConfirmListener.onConfirmed(v.getId(), null);
                }
                break;
        }
    }
}
