package com.teecoin.feature.general.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCDecisionListener;

import butterknife.BindView;

public class EnableEmailLoginDialog extends TCBaseDialog {

    @BindView(R.id.dialog_login_fail_rl_container)
    View vContainer;

    private TCDecisionListener tcDecisionListener;

    public EnableEmailLoginDialog(Context context, TCDecisionListener tcDecisionListener) {
        super(context);

        this.tcDecisionListener = tcDecisionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enable_email_login);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);

    }

    @Override
    protected void onViewClick() {

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        registerSingleClick(R.id.dialog_login_fail_rl_container, R.id.dialog_login_fail_tv_import_wallet);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.dialog_login_fail_rl_container:

                dismiss();

                break;

            case R.id.dialog_login_fail_tv_import_wallet:

                tcDecisionListener.onPositiveButtonClicked(View.NO_ID, null);

                dismiss();

                break;
        }
    }

}
