package com.teecoin.feature.general.signupfacebook;

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

import com.facebook.login.widget.LoginButton;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.teecoin.R;
import com.teecoin.base.TCSignUpBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.loginwithemail.LoginWithEmailScreen;
import com.teecoin.feature.general.recaptcha.GetRecaptchaListener;
import com.teecoin.feature.general.recaptcha.RecaptchaDialog;
import com.teecoin.feature.general.welcome.NewWelcomeScreen;
import com.teecoin.feature.walletSystem.verifyphonenumber.VerifyPhoneNumberScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.SocialInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletCheckExistEmailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCheckExistPhoneRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SignUpAccountWithFaceBookScreen extends TCSignUpBaseFragment implements APIResponseListener {

    private static final String SOCIAL_INFO_MODEL = "SocialInfoModel";

    @BindView(R.id.frg_sign_up_account_facebook_v_input_full_name)
    View vFullName;

    @BindView(R.id.frg_sign_up_account_facebook_et_name)
    EditText etFullName;

    @BindView(R.id.frg_sign_up_account_facebook_iv_remove_name)
    ImageView ivClearFullName;

    @BindView(R.id.frg_sign_up_account_facebook_tv_error_input_full_name)
    TextView tvErrorFullName;

    @BindView(R.id.frg_sign_up_account_facebook_v_input_email)
    View vEmail;

    @BindView(R.id.frg_sign_up_account_facebook_et_email)
    EditText etEmail;

    @BindView(R.id.frg_sign_up_account_facebook_iv_remove_email)
    ImageView ivClearEmail;

    @BindView(R.id.frg_sign_up_account_facebook_tv_error_input_email)
    TextView tvErrorEmail;

    @BindView(R.id.frg_sign_up_account_facebook_sp_phone_code)
    Spinner spPhoneCode;

    @BindView(R.id.frg_sign_up_account_facebook_v_input_phone)
    View vPhone;

    @BindView(R.id.frg_sign_up_account_facebook_et_input_phone)
    EditText etPhone;

    @BindView(R.id.frg_sign_up_account_facebook_iv_clear_input_phone)
    ImageView ivClearPhone;

    @BindView(R.id.frg_sign_up_account_facebook_tv_error_input_phone)
    TextView tvErrorPhone;

    @BindView(R.id.frg_sign_up_account_facebook_tv_male)
    TextView tvMale;

    @BindView(R.id.frg_sign_up_account_facebook_tv_female)
    TextView tvFemale;

    @BindView(R.id.frg_sign_up_account_facebook_tv_error_gender)
    TextView tvErrorGender;

    private String gender = EnumMgr.EnumGender.Unknown.getValue();


    private LoginButton btFacebook;
    private SocialInfoModel socialInfoModel;

    public static SignUpAccountWithFaceBookScreen newInstance(SocialInfoModel signUpSocialModel) {
        SignUpAccountWithFaceBookScreen screen = new SignUpAccountWithFaceBookScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SOCIAL_INFO_MODEL, signUpSocialModel);
        screen.setArguments(bundle);
        return screen;
    }

    public void setButtonFacebook(LoginButton btFacebook) {
        this.btFacebook = btFacebook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_account_facebook, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        hideMenuNextBottom();
        showCancelNextBottomView();
        setTextForNextBottomView(TCUtils.getString(R.string.text_sign_up).toUpperCase());
        setTextForCancelBottomView(TCUtils.getString(R.string.text_cancel).toUpperCase());
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            socialInfoModel = (SocialInfoModel) bundle.getSerializable(SOCIAL_INFO_MODEL);
            updateTitleHeader(TCUtils.getString(R.string.text_sign_up).toUpperCase());
        }

        setupFacebook(btFacebook, true);

        initView();

        registerSingleClick(R.id.frg_sign_up_account_facebook_tv_male, R.id.frg_sign_up_account_facebook_tv_female,
                R.id.frg_sign_up_account_facebook_iv_phone_code);


    }

    private void initView() {

        if (socialInfoModel != null) {
            etFullName.setText(!TCUtils.isEmpty(socialInfoModel.getFull_name()) ? socialInfoModel.getFull_name() : "");
            etEmail.setText(!TCUtils.isEmpty(socialInfoModel.getEmail()) ? socialInfoModel.getEmail() : "");

            if (!TCUtils.isEmpty(socialInfoModel.getEmail())) {
                etEmail.setEnabled(false);
                ivClearPhone.setVisibility(View.GONE);
            }

            etPhone.setText(!TCUtils.isEmpty(socialInfoModel.getPhone()) ? socialInfoModel.getPhone() : "");
        }

        TCUtils.setupPhoneSpinner(spPhoneCode);

        TCUtils.editTextTextChange(etFullName, ivClearFullName, tvErrorFullName, vFullName, null);
        TCUtils.setBgWhenFocusView(etFullName, vFullName, vEmail, vPhone);

        TCUtils.editTextTextChange(etEmail, ivClearEmail, tvErrorEmail, vEmail, null);
        TCUtils.setBgWhenFocusView(etEmail, vEmail, vFullName, vPhone);

        TCUtils.editTextTextChange(etPhone, ivClearPhone, tvErrorPhone, vPhone, null);
        TCUtils.setBgWhenFocusView(etPhone, vPhone, vFullName, vEmail);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.frg_sign_up_account_facebook_tv_male:

                setBgGender(EnumMgr.EnumGender.Male.getValue());
                tvErrorGender.setVisibility(View.INVISIBLE);

                break;

            case R.id.frg_sign_up_account_facebook_tv_female:

                setBgGender(EnumMgr.EnumGender.Female.getValue());
                tvErrorGender.setVisibility(View.INVISIBLE);

                break;

            case R.id.frg_sign_up_account_facebook_iv_phone_code:
                vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

                spPhoneCode.performClick();

                break;

        }
    }

    public void validateExistEmail() {
        socialInfoModel.setEmail(etEmail.getText().toString());
        requestApi(new WalletCheckExistEmailRequest(socialInfoModel.getEmail(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                validateExistPhone();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                new DialogSignUpFail(getActiveActivity(),
                        TCUtils.getString(R.string.email_exists),
                        TCUtils.getString(R.string.the_email_address_has_been_already_taken),
                        EnumMgr.SignUpType.Facebook, true, new SignUpFailListener() {
                    @Override
                    public void doLogin() {
                        addFragment(LoginWithEmailScreen.getInstance(etEmail.getText().toString()));
                    }

                    @Override
                    public void doRestart() {
                        // validateExistEmail();
                        replaceFragment(NewWelcomeScreen.getInstance(), true);
                    }
                }).show();
            }
        }));
    }

    public void validateExistPhone() {
        socialInfoModel.setPhone(etPhone.getText().toString());
        socialInfoModel.setCountry_code(String.format("+%s", PhoneNumberUtil.normalizeDigitsOnly(
                TCUtils.getCallingPhoneCode(spPhoneCode.getSelectedItem().toString()))));
        requestApi(new WalletCheckExistPhoneRequest(socialInfoModel.getPhone(), socialInfoModel.getCountry_code(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
//                createFacebookAccount();
                //old thread: no need verify phone
                //new thread: verify phone before createFacebookAccount
                verifyPhone();

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
//                tvErrorPhone.setVisibility(View.VISIBLE);
//                tvErrorPhone.setText(errorModel.getErrorMessage());
                showBaseMessage(TCUtils.getString(R.string.phone_number_already_exists));
            }
        }));
    }

    private void verifyPhone() {

        socialInfoModel.setGender(gender);
        socialInfoModel.setDevice_id(TCUtils.getUniquePseudoID());

        //addFragment(VerifyPhoneNumberScreen.getInstance(socialInfoModel));
        new RecaptchaDialog(getActiveActivity(), new GetRecaptchaListener() {
            @Override
            public void getRecaptcha(String reCaptcha) {
                addFragment(VerifyPhoneNumberScreen.getInstance(socialInfoModel, reCaptcha));
            }
        }).show();
    }

    public boolean validate() {

        String email = etEmail.getText().toString();

        String name = etFullName.getText().toString();

        String phoneNumber = etPhone.getText().toString();

        etEmail.setError(null);

        if (name.isEmpty()) {
            tvErrorFullName.setVisibility(View.VISIBLE);
            vFullName.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
            vFullName.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            return false;
        } else {
            tvErrorFullName.setVisibility(View.INVISIBLE);
        }
        tvErrorEmail.setText(TCUtils.getString(R.string.please_enter_your_email));
        if (email.isEmpty()) {
            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(TCUtils.getString(R.string.please_enter_your_email));
            vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            return false;
        } else {
            if (!TCUtils.validateEmail(email)) {
                tvErrorEmail.setVisibility(View.VISIBLE);
                tvErrorEmail.setText(TCUtils.getString(R.string.validate_please_enter_valid_email));
                vEmail.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
                return false;
            } else {
                tvErrorEmail.setVisibility(View.INVISIBLE);
            }
        }

        if (TCUtils.isEmpty(phoneNumber)) {

            tvErrorPhone.setVisibility(View.VISIBLE);
            tvErrorPhone.setText(TCUtils.getString(R.string.please_enter_your_phone_number));
            vPhone.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            return false;

        } else {
            if (!TCUtils.validatePhoneNumber(etPhone.getText().toString(), spPhoneCode.getSelectedItem().toString())) {

                TCAppFlyerTrackingEvent.getInstance().trackPhoneNumberInvalid();

                tvErrorPhone.setVisibility(View.VISIBLE);
                tvErrorPhone.setText(TCUtils.getString(R.string.validate_please_enter_valid_phone_number));
                vPhone.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

                return false;
            } else {
                tvErrorPhone.setVisibility(View.INVISIBLE);
            }
        }

        if (EnumMgr.EnumGender.Unknown.getValue().equals(gender)) {

//            tvErrorGender.setVisibility(View.VISIBLE);
            new DialogNotSelectGender(getActiveActivity()).show();

            return false;

        }

        return true;
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
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.CHECK_EXIST_EMAIL) {
            if (etEmail != null) {
                etEmail.setError(null);
            }
        }
    }


    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.CHECK_EXIST_EMAIL) {
            if (etEmail != null) {
                etEmail.setError(TCUtils.getString(R.string.validate_email_exists));
            }
        } else if (requestTarget == WalletRequestTarget.CREATE_USER
                || requestTarget == WalletRequestTarget.CREATE_SHOP) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert),
                    errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
        }
    }
}
