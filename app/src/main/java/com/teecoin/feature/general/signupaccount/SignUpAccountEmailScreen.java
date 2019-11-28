package com.teecoin.feature.general.signupaccount;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.recaptcha.GetRecaptchaListener;
import com.teecoin.feature.general.recaptcha.RecaptchaDialog;
import com.teecoin.feature.walletSystem.verifyphonenumber.VerifyPhoneNumberScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletCheckExistEmailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCheckExistPhoneRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SignUpAccountEmailScreen extends TCGeneralBaseFragment implements APIResponseListener, StellarResponseListener {

    private static final String REFERRAL_CODE = "REFERRAL_CODE";

    private static final String GET_IS_CREATE = "CREATE_WALLET";

    private static final String ACCOUNT_MODEL = "ACCOUNT_MODEL";

    @BindView(R.id.frg_sign_up_account_email_v_input_full_name)
    View vFullName;

    @BindView(R.id.frg_sign_up_account_email_et_full_name)
    EditText etFullName;

    @BindView(R.id.frg_sign_up_account_email_iv_clear_input_full_name)
    ImageView ivClearFullName;

    @BindView(R.id.frg_sign_up_account_email_tv_error_input_full_name)
    TextView tvErrorFullName;

    @BindView(R.id.frg_sign_up_account_email_v_input_email)
    View vEmail;

    @BindView(R.id.frg_sign_up_account_email_et_email)
    EditText etEmail;

    @BindView(R.id.frg_sign_up_account_email_iv_clear_input_email)
    ImageView ivClearEmail;

    @BindView(R.id.frg_sign_up_account_email_tv_error_input_email)
    TextView tvErrorEmail;

    @BindView(R.id.frg_sign_up_account_email_v_input_password)
    View vPassword;

    @BindView(R.id.frg_sign_up_account_email_et_input_password)
    EditText etPassword;

    @BindView(R.id.frg_sign_up_account_email_iv_clear_input_password)
    ImageView ivClearPassword;

    @BindView(R.id.frg_sign_up_account_email_tv_error_input_password)
    TextView tvErrorPassword;

    @BindView(R.id.frg_sign_up_account_email_v_input_confirm_password)
    View vConfirmPassword;

    @BindView(R.id.frg_sign_up_account_email_et_input_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.frg_sign_up_account_email_iv_clear_input_confirm_password)
    ImageView ivClearConfirmPassword;

    @BindView(R.id.frg_sign_up_account_email_tv_error_input_confirm_password)
    TextView tvErrorConfirmPassword;

    @BindView(R.id.frg_sign_up_account_email_v_input_phone)
    View vPhone;

    @BindView(R.id.frg_sign_up_account_email_et_input_phone)
    EditText etPhone;

    @BindView(R.id.frg_sign_up_account_email_iv_clear_input_phone)
    ImageView ivClearPhone;

    @BindView(R.id.frg_sign_up_account_email_tv_error_input_phone)
    TextView tvErrorPhone;

    @BindView(R.id.frg_sign_up_account_email_sp_phone_code)
    Spinner spPhoneCode;

    @BindView(R.id.frg_sign_up_account_email_tv_male)
    TextView tvMale;

    @BindView(R.id.frg_sign_up_account_email_tv_female)
    TextView tvFemale;

    @BindView(R.id.frg_sign_up_account_email_tv_error_gender)
    TextView tvErrorGender;

    private String gender = EnumMgr.EnumGender.Unknown.getValue();

    private boolean isCreate = true;

    private AccountModel accountModel;


    public static SignUpAccountEmailScreen getInstance(boolean create, AccountModel accountModel) {
        SignUpAccountEmailScreen screen = new SignUpAccountEmailScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GET_IS_CREATE, create);
        bundle.putSerializable(ACCOUNT_MODEL, accountModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_account_email, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        hideMenuNextBottom();
        showCancelNextBottomView();
        setTextForNextBottomView(TCUtils.getString(R.string.text_sign_up));
        setTextForCancelBottomView(TCUtils.getString(R.string.text_cancel));
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();

        if (bundle != null) {

            isCreate = bundle.getBoolean(GET_IS_CREATE);

            accountModel = (AccountModel) bundle.getSerializable(ACCOUNT_MODEL);

            updateTitleHeader(TCUtils.getString(R.string.text_sign_up));
        }
        initView();
    }

    private void initView() {

        TCUtils.editTextTextChange(etFullName, ivClearFullName, tvErrorFullName, vFullName, null);
        TCUtils.setBgWhenFocusView(etFullName, vFullName, vEmail, vPassword, vConfirmPassword, vPhone);

        TCUtils.editTextTextChange(etEmail, ivClearEmail, tvErrorEmail, vEmail, null);
        TCUtils.setBgWhenFocusView(etEmail, vEmail, vFullName, vPassword, vConfirmPassword, vPhone);

        TCUtils.editTextTextChange(etPassword, ivClearPassword, tvErrorPassword, vPassword, null);
        TCUtils.setBgWhenFocusView(etPassword, vPassword, vFullName, vEmail, vConfirmPassword, vPhone);

        TCUtils.editTextTextChange(etConfirmPassword, ivClearConfirmPassword, tvErrorConfirmPassword, vConfirmPassword, null);
        TCUtils.setBgWhenFocusView(etConfirmPassword, vConfirmPassword, vFullName, vEmail, vPassword, vPhone);

        TCUtils.editTextTextChange(etPhone, ivClearPhone, tvErrorPhone, vPhone, null);
        TCUtils.setBgWhenFocusView(etPhone, vPhone, vFullName, vEmail, vPassword, vConfirmPassword);

        TCUtils.setupPhoneSpinner(spPhoneCode);
        registerSingleClick(R.id.frg_sign_up_account_email_iv_phone_code,
                R.id.frg_sign_up_account_email_tv_male, R.id.frg_sign_up_account_email_tv_female);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.frg_sign_up_account_email_iv_phone_code:

                spPhoneCode.performClick();

                break;

            case R.id.frg_sign_up_account_email_tv_male:

                setBgGender(EnumMgr.EnumGender.Male.getValue());
                tvErrorGender.setVisibility(View.INVISIBLE);

                break;

            case R.id.frg_sign_up_account_email_tv_female:

                setBgGender(EnumMgr.EnumGender.Female.getValue());
                tvErrorGender.setVisibility(View.INVISIBLE);

                break;
        }
    }

    private void setBgGender(String type) {

        gender = type;

        tvMale.setBackground(TCUtils.getDrawable(
                EnumMgr.EnumGender.Male.getValue().equals(type) ?
                        R.drawable.bg_white_solid_gold_border_5_radius
                        : R.drawable.bg_gray_solid_no_border_5_radius));

        tvFemale.setBackground(TCUtils.getDrawable(
                EnumMgr.EnumGender.Female.getValue().equals(type) ?
                        R.drawable.bg_white_solid_gold_border_5_radius
                        : R.drawable.bg_gray_solid_no_border_5_radius));

    }

    @Override
    public void onBaseDestroyView() {

        unregisterSingleClick(R.id.frg_sign_up_account_email_iv_phone_code);
    }

    public void validateExistEmail() {

        if (accountModel == null)
            accountModel = new AccountModel();

        accountModel.setEmail(etEmail.getText().toString());
        accountModel.setGender(gender);

        requestApi(new WalletCheckExistEmailRequest(accountModel.getEmail(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                if (requestTarget == WalletRequestTarget.CHECK_EXIST_EMAIL) {

                    tvErrorEmail.setVisibility(View.INVISIBLE);

                    validateExistPhone();
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                if (requestTarget == WalletRequestTarget.CHECK_EXIST_EMAIL) {

                    tvErrorEmail.setVisibility(View.VISIBLE);

                    tvErrorEmail.setText(errorModel.getErrorMessage());
                }
            }
        }));
    }

    public void validateExistPhone() {
        accountModel.setPhone(etPhone.getText().toString());
        accountModel.setCountry_code(String.format("+%s", PhoneNumberUtil.normalizeDigitsOnly(
                TCUtils.getCallingPhoneCode(spPhoneCode.getSelectedItem().toString()))));
        requestApi(new WalletCheckExistPhoneRequest(accountModel.getPhone(), accountModel.getCountry_code(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                createAccount();

                tvErrorPhone.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                tvErrorPhone.setVisibility(View.VISIBLE);
                tvErrorPhone.setText(errorModel.getErrorMessage());
                //  showBaseMessage(TCUtils.getString(R.string.phone_number_already_exists));
            }
        }));
    }

    public void startSignUp() {

        String fullName = etFullName.getText().toString();

        String email = etEmail.getText().toString();

        String password = etPassword.getText().toString();

        String phoneNumber = etPhone.getText().toString();

        accountModel.setPhone(phoneNumber);
        accountModel.setCountry_code(String.format("+%s", PhoneNumberUtil.normalizeDigitsOnly(
                TCUtils.getCallingPhoneCode(spPhoneCode.getSelectedItem().toString()))));
        accountModel.setEmail(email);
        accountModel.setFull_name(fullName);
        accountModel.setName(fullName);
        accountModel.setPassword(password);
        accountModel.setConfirm_password(password);
        accountModel.setDeviceId(TCUtils.getUniquePseudoID());
        accountModel.setLanguage(TCUtils.getLanguageCode());
        accountModel.setAvatar(isAppUser() ? TCConstant.DEFAULT_USER_AVATAR_URL : TCConstant.DEFAULT_SHOP_AVATAR_URL);

        validateExistEmail();
    }

    public boolean validate() {

        if (isEmulator() && TCUtils.isProductionMode()) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.you_cant_create_account_on_emulator), TCUtils.getString(R.string.text_ok), null, null);
            return false;
        }

        boolean isValidate = true;
        String fullName = etFullName.getText().toString();

        String email = etEmail.getText().toString();

        String password = etPassword.getText().toString();

        String confirmPassword = etConfirmPassword.getText().toString();

        String phoneNumber = etPhone.getText().toString();

        if (accountModel == null) {

            accountModel = new AccountModel();
        }
        accountModel.setNotes("");

        if (TCUtils.isEmpty(fullName)) {

            tvErrorFullName.setVisibility(View.VISIBLE);
            vFullName.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            isValidate = false;
        }

        if (TCUtils.isEmpty(email)) {

            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(TCUtils.getString(R.string.please_enter_your_email));
            vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (email.length() > 100) {

            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(TCUtils.getString(R.string.the_email_is_max_100_characters));
            vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }


        if (!TCUtils.validateEmail(email)) {

            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(TCUtils.getString(R.string.validate_please_enter_valid_email));
            vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (TCUtils.isEmpty(password)) {

            tvErrorPassword.setVisibility(View.VISIBLE);
            tvErrorPassword.setText(TCUtils.getString(R.string.please_enter_your_password));
            vPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (!TCUtils.validatePassword(password)) {

            tvErrorPassword.setVisibility(View.VISIBLE);
            tvErrorPassword.setText(TCUtils.getString(R.string.validate_please_enter_valid_password));
            vPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (password.length() > 100) {

            tvErrorPassword.setVisibility(View.VISIBLE);
            tvErrorPassword.setText(TCUtils.getString(R.string.password_is_max_100_characters));
            vPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (TCUtils.isEmpty(confirmPassword)) {

            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            tvErrorConfirmPassword.setText(TCUtils.getString(R.string.please_re_enter_your_password));
            vConfirmPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }

        if (!confirmPassword.equals(password)) {

            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            tvErrorConfirmPassword.setText(TCUtils.getString(R.string.validate_password_not_match));
            vConfirmPassword.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            isValidate = false;
        }


        if (TCUtils.isEmpty(phoneNumber)) {

            tvErrorPhone.setVisibility(View.VISIBLE);
            tvErrorPhone.setText(TCUtils.getString(R.string.please_enter_your_phone_number));

            isValidate = false;

        } else {
            if (!TCUtils.validatePhoneNumber(etPhone.getText().toString(), spPhoneCode.getSelectedItem().toString())) {

                TCAppFlyerTrackingEvent.getInstance().trackPhoneNumberInvalid();

                tvErrorPhone.setVisibility(View.VISIBLE);
                tvErrorPhone.setText(TCUtils.getString(R.string.validate_please_enter_valid_phone_number));

                isValidate = false;

            }
        }

        if (EnumMgr.EnumGender.Unknown.getValue().equals(gender)) {

            tvErrorGender.setVisibility(View.VISIBLE);

            return false;

        }

        return isValidate;
    }

    private void createAccount() {

        if (isCreate) {
            if (TCUtils.isEmpty(accountModel.getReferralCode())) {

                TCAppFlyerTrackingEvent.getInstance().trackCreateWalletForm1();

            } else {

                TCAppFlyerTrackingEvent.getInstance().trackReferralWalletForm1();
            }
            new RecaptchaDialog(getActiveActivity(), new GetRecaptchaListener() {
                @Override
                public void getRecaptcha(String reCaptcha) {
                    addFragment(VerifyPhoneNumberScreen.getInstance(isCreate, accountModel, reCaptcha));
                }
            }).show();
        } else {
            new RecaptchaDialog(getActiveActivity(), new GetRecaptchaListener() {
                @Override
                public void getRecaptcha(String reCaptcha) {
                    addFragment(VerifyPhoneNumberScreen.getInstance(isCreate, accountModel, reCaptcha));
                }
            }).show();
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }


    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

        if (requestTarget == WalletRequestTarget.CHECK_EXIST_EMAIL) {

            if (etEmail != null) {

                tvErrorEmail.setVisibility(View.VISIBLE);

                tvErrorEmail.setText(TCUtils.getString(R.string.validate_email_exists));
            }
        } else if (requestTarget == WalletRequestTarget.CREATE_USER || requestTarget == WalletRequestTarget.CREATE_SHOP) {

            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
        }
    }

    @Override
    public void onStellarSuccess(Object result) {
        if ((boolean) result) {
            Toast.makeText(getActiveActivity(), R.string.trust_tee_coin_success, Toast.LENGTH_LONG).show();
            ((TCMainActivity) getActiveActivity()).openHomeScreen();
        } else {
            Toast.makeText(getActiveActivity(), R.string.trust_tee_coin_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStellarFail(Throwable o) {
        Toast.makeText(getActiveActivity(), R.string.trust_tee_coin_fail, Toast.LENGTH_LONG).show();
    }
}
