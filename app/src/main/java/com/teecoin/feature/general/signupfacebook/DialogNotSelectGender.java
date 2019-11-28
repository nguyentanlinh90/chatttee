package com.teecoin.feature.general.signupfacebook;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogNotSelectGender extends TCBaseDialog {

    @BindView(R.id.dialog_not_select_gender_rl_container)
    View vContainer;

    @BindView(R.id.ll_bt_ok)
    View vGotIt;

    @BindView(R.id.button_ok_tv_ok)
    TextView tvGotIt;

    public DialogNotSelectGender(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_not_select_gender);
    }

    @Override
    protected void initContentView() {

        tvGotIt.setText(TCUtils.getString(R.string.text_got_it));

        setFullScreen(true);
    }

    @Override
    protected void onViewClick() {

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        vGotIt.setOnClickListener(v -> dismiss());

    }
}
