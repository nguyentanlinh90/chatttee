package com.teecoin.base;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;

public class TCBaseAlertDialog extends TCBaseDialog {

    @BindView(R.id.rl_container)
    View vContainer;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_button)
    TextView tvButton;

    private String title;
    private String message;
    private String textButton;
    private TCConfirmListener tcConfirmListener;

    public TCBaseAlertDialog(Context context, String title, String message, String textButton, TCConfirmListener tcConfirmListener) {
        super(context);
        this.title = title;
        this.message = message;
        this.textButton = textButton;
        this.tcConfirmListener = tcConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_alert_1_button_transparent_bg);
    }

    @Override
    protected void initContentView() {
        tvTitle.setText(title);
        tvMessage.setText(message);
        tvButton.setText(textButton);
        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {
        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());
        registerSingleClick(R.id.rl_container, R.id.tv_button);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.rl_container:
            case R.id.tv_button:
                tcConfirmListener.onConfirmed(1, null);
                dismiss();
                break;
        }
    }
}
