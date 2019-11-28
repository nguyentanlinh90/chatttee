package com.teecoin.feature.general.account;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;


public class AskLogoutDialog extends TCBaseDialog {

    @BindView(R.id.tv_bt_left)
    TextView tvCancel;

    @BindView(R.id.tv_bt_right)
    TextView tvYes;

    private TCConfirmListener confirm_listener;

    AskLogoutDialog(Context context, TCConfirmListener confirm_listener) {
        super(context);
        this.confirm_listener = confirm_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ask_logout);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);

        tvCancel.setText(TCUtils.getString(R.string.text_cancel));

        tvYes.setText(TCUtils.getString(R.string.text_yes));
    }

    @Override
    protected void onViewClick() {

        tvCancel.setOnClickListener(v -> dismiss());

        tvYes.setOnClickListener(v -> logout());
    }

    private void logout() {

        dismiss();

        confirm_listener.onConfirmed(AccountScreen.AccountAccess.ACCOUNT_LOGOUT.getValue(), null);
    }
}
