package com.teecoin.feature.walletSystem.verifyphonenumber;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.recaptcha.RecaptchaDialog;
import com.teecoin.javastellarsdk.stellar.StellarProcess;
import com.teecoin.javastellarsdk.stellar.model.StellarAccount;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.SocialInfoModel;
import com.teecoin.model.walletsystem.CreateAccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralSignUpSocialRequest;
import com.teecoin.myapi.apirequest.walletsystem.SendOTPRequest;
import com.teecoin.myapi.apirequest.walletsystem.VerifyOTPRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCreateAccountRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_CODE;
import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_COIN_BACK_REWARD_RATE;
import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_COIN_EXCHANGE;
import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_MIN_AMOUNT;
import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_SYMBOL;

public class VerifyPhoneNumberScreen extends TCBaseFragment implements View.OnKeyListener, View.OnFocusChangeListener, TextWatcher, StellarResponseListener {
    private static final String ACCOUNT_MODEL = "AccountModel";
    private static final String SOCIAL_INFO_MODEL = "SocialInfoModel";
    private static final String IS_CREATE = "IS_CREATE";
    private static final String RECAPTCHA = "RECAPTCHA";
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_1)
    EditText etValidationCode1;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_2)
    EditText etValidationCode2;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_3)
    EditText etValidationCode3;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_4)
    EditText etValidationCode4;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_5)
    EditText etValidationCode5;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_6)
    EditText etValidationCode6;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_hidden)
    EditText etValidationCodeHidden;
    @BindView(R.id.frag_verify_code_tv_wrong_code)
    TextView tv_wrong_code;
    @BindView(R.id.frag_verify_code_tv_resend_in)
    TextView tv_resend_in;
    @BindView(R.id.frag_verify_code_tv_send_to_number)
    TextView tv_send_to_number;
    boolean isCodeIncorrect = false;
    private AccountModel accountModel;
    private SocialInfoModel socialInfoModel;
    private CountDownTimer countDownTimer;
    private boolean isCreate;
    private EnumMgr.SignUpFlow signUpFlow;
    private String recaptchaToken;

    //sign-up with email
    public static VerifyPhoneNumberScreen getInstance(boolean isCreate, AccountModel accountModel, String recaptchaToken) {
        VerifyPhoneNumberScreen screen = new VerifyPhoneNumberScreen();
        screen.signUpFlow = EnumMgr.SignUpFlow.Email;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT_MODEL, accountModel);
        bundle.putBoolean(IS_CREATE, isCreate);
        bundle.putString(RECAPTCHA, recaptchaToken);
        screen.setArguments(bundle);
        return screen;
    }

    //sign-up with social
    public static VerifyPhoneNumberScreen getInstance(SocialInfoModel socialInfoModel, String recaptchaToken) {
        VerifyPhoneNumberScreen screen = new VerifyPhoneNumberScreen();
        screen.signUpFlow = EnumMgr.SignUpFlow.Social;
        Bundle bundle = new Bundle();
        bundle.putSerializable(SOCIAL_INFO_MODEL, socialInfoModel);
        bundle.putString(RECAPTCHA, recaptchaToken);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_phone_number, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showCancelNextBottomView();
        updateTitleHeader(TCUtils.getString(R.string.verify_phone_number_title));
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            accountModel = (AccountModel) bundle.getSerializable(ACCOUNT_MODEL);
            socialInfoModel = (SocialInfoModel) bundle.getSerializable(SOCIAL_INFO_MODEL);
            isCreate = bundle.getBoolean(IS_CREATE);
            recaptchaToken = bundle.getString(RECAPTCHA);
        }

        //email
        if (signUpFlow.equals(EnumMgr.SignUpFlow.Email)) {
            if (!TCUtils.isEmpty(accountModel.getPhone())
                    && accountModel.getPhone().length() > 0) {
                tv_send_to_number.setText(
                        String.format(TCUtils.getString(
                                R.string.verify_phone_number_message_code_to_phone),
                                accountModel.getCountry_code(),
                                accountModel.getPhone().substring(0, 2),
                                accountModel.getPhone().substring(accountModel.getPhone().length() - 2)));

                sendOTP(accountModel.getCountry_code(), accountModel.getPhone(), accountModel.getDeviceId(), recaptchaToken);
            }
        } else if (signUpFlow.equals(EnumMgr.SignUpFlow.Social)) {
            if (!TCUtils.isEmpty(socialInfoModel.getPhone())
                    && socialInfoModel.getPhone().length() > 0) {
                tv_send_to_number.setText(
                        String.format(TCUtils.getString(
                                R.string.verify_phone_number_message_code_to_phone),
                                socialInfoModel.getCountry_code(),
                                socialInfoModel.getPhone().substring(0, 2),
                                socialInfoModel.getPhone().substring(socialInfoModel.getPhone().length() - 2)));

                sendOTP(socialInfoModel.getCountry_code(), socialInfoModel.getPhone(), socialInfoModel.getDevice_id(), recaptchaToken);

            }
        }

        setFocusedCodeBackground(etValidationCode1);
        setFocus(etValidationCodeHidden);
        showKeyboard(etValidationCodeHidden);
        setCodeListeners();
        countDownTimer();
        registerSingleClick(tv_resend_in);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_verify_code_tv_resend_in:
                send();
                break;
        }
    }

    private void send() {
        countDownTimer();
        new RecaptchaDialog(getActiveActivity(),
                reCaptcha -> {
                    if (signUpFlow.equals(EnumMgr.SignUpFlow.Email) && null != accountModel) {
                        sendOTP(accountModel.getCountry_code(), accountModel.getPhone(), accountModel.getDeviceId(), reCaptcha);
                    } else if (signUpFlow.equals(EnumMgr.SignUpFlow.Email) && null != socialInfoModel) {
                        sendOTP(socialInfoModel.getCountry_code(), socialInfoModel.getPhone(), socialInfoModel.getDevice_id(), reCaptcha);
                    }
                }).show();
    }

    private void countDownTimer() {
        String resendString = TCUtils.getString(R.string.verify_phone_number_resend_in);
        tv_resend_in.setEnabled(false);
        if (countDownTimer != null)
            countDownTimer = null;
        countDownTimer = new CountDownTimer(TCConstant.SIXTY_SECOND_IN_MILLISECOND, TCConstant.ONE_SECOND_IN_MILLISECOND) {
            public void onTick(long millisUntilFinished) {
                tv_resend_in.setText(String.format(resendString, millisUntilFinished / 1000));
            }

            public void onFinish() {
                tv_resend_in.setText(TCUtils.getString(R.string.verify_phone_number_resend));
                tv_resend_in.setEnabled(true);
            }
        }.start();
    }

    private void setCodeListeners() {
        etValidationCodeHidden.addTextChangedListener(this);

        etValidationCode1.setOnFocusChangeListener(this);
        etValidationCode2.setOnFocusChangeListener(this);
        etValidationCode3.setOnFocusChangeListener(this);
        etValidationCode4.setOnFocusChangeListener(this);
        etValidationCode5.setOnFocusChangeListener(this);
        etValidationCode6.setOnFocusChangeListener(this);

        etValidationCode1.setOnKeyListener(this);
        etValidationCode2.setOnKeyListener(this);
        etValidationCode3.setOnKeyListener(this);
        etValidationCode4.setOnKeyListener(this);
        etValidationCode5.setOnKeyListener(this);
        etValidationCode6.setOnKeyListener(this);
        etValidationCodeHidden.setOnKeyListener(this);
    }

    public void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void setDefaultCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_0_radius));
    }

    private void setFocusedCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_0_radius));
    }

    private void setCodeIncorrectCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_0_radius));
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.frg_coupon_user_validation_code_et_validation_code_hidden:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etValidationCodeHidden.getText().length() == 6)
                            etValidationCode6.setText("");
                        else if (etValidationCodeHidden.getText().length() == 5)
                            etValidationCode5.setText("");
                        else if (etValidationCodeHidden.getText().length() == 4)
                            etValidationCode4.setText("");
                        else if (etValidationCodeHidden.getText().length() == 3)
                            etValidationCode3.setText("");
                        else if (etValidationCodeHidden.getText().length() == 2)
                            etValidationCode2.setText("");
                        else if (etValidationCodeHidden.getText().length() == 1)
                            etValidationCode1.setText("");

                        if (etValidationCodeHidden.length() > 0)
                            etValidationCodeHidden.setText(etValidationCodeHidden.getText().subSequence(0, etValidationCodeHidden.length() - 1));

                        return true;
                    }

                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.frg_coupon_user_validation_code_et_validation_code_1:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_2:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_3:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_4:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_5:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_6:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isCodeIncorrect) {
            setDefaultCodeBackground(etValidationCode1);
            setDefaultCodeBackground(etValidationCode2);
            setDefaultCodeBackground(etValidationCode3);
            setDefaultCodeBackground(etValidationCode4);
            setDefaultCodeBackground(etValidationCode5);
            setDefaultCodeBackground(etValidationCode6);
        }

        if (s.length() == 0) {
            setFocusedCodeBackground(etValidationCode1);
            etValidationCode1.setText("");
            if (isCodeIncorrect) {
                isCodeIncorrect = false;
                setCodeIncorrectCodeBackground(etValidationCode1);
            }
        } else if (s.length() == 1) {
            setFocusedCodeBackground(etValidationCode2);
            etValidationCode1.setText(String.format("%s", s.charAt(0)));
            etValidationCode2.setText("");
            etValidationCode3.setText("");
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
            if (tv_wrong_code.getVisibility() == View.VISIBLE) {
                tv_wrong_code.setVisibility(View.GONE);
            }
//            if (tvErrorQrCode.getVisibility() == View.VISIBLE) {
//                tvErrorQrCode.setVisibility(View.GONE);
//            }
        } else if (s.length() == 2) {
            setFocusedCodeBackground(etValidationCode3);
            etValidationCode2.setText(String.format("%s", s.charAt(1)));
            etValidationCode3.setText("");
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 3) {
            setFocusedCodeBackground(etValidationCode4);
            etValidationCode3.setText(String.format("%s", s.charAt(2)));
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 4) {
            setFocusedCodeBackground(etValidationCode5);
            etValidationCode4.setText(String.format("%s", s.charAt(3)));
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 5) {
            setFocusedCodeBackground(etValidationCode6);
            etValidationCode5.setText(String.format("%s", s.charAt(4)));
            etValidationCode6.setText("");
        } else if (s.length() == 6) {
            etValidationCode6.setText(String.format("%s", s.charAt(5)));
            hideKeyBoardEditText();

            if (signUpFlow.equals(EnumMgr.SignUpFlow.Email)) {
                sendVerificationCode(accountModel.getCountry_code(), accountModel.getPhone(), s.toString());
            } else if (signUpFlow.equals(EnumMgr.SignUpFlow.Social)) {
                sendVerificationCode(socialInfoModel.getCountry_code(), socialInfoModel.getPhone(), s.toString());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TCUtils.isEmpty(etValidationCodeHidden.getText().toString())) {
            etValidationCodeHidden.setSelection(etValidationCodeHidden.getText().toString().length());
        }
    }

    private void sendVerificationCode(String countryCode, String phone, String smsVerificationCode) {
        requestApi(new VerifyOTPRequest(countryCode, phone, smsVerificationCode, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getSuccess()) {
                    TCAppFlyerTrackingEvent.getInstance().trackPhoneNumberOTPSuccess();
                    tv_wrong_code.setVisibility(View.GONE);

                    //create account with email
                    if (signUpFlow.equals(EnumMgr.SignUpFlow.Email)) {
                        createAccountWithEmail();
                    } else if (signUpFlow.equals(EnumMgr.SignUpFlow.Social)) {
                        createAccountWithSocial();
                    }
                } else {
                    TCAppFlyerTrackingEvent.getInstance().trackPhoneNumberOTPError();
                    tv_wrong_code.setVisibility(View.VISIBLE);
                    tv_wrong_code.setText(TCUtils.getString(R.string.verify_phone_number_message_wrong));

                    etValidationCodeHidden.setText("");

                    etValidationCode1.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode1);
                    etValidationCode2.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode2);
                    etValidationCode3.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode3);
                    etValidationCode4.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode4);
                    etValidationCode5.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode5);
                    etValidationCode6.setText("");
                    setCodeIncorrectCodeBackground(etValidationCode6);

                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                tv_wrong_code.setVisibility(View.VISIBLE);
                tv_wrong_code.setText(errorModel.getErrorMessage());
            }
        }));
    }

    private void createAccountWithSocial() {

        StellarAccount account = StellarProcess.create();
        if (null == accountModel) {
            accountModel = new AccountModel();
        }
        accountModel.setPublic_key(account.getAccountId());
        accountModel.setSecret_key(SecretKeyEncryption.encrypt(account.getSecretSeed()));

        accountModel.setAvatar(isAppUser() ?
                TCConstant.DEFAULT_USER_AVATAR_URL : TCConstant.DEFAULT_SHOP_AVATAR_URL);
        requestApi(new GeneralSignUpSocialRequest(socialInfoModel, GeneralRequestTarget.SOCIAL_REGISTER, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                AccountModel accountModel = (AccountModel) response.getResult();
                accountModel.setLogin(true);
                TCSharePreferenceManager.getInstance().setString(DataKey.Token, accountModel.getToken());
                ((TCMainActivity) getActiveActivity()).insertInitData(accountModel, () -> openHomeScreen());
                TCAppFlyerTrackingEvent.getInstance().trackWalletCreationSuccessFacebook();

                isAccountTrustedWithTeeCoin(accountModel.getPublic_key(), (step, eventLog) -> {
                    sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.IsAccountTrustedWithTeeCoin,
                            step, eventLog);
                }, new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o == null || !(boolean) o) {
                            stellarTrustTeeCoin(
                                    SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                                    false, (step, eventLog) -> {
                                        sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.TrustTeeCoin, step, eventLog);
                                    }, this);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable t) {
                    }
                });
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void createAccountWithEmail() {
        if (isCreate) {
            StellarAccount account = StellarProcess.create();
            accountModel.setPublic_key(account.getAccountId());
            accountModel.setSecret_key(SecretKeyEncryption.encrypt(account.getSecretSeed()));
        } else {
            accountModel.setPublic_key(StellarProcess.getPublicKey(accountModel.getSecret_key()));
            accountModel.setSecret_key(SecretKeyEncryption.encrypt(accountModel.getSecret_key()));
        }

        accountModel.setAvatar(isAppUser() ?
                TCConstant.DEFAULT_USER_AVATAR_URL : TCConstant.DEFAULT_SHOP_AVATAR_URL);
        requestApi(new WalletCreateAccountRequest(accountModel,
                isAppUser() ? WalletRequestTarget.CREATE_USER : WalletRequestTarget.CREATE_SHOP, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {
                    CreateAccountModel createAccountModel = (CreateAccountModel) response.getResult();
                    accountModel.setTokenAndUUID(createAccountModel);
                    accountModel.setLogin(true);

                    TCSharePreferenceManager.getInstance().setString(DataKey.Token, accountModel.getToken());

                    stellarTrustTeeCoin(SecretKeyEncryption.decrypt(accountModel.getSecret_key()), true,
                            (step, eventLog) -> sendTrackTrustIssueToAnalytic(
                                    TCGoogleAnalyticTrackingEvent.TrackType.TrustTeeCoin, step, eventLog),
                            VerifyPhoneNumberScreen.this);
                    accountModel.setReturn_rate(DEFAULT_COIN_BACK_REWARD_RATE);
                    accountModel.setMin_amount(DEFAULT_MIN_AMOUNT);
                    accountModel.setCurrency_code(DEFAULT_CODE);
                    accountModel.setCurrency_symbol(DEFAULT_SYMBOL);
                    accountModel.setRate(DEFAULT_COIN_EXCHANGE);

                    ((TCMainActivity) getActiveActivity()).insertInitData(accountModel, () -> openNextScreen());
                    TCAppFlyerTrackingEvent.getInstance().trackWalletCreationSuccessEmail();
                } else {
                    TCLog.e("Please check model response");
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));

    }

    public void validate() {
        if (etValidationCodeHidden.getText().toString().length() < 6) {
            tv_wrong_code.setVisibility(View.VISIBLE);
            tv_wrong_code.setText(TCUtils.getString(R.string.verify_phone_number_pls_input_verification_code));
        }
    }

    private void sendOTP(String countryCode, String phone, String deviceId, String recaptchaToken) {
        requestApi(new SendOTPRequest(countryCode, phone, deviceId, recaptchaToken,
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        tv_wrong_code.setVisibility(View.VISIBLE);
                        tv_wrong_code.setText(errorModel.getErrorMessage());
                    }
                }));
    }

    @Override
    public void onStellarSuccess(Object result) {
        if ((boolean) result) {
            Toast.makeText(getActiveActivity(), R.string.trust_tee_coin_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActiveActivity(), R.string.trust_tee_coin_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStellarFail(Throwable o) {
    }

    private void openNextScreen() {
        openHomeScreen();
    }
}
