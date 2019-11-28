package com.teecoin.feature.general.changeRecoveryPassword;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.ChangeRecoveryPasswordModel;
import com.teecoin.model.general.SetNewRecoveryPasswordModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralChangeRecoveryPasswordRequest;
import com.teecoin.myapi.apirequest.general.GeneralSetNewRecoveryPasswordRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.KeyStoreUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ChangePasswordScreen extends TCGeneralBaseFragment implements View.OnFocusChangeListener {

    private static final String IS_RESET_PASSWORD = "IS_RESET_PASSWORD";
    private static final String VERIFICATION_CODE = "VERIFICATION_CODE";

    @BindView(R.id.view_input_password_v_input)
    View vInput;

    @BindView(R.id.view_input_password_v_confirm_input)
    View vConfirm;

    @BindView(R.id.view_input_password_tv_password)
    TextView tvPassword;

    @BindView(R.id.view_input_password_tv_password_note)
    TextView tvPasswordNote;

    @BindView(R.id.fragment_change_password_tv_confirm_password)
    TextView tvConfirmPassword;

    @BindView(R.id.view_input_password_et_input)
    EditText etPassword;

    @BindView(R.id.view_input_password_iv_clear_input)
    ImageView ivClearPassword;

    @BindView(R.id.view_input_password_tv_error_password)
    TextView tvErrorPassword;

    @BindView(R.id.view_input_password_et_confirm_input)
    EditText etConfirmPassword;

    @BindView(R.id.view_input_password_iv_clear_confirm_input)
    ImageView ivClearConfirmPassword;

    @BindView(R.id.view_input_password_tv_error_confirm_password)
    TextView tvErrorConfirmPassword;

    @BindView(R.id.tv_bt_right)
    TextView tvSubmit;

    private AccountModel accountModel;
    private boolean isResetPassword; //use for Forget Recovery Password flow
    private String verificationCode; //use for Forget Recovery Password flow

    public static ChangePasswordScreen getInstance(boolean isResetPassword, String verificationCode) {
        ChangePasswordScreen screen = new ChangePasswordScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IS_RESET_PASSWORD, isResetPassword);
        bundle.putSerializable(VERIFICATION_CODE, verificationCode);
        screen.setArguments(bundle);
        return screen;
    }

//    public static ChangePasswordScreen getInstance() {
//        return new ChangePasswordScreen();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.new_password).toUpperCase());
        showButtonBackToolbar();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isResetPassword = getArguments().getBoolean(IS_RESET_PASSWORD);
            verificationCode = getArguments().getString(VERIFICATION_CODE);
        }

        initView();

        registerSingleClick(R.id.tv_bt_right);
    }

    private void initView() {

        showKeyboard(etPassword);

        tvPassword.setVisibility(View.VISIBLE);
        tvPasswordNote.setVisibility(View.VISIBLE);
//        tvPassword.setText(Html.fromHtml(String.format("<b><font color = '#000000'>%s</font></b> &nbsp;%s"
//                , TCUtils.getString(R.string.password), TCUtils.getString(R.string.minimum_8_characters_at_least_1_upper_case))));

        tvConfirmPassword.setVisibility(View.VISIBLE);

        etPassword.setOnFocusChangeListener(this);

        etConfirmPassword.setOnFocusChangeListener(this);

        vConfirm.setVisibility(View.VISIBLE);

        tvSubmit.setText(TCUtils.getString(R.string.submit));
        tvSubmit.setVisibility(View.VISIBLE);

        TCUtils.editTextTextChange(etPassword, ivClearPassword, tvErrorPassword, vInput, null);

        TCUtils.editTextTextChange(etConfirmPassword, ivClearConfirmPassword, tvErrorConfirmPassword, vConfirm, () -> {
            if (tvErrorConfirmPassword.getVisibility() == View.VISIBLE) {
                tvErrorConfirmPassword.setText(getString(R.string.please_re_enter_your_password));
            }
        });

    }

    private void validate() {

        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));

        String password = etPassword.getText().toString();

        String confirm_password = etConfirmPassword.getText().toString();

        if (TCUtils.isEmpty(password)) {
            etPassword.requestFocus();
            tvErrorPassword.setText(getString(R.string.please_enter_your_password));
            tvErrorPassword.setVisibility(View.VISIBLE);
            TCUtils.viewInputsetError(vInput);
            return;
        }
        if (TCUtils.isEmpty(confirm_password)) {
            etConfirmPassword.requestFocus();
            tvErrorConfirmPassword.setText(getString(R.string.please_re_enter_your_password));
            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            TCUtils.viewInputsetError(vConfirm);
            return;
        }
        if (!password.equals(confirm_password)) {

            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            tvErrorConfirmPassword.setText(TCUtils.getString(R.string.validate_password_not_match));
            TCUtils.viewInputsetError(vInput);
            TCUtils.viewInputsetError(vConfirm);
            return;
        }
        if (!TCUtils.validatePassword(password)) {
            tvErrorPassword.setVisibility(View.VISIBLE);
            tvErrorPassword.setText(R.string.validate_please_enter_valid_password);
            TCUtils.viewInputsetError(vInput);
            return;
        }
        if (isResetPassword) {
            resetRecoveryPassword(verificationCode, password, confirm_password);
        } else {
            changeRecoveryPassword(verificationCode, password, confirm_password);
        }
    }

    private void resetRecoveryPassword(String verificationCode, String password, String confirm_password) {
        requestApi(new GeneralSetNewRecoveryPasswordRequest(
                new SetNewRecoveryPasswordModel(verificationCode, password, confirm_password),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                        accountModel.setPassword(KeyStoreUtility.getInstance().encryptString(password));

                        RealmController.getInstance().updateAccountModel(accountModel);

                        handleBackPressed();

                        showDialogMessageAlert(EnumMgr.EnumAlert.Success.getValue(),
                                TCUtils.getString(R.string.your_password_have_been_successfully_updated));
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                        if (!TCUtils.isEmpty(errorModel.getErrorMessage())) {

                            handleBackPressed();

                            showDialogMessageAlert(EnumMgr.EnumAlert.UnSuccess.getValue(), errorModel.getErrorMessage());
                        }
                    }
                }));
    }

    private void changeRecoveryPassword(String decryptedPass, String password, String confirm_password) {

        requestApi(new GeneralChangeRecoveryPasswordRequest(
                new ChangeRecoveryPasswordModel(decryptedPass, password, confirm_password),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                        accountModel.setPassword(KeyStoreUtility.getInstance().encryptString(password));

                        RealmController.getInstance().updateAccountModel(accountModel);

                        handleBackPressed();

                        showDialogMessageAlert(EnumMgr.EnumAlert.Success.getValue(),
                                TCUtils.getString(R.string.your_password_have_been_successfully_updated));
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                        if (!TCUtils.isEmpty(errorModel.getErrorMessage())) {

                            handleBackPressed();

                            showDialogMessageAlert(EnumMgr.EnumAlert.UnSuccess.getValue(), errorModel.getErrorMessage());

                        }
                    }
                }));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.view_input_password_et_input:

                vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gold_border_5_radius));

                vConfirm.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));

                break;

            case R.id.view_input_password_et_confirm_input:

                vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));

                vConfirm.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gold_border_5_radius));

                break;
        }
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.tv_bt_right:

                validate();

                break;
        }
    }
}
