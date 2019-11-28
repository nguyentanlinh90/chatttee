package com.teecoin.feature.general.loginwithemail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class LoginResetPasswordDialog extends TCBaseDialog {

    @BindView(R.id.button_ok_tv_ok)
    TextView tvGotIt;

    @BindView(R.id.dialog_login_reset_password_tv_email)
    TextView tvEmail;

    private String email;

    public LoginResetPasswordDialog(Context context, String email) {
        super(context);

        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_login_reset_password);
    }

    @Override
    protected void initContentView() {

        tvGotIt.setText(TCUtils.getString(R.string.text_got_it));

        tvEmail.setText(email);

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {
        registerSingleClick(tvGotIt);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.button_ok_tv_ok:

                dismiss();

                break;
        }
    }
}
