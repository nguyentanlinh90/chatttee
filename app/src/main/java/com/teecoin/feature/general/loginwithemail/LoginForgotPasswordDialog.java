package com.teecoin.feature.general.loginwithemail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class LoginForgotPasswordDialog extends TCBaseDialog {

    @BindView(R.id.dialog_login_forgot_password_rl_container)
    View vContainer;

    @BindView(R.id.tv_bt_right)
    TextView tvYes;

    private TCDecisionListener listener;

    public LoginForgotPasswordDialog(Context context, TCDecisionListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_login_forgot_password);
    }

    @Override
    protected void initContentView() {

        tvYes.setText(TCUtils.getString(R.string.text_yes));

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        registerSingleClick(R.id.tv_bt_left, R.id.tv_bt_right, R.id.dialog_login_forgot_password_rl_container);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.tv_bt_right:

                confirmResetPass();

                break;

            case R.id.tv_bt_left:

                closeDialog();

                break;

            case R.id.dialog_login_forgot_password_rl_container:

                dismiss();

                break;
        }
    }

    private void closeDialog() {
        dismiss();
    }

    private void confirmResetPass() {

        listener.onPositiveButtonClicked(View.NO_ID, null);

        closeDialog();

    }
}
