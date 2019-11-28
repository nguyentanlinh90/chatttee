package com.teecoin.feature.walletSystem.recoveryPassword;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ResetPasswordSuccessDialog extends TCBaseDialog {
    @BindView(R.id.ll_bt_ok)
    View bt_ok;
    @BindView(R.id.tv_content)
    TextView tv_content;
    private String email;

    public ResetPasswordSuccessDialog(Context context, String email) {
        super(context);
        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reset_password_sucesss);
    }

    @Override
    protected void initContentView() {
        tv_content.setText(String.format("%s\n%s", TCUtils.getString(R.string.reset_password_sent_new_password), email));
    }

    @Override
    protected void onViewClick() {
        bt_ok.setOnClickListener(v -> dismiss());
    }
}
