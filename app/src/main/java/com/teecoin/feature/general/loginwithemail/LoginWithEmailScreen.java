package com.teecoin.feature.general.loginwithemail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.login.EnableEmailLoginDialog;
import com.teecoin.feature.walletSystem.importSecretKey.ImportSecretKeyScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.walletsystem.LoginEmailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.LoginEmailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class LoginWithEmailScreen extends TCWalletBaseFragment {
    private static final String EMAIL = "EMAIL";

    @BindView(R.id.fragment_log_in_v_container)
    View vContainer;

    @BindView(R.id.fragment_log_in_v_input_email)
    View vInputEmail;

    @BindView(R.id.fragment_log_in_et_input_email)
    EditText etInputEmail;

    @BindView(R.id.fragment_log_in_iv_clear_input_email)
    ImageView ivClearEmail;

    @BindView(R.id.fragment_log_in_tv_error_input_email)
    TextView tvErrorEmail;

    @BindView(R.id.fragment_log_in_v_input_password)
    View vInputPassword;

    @BindView(R.id.fragment_log_in_et_input_password)
    EditText etInputPassword;

    @BindView(R.id.fragment_log_in_iv_clear_input_password)
    ImageView ivClearPassword;

    @BindView(R.id.fragment_log_in_tv_error_input_password)
    TextView tvErrorPassword;

    @BindView(R.id.fragment_log_in_tv_forgot_password)
    TextView tvForgotPassword;

    private String email;


    public static LoginWithEmailScreen getInstance(String email) {
        LoginWithEmailScreen screen = new LoginWithEmailScreen();
        Bundle bundle = new Bundle();
        bundle.putString(EMAIL, email);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in_with_email, container, false);
    }

    @Override
    public void onBaseResume() {

        showHeader();

        showButtonBackToolbar();

        updateTitleHeader(TCUtils.getString(R.string.text_login));

        showFooter();

        hideMenuNextBottom();

        showCancelNextBottomView();

        setTextForNextBottomView(TCUtils.getString(R.string.text_login));

        setTextForCancelBottomView(TCUtils.getString(R.string.text_cancel));
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString(EMAIL);
        }
        initView();
    }

    private void initView() {
        tvForgotPassword.setText(Html.fromHtml(TCUtils.getString(R.string.forgot_password)));
        if (!TCUtils.isEmpty(email)) {
            etInputEmail.setText(email);
        }
        TCUtils.editTextTextChange(etInputEmail, ivClearEmail, tvErrorEmail, vInputEmail, null);
        TCUtils.setBgWhenFocusView(etInputEmail, vInputEmail, vInputPassword);
        TCUtils.editTextTextChange(etInputPassword, ivClearPassword, tvErrorPassword, vInputPassword, null);
        TCUtils.setBgWhenFocusView(etInputPassword, vInputPassword, vInputEmail);
        registerSingleClick(R.id.fragment_log_in_tv_forgot_password);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        if (inValidEmail()) {
            return;
        }
        TCUtils.popupMessageResetPassword(etInputEmail);
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.fragment_log_in_tv_forgot_password);
    }

    private boolean inValidEmail() {
        boolean isValid = true;
        if (etInputEmail.getText().toString().isEmpty()) {
            tvErrorEmail.setText(TCUtils.getString(R.string.please_enter_your_email));
            tvErrorEmail.setVisibility(View.VISIBLE);
            etInputEmail.requestFocus();
            vInputEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValid = false;
        }else if (!TCUtils.validateEmail(etInputEmail.getText().toString())) {
            tvErrorEmail.setText(TCUtils.getString(R.string.validate_please_enter_valid_email));
            tvErrorEmail.setVisibility(View.VISIBLE);
            etInputEmail.requestFocus();
            vInputEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValid = false;
        }
        return !isValid;
    }

    public void validateLogin() {
        if (inValidEmail()) {
            return;
        }

        if (etInputPassword.getText().toString().isEmpty()) {

            tvErrorPassword.setVisibility(View.VISIBLE);

            etInputPassword.requestFocus();
            vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            return;
        }

        LoginEmailModel loginEmailModel = new LoginEmailModel(etInputEmail.getText().toString(), etInputPassword.getText().toString(),
                TCUtils.getUniquePseudoID());

        doLogin(loginEmailModel);
    }

    private void doLogin(LoginEmailModel loginEmailModel) {

        requestApi(new LoginEmailRequest(false, loginEmailModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                if (response.getResult() != null) {

                    AccountModel accountModel = (AccountModel) response.getResult();
                    accountModel.setLogin(true);
                    accountModel.setPassword(loginEmailModel.getPassword());

                    TCSharePreferenceManager.getInstance().setString(DataKey.Token, accountModel.getToken());

                    ((TCMainActivity) getActiveActivity()).insertInitData(accountModel, () -> openHomeScreen());

                    isAccountTrustedWithTeeCoin(accountModel.getPublic_key(), (step, eventLog) -> {
                        sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.IsAccountTrustedWithTeeCoin, step, eventLog);
                    }, new StellarResponseListener() {
                        @Override
                        public void onStellarSuccess(Object o) {
                            if (o == null || !(boolean) o) {
                                stellarTrustTeeCoin(
                                        SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                                        true, (step, eventLog) -> {
                                            sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.TrustTeeCoin, step, eventLog);
                                        }, this);
                            }
                        }

                        @Override
                        public void onStellarFail(Throwable t) {

//                            showLoginFailDialog();

                        }
                    });
                }

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                if ("Email login fail.".equals(errorModel.getErrorMessage())) {// not save secret key

                    EnableEmailLoginDialog enableEmailLoginDialog = new EnableEmailLoginDialog(getActiveActivity(), new TCDecisionListener() {
                        @Override
                        public void onPositiveButtonClicked(int id, Object onWhat) {

//                            handleBackPressed();

                            replaceFragment(ImportSecretKeyScreen.getInstance(), false);
                        }

                        @Override
                        public void onNegativeButtonClicked(int id, Object onWhat) {

                        }

                        @Override
                        public void onNeutralButtonClicked(int id, Object onWhat) {

                        }
                    });

                    enableEmailLoginDialog.show();

                } else {

                    showLoginFailDialog();
                }
            }
        }));
    }

    private void showLoginFailDialog() {
        vInputPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
        vInputEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.log_in_failed),
                TCUtils.getString(R.string.the_email_address_or_password_entered_is_invalid), TCUtils.getString(R.string.try_again)).show();
    }
}
