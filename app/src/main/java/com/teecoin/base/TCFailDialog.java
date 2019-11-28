package com.teecoin.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;

public class TCFailDialog extends TCBaseDialog {

    @BindView(R.id.dialog_fail_rl_container)
    View vContainer;

    @BindView(R.id.dialog_fail_iv_icon)
    ImageView iv_icon;

    @BindView(R.id.dialog_fail_tv_title)
    TextView tvTitle;

    @BindView(R.id.dialog_fail_tv_message)
    TextView tvMessage;

    @BindView(R.id.dialog_login_fail_tv_try_again)
    TextView tvTryAgain;

    private Drawable icon;
    private String title;
    private String message;
    private String textButton;


    public TCFailDialog(Context context, Drawable icon, String title, String message, String textButton) {
        super(context);
        this.icon = icon;
        this.title = title;
        this.message = message;
        this.textButton = textButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_fail);
    }

    @Override
    protected void initContentView() {

        tvTitle.setText(title);

        tvMessage.setText(Html.fromHtml(message));

        tvTryAgain.setText(textButton);

        if (icon != null) {
            iv_icon.setImageDrawable(icon);
        }

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        registerSingleClick(R.id.dialog_fail_rl_container, R.id.dialog_login_fail_tv_try_again);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.dialog_fail_rl_container:

                dismiss();

                break;

            case R.id.dialog_login_fail_tv_try_again:

                dismiss();

                break;

        }
    }
}
