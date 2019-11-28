package com.teecoin.feature.general.signupfacebook;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogSignUpFail extends TCBaseDialog {

    @BindView(R.id.dialog_signup_fail_v_container)
    View vContainer;

    @BindView(R.id.dialog_signup_fail_tv_title)
    TextView tvTitle;

    @BindView(R.id.dialog_signup_fail_tv_message)
    TextView tvMessage;

    @BindView(R.id.dialog_signup_fail_tv_login_by)
    TextView tvLoginBy;

    @BindView(R.id.dialog_signup_fail_iv_icon)
    ImageView iv_icon;


    private String title;
    private String message;
    private String textButton;
    private EnumMgr.SignUpType signUpType;// check show dialog on the steps signup or signin
    private SignUpFailListener signUpFailListener;
    private boolean isLoginFail;

    public DialogSignUpFail(Context context, String title, String message, EnumMgr.SignUpType signUpType, SignUpFailListener signUpFailListener) {
        super(context);
        this.title = title;
        this.message = message;
        this.signUpType = signUpType;
        this.signUpFailListener = signUpFailListener;
    }

    public DialogSignUpFail(Context context, String title, String message, EnumMgr.SignUpType signUpType, boolean isLoginFail, SignUpFailListener signUpFailListener) {
        super(context);
        this.title = title;
        this.message = message;
        this.signUpType = signUpType;
        this.isLoginFail = isLoginFail;
        this.signUpFailListener = signUpFailListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signup_fail);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);

        vContainer.setOnFocusChangeListener((v, hasFocus) -> dismiss());

        tvTitle.setText(title);
        tvMessage.setText(message);
//        if (signUpType.getValue() == EnumMgr.SignUpType.Facebook.getValue()) {
//            tvLoginBy.setText(TCUtils.getString(R.string.login_by_facebook));
//        } else if (signUpType.getValue() == EnumMgr.SignUpType.Google.getValue()) {
//            tvLoginBy.setText(TCUtils.getString(R.string.login_by_google));
//        } else {
            tvLoginBy.setText(TCUtils.getString(R.string.login_by_email));
//        }
        if (isLoginFail) {
            Glide.with(getContext()).load(TCUtils.getDrawable(R.drawable.ic_message)).into(iv_icon);
            tvLoginBy.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onViewClick() {
        registerSingleClick(R.id.dialog_signup_fail_tv_login_by, R.id.dialog_signup_fail_tv_restart,
                R.id.dialog_signup_fail_v_container);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.dialog_signup_fail_tv_login_by:

                signUpFailListener.doLogin();

                dismiss();

                break;
            case R.id.dialog_signup_fail_tv_restart:

                signUpFailListener.doRestart();

                dismiss();

                break;

            case R.id.dialog_signup_fail_v_container:

                dismiss();

                break;
        }
    }
}
