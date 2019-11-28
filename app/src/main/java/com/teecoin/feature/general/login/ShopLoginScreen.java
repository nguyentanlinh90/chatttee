package com.teecoin.feature.general.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.base.TCFailDialog;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.walletsystem.LoginEmailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.LoginEmailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ShopLoginScreen extends TCBaseFragment {
    @BindView(R.id.frg_shop_log_in_tv_forgot_password)
    TextView tvForgotPassword;

    @BindView(R.id.frg_shop_log_in_ll_register)
    View llRegister;

    @BindView(R.id.frg_shop_log_in_v_register)
    View vRegister;

    @BindView(R.id.frg_shop_log_in_v_input_password)
    View vInputPassword;
    @BindView(R.id.frg_shop_log_in_et_input_email)
    EditText etInputEmail;
    @BindView(R.id.frg_shop_log_in_iv_clear_input_email)
    ImageView ivClearEmail;
    @BindView(R.id.frg_shop_log_in_tv_error_input_email)
    TextView tvErrorEmail;

    @BindView(R.id.frg_shop_log_in_v_input_email)
    View vInputEmail;
    @BindView(R.id.frg_shop_log_in_et_input_password)
    EditText etInputPassword;
    @BindView(R.id.frg_shop_log_in_iv_clear_input_password)
    ImageView ivClearPassword;
    @BindView(R.id.frg_shop_log_in_tv_error_input_password)
    TextView tvErrorPassword;

    public static ShopLoginScreen getInstance() {
        return new ShopLoginScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        setWidthIndicatorRegister();
        initView();
        if (getActiveActivity().getWindow() != null) {
            getActiveActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    private void initView() {
        tvForgotPassword.setText(Html.fromHtml(TCUtils.getString(R.string.forgot_password)));

        TCUtils.editTextTextChange(etInputEmail, ivClearEmail, tvErrorEmail, vInputEmail, null);
        TCUtils.setBgWhenFocusView(etInputEmail, vInputEmail, vInputPassword);
        TCUtils.editTextTextChange(etInputPassword, ivClearPassword, tvErrorPassword, vInputPassword, null);
        TCUtils.setBgWhenFocusView(etInputPassword, vInputPassword, vInputEmail);
        registerSingleClick(R.id.frg_shop_log_in_tv_login, R.id.frg_shop_log_in_ll_register, R.id.frg_shop_log_in_tv_forgot_password);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_shop_log_in_tv_login:
                validateLogin();
                break;

            case R.id.frg_shop_log_in_ll_register:
                gotoRegister();
                break;

            case R.id.frg_shop_log_in_tv_forgot_password:
                if (inValidEmail()) {
                    return;
                }
                TCUtils.popupMessageResetPassword(etInputEmail);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frg_shop_log_in_tv_login, R.id.frg_shop_log_in_ll_register,
                R.id.frg_shop_log_in_tv_forgot_password);

        if (getActiveActivity().getWindow() != null) {
            getActiveActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    private boolean inValidEmail() {
        boolean isValid = true;
        if (etInputEmail.getText().toString().isEmpty()) {
            tvErrorEmail.setText(TCUtils.getString(R.string.please_enter_your_email));
            tvErrorEmail.setVisibility(View.VISIBLE);
            etInputEmail.requestFocus();
            vInputEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValid = false;
        } else if (!TCUtils.validateEmail(etInputEmail.getText().toString().trim())) {
            tvErrorEmail.setText(TCUtils.getString(R.string.validate_please_enter_valid_email));
            tvErrorEmail.setVisibility(View.VISIBLE);
            etInputEmail.requestFocus();
            vInputEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValid = false;
        }
        return !isValid;
    }

    private void gotoRegister() {
        addFragment(WebViewScreen.getInstance(TCConstant.SHOP_REGISTER, TCUtils.getString(R.string.register_account)));
    }

    private void validateLogin() {
        boolean isValid = false;
//        if (inValidEmail()) {
//           // return;
//        }
        isValid = inValidEmail();

        if (etInputPassword.getText().toString().isEmpty()) {
            tvErrorPassword.setVisibility(View.VISIBLE);
            tvErrorPassword.setText(TCUtils.getString(R.string.please_enter_your_password));
            vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValid = false;
        } else {
            if (!TCUtils.validatePassword(etInputPassword.getText().toString())) {
                tvErrorPassword.setVisibility(View.VISIBLE);
                tvErrorPassword.setText(TCUtils.getString(R.string.validate_please_enter_valid_password));
                etInputPassword.requestFocus();
                vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
                isValid = false;
            } else {
                isValid = true;
            }
        }


        if (isValid) {
            LoginEmailModel loginEmailModel = new LoginEmailModel(etInputEmail.getText().toString(), etInputPassword.getText().toString(), TCUtils.getUniquePseudoID());
            doLogin(loginEmailModel);
        }

    }

    private void doLogin(LoginEmailModel loginEmailModel) {

        requestApi(new LoginEmailRequest(true, loginEmailModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {

                    AccountModel accountModel = (AccountModel) response.getResult();
                    accountModel.setLogin(true);
                    accountModel.setPassword(loginEmailModel.getPassword());

                    TCSharePreferenceManager.getInstance().setString(DataKey.Token, accountModel.getToken());
                    ((TCMainActivity) getActiveActivity()).insertInitData(accountModel, () -> openHomeScreen());
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                showLoginFailDialog();
            }
        }));
    }

    private void showLoginFailDialog() {
        //vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
        etInputPassword.setText("");
        vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_5_radius));
        tvErrorPassword.setVisibility(View.GONE);
        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.log_in_failed),
                TCUtils.getString(R.string.the_email_address_or_password_entered_is_invalid), TCUtils.getString(R.string.try_again)).show();
    }

    private void setWidthIndicatorRegister() {
        llRegister.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int width = llRegister.getMeasuredWidth();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, 2);
            vRegister.setLayoutParams(layoutParams);
        });
    }
}
