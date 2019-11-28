package com.teecoin.feature.general.changeRecoveryPassword;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogConfirmRecoveryPassword extends TCBaseDialog {
    @BindView(R.id.ll_bt_cancel)
    View ll_bt_cancel;
    @BindView(R.id.ll_bt_ok)
    View ll_bt_ok;
    @BindView(R.id.dialog_confirm_password_et_pass)
    EditText et_pass;
    private String getPassword;
    private TCConfirmListener listener;

    public DialogConfirmRecoveryPassword(Context context, String getPassword, TCConfirmListener listener) {
        super(context);
        this.getPassword = getPassword;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_recovery_password);
    }

    @Override
    protected void initContentView() {

    }

    @Override
    protected void onViewClick() {
        ll_bt_cancel.setOnClickListener(v -> dismiss());
        ll_bt_ok.setOnClickListener(v -> validate());
    }

    private void validate() {
        if (TCUtils.isEmpty(et_pass.getText().toString())) {
            et_pass.setError(TCUtils.getString(R.string.please_enter_your_password));
            return;
        }
        if (!et_pass.getText().toString().equals(getPassword)) {
            et_pass.setError(TCUtils.getString(R.string.incorrect_recovery_password));
            return;
        }
        listener.onConfirmed(1, "success");
        dismiss();
    }
}
