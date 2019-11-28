package com.teecoin.feature.walletSystem.recoveryPassword;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;

import butterknife.BindView;

public class ConfirmResetPasswordDialog extends TCBaseDialog {
    @BindView(R.id.ll_bt_cancel)
    View bt_cancel;
    @BindView(R.id.ll_bt_ok)
    View bt_ok;
    private ConfirmResetPassword resetPasswordListener;

    public ConfirmResetPasswordDialog(Context context, ConfirmResetPassword resetPasswordListener) {
        super(context);
        this.resetPasswordListener = resetPasswordListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_reset_password);
    }

    @Override
    protected void initContentView() {
        bt_cancel.setOnClickListener(v -> dismiss());
        bt_ok.setOnClickListener(v -> {
            resetPasswordListener.onReset();
            dismiss();
        });
    }

    @Override
    protected void onViewClick() {

    }
}
