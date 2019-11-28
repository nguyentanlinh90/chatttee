package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;

import butterknife.BindView;

public class MessageBaseScreen extends TCBaseDialog {
    @BindView(R.id.base_notify_tv_content)
    TextView tv_content;
    @BindView(R.id.base_notify_tv_ok)
    TextView tv_ok;
    private String contentMSG;

    public MessageBaseScreen(Context context, String contentMSG) {
        super(context);
        this.contentMSG = contentMSG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_dialog_notification);
    }

    @Override
    protected void initContentView() {
        tv_content.setText(contentMSG);
    }

    @Override
    protected void onViewClick() {
        tv_ok.setOnClickListener(v -> dismiss());
    }
}
