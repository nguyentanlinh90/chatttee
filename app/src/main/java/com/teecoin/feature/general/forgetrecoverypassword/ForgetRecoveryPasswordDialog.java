package com.teecoin.feature.general.forgetrecoverypassword;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ForgetRecoveryPasswordDialog extends TCBaseDialog {

    @BindView(R.id.tv_bt_left)
    TextView tvCancel;

    @BindView(R.id.tv_bt_right)
    TextView tvGetCode;

    private TCDecisionListener listener;

    ForgetRecoveryPasswordDialog(Context context, TCDecisionListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_forgot_password);
    }

    @Override
    protected void initContentView() {

        tvCancel.setText(TCUtils.getString(R.string.text_cancel));

        tvGetCode.setText(TCUtils.getString(R.string.get_code));

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {
        registerSingleClick(tvCancel, tvGetCode);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.tv_bt_left:
                closeDialog();
                break;
            case R.id.tv_bt_right:
                getVerificationCode();
                break;
        }

    }

    private void closeDialog() {
        dismiss();
    }

    private void getVerificationCode() {

        dismiss();

        listener.onPositiveButtonClicked(View.NO_ID, null);
    }

}
