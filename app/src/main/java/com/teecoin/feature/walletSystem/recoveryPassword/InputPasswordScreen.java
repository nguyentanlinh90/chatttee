package com.teecoin.feature.walletSystem.recoveryPassword;

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
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.loginwithemail.LoginForgotPasswordDialog;
import com.teecoin.feature.general.loginwithemail.LoginResetPasswordDialog;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.ResetPasswordResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralResetPasswordByPublicKeyRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletLoginAccountRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class InputPasswordScreen extends TCWalletBaseFragment implements StellarResponseListener {

    private static final String SECRET_TOKEN = "SecretToken";
    private static final String ACCOUNT_MODEL = "AccountModel";

    @BindView(R.id.fragment_input_password_v_container)
    View vContainer;

    @BindView(R.id.fragment_input_password_v_input_password)
    View vInputPassword;

    @BindView(R.id.fragment_input_password_et_input_password)
    EditText etInputPassword;

    @BindView(R.id.fragment_input_password_tv_error_input_password)
    TextView tvErrorPassword;

    @BindView(R.id.fragment_input_password_iv_clear_input_password)
    ImageView ivClearPassword;

    @BindView(R.id.fragment_input_password_tv_forgot_password)
    TextView tvForgotPassword;

    private String secretToken;

    private AccountModel accountModel;

    public static InputPasswordScreen getInstance(AccountModel accountModel, String secretToken) {
        InputPasswordScreen screen = new InputPasswordScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT_MODEL, accountModel);
        bundle.putString(SECRET_TOKEN, secretToken);
        screen.setArguments(bundle);
        return screen;
    }

    public static InputPasswordScreen getInstance(String secretToken) {
        InputPasswordScreen screen = new InputPasswordScreen();
        Bundle bundle = new Bundle();
        bundle.putString(SECRET_TOKEN, secretToken);
        screen.setArguments(bundle);
        return screen;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_password, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.password));
        showFooter();
        showCancelNextBottomView();
        setTextForNextBottomView(TCUtils.getString(R.string.text_login));
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();

        if (bundle != null) {

            secretToken = bundle.getString(SECRET_TOKEN);

            accountModel = (AccountModel) bundle.getSerializable(ACCOUNT_MODEL);

        }

        TCUtils.editTextTextChange(etInputPassword, ivClearPassword, tvErrorPassword, vInputPassword, null);

        tvForgotPassword.setText(Html.fromHtml(TCUtils.getString(R.string.forgot_password)));

        registerSingleClick(R.id.fragment_input_password_tv_forgot_password);

        TCUtils.addOnGlobalLayoutListener(vContainer, vInputPassword);
    }

    @Override
    public void onSingleClick(View v, Object object) {

        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.fragment_input_password_tv_forgot_password:

                popupMessageResetPassword();

                break;
        }
    }

    public boolean validate() {
        if (TCUtils.isEmpty(etInputPassword.getText().toString())) {

            tvErrorPassword.setVisibility(View.VISIBLE);

            return false;
        } else {
            return true;
        }
    }

    public void getAccountInfoAndGoToNextScreen() {
        String password = etInputPassword.getText().toString();

        requestApi(new WalletLoginAccountRequest(
                new AccountModel(accountModel.getPublic_key(), password, secretToken),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        if (response.getResult() != null) {
                            AccountModel accountModel = (AccountModel) response.getResult();
                            accountModel.setLogin(true);
                            accountModel.setSecret_key(SecretKeyEncryption.encrypt(secretToken));
                            accountModel.setPassword(password);
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
                                                }, InputPasswordScreen.this);
                                    }
                                }

                                @Override
                                public void onStellarFail(Throwable t) {
                                }
                            });
                        }
//                        showLoading(false);
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.log_in_failed),
                                errorModel.getErrorMessage(), TCUtils.getString(R.string.try_again)).show();
                        etInputPassword.setText("");
                        etInputPassword.setFocusable(true);
                    }
                }
        ));
    }

    private void popupMessageResetPassword() {
        /*ConfirmResetPasswordDialog resetPasswordDialog = new ConfirmResetPasswordDialog(getActiveActivity(), this::resetPasswordByPublicKey);
        resetPasswordDialog.show();*/

        LoginForgotPasswordDialog loginForgotPasswordDialog = new LoginForgotPasswordDialog(getActiveActivity(), new TCDecisionListener() {
            @Override
            public void onPositiveButtonClicked(int id, Object onWhat) {

                resetPassword();

            }

            @Override
            public void onNegativeButtonClicked(int id, Object onWhat) {

            }

            @Override
            public void onNeutralButtonClicked(int id, Object onWhat) {

            }
        });

        loginForgotPasswordDialog.show();
    }

    private void resetPassword() {
        requestApi(new GeneralResetPasswordByPublicKeyRequest(accountModel.getPublic_key(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == GeneralRequestTarget.RESET_PASSWORD) {
                    ResetPasswordResponseModel responseModel = (ResetPasswordResponseModel) response.getResult();
                    if (responseModel != null) {

//                        dialogResetPasswordSuccess(responseModel.getEmail());

                        new LoginResetPasswordDialog(getActiveActivity(), responseModel.getEmail()).show();
                    }
                }

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                if (requestTarget == GeneralRequestTarget.RESET_PASSWORD) {

                    showBaseMessage(errorModel.getErrorMessage());
                }
            }
        }));
    }

    private void dialogResetPasswordSuccess(String email) {
        ResetPasswordSuccessDialog resetPasswordSuccessDialog = new ResetPasswordSuccessDialog(getActiveActivity(), email);
        resetPasswordSuccessDialog.show();
    }

    @Override
    public void onStellarSuccess(Object result) {
        Toast.makeText(getActiveActivity(),
                (boolean) result ?
                        R.string.trust_tee_coin_success : R.string.trust_tee_coin_fail,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStellarFail(Throwable t) {

    }
}


