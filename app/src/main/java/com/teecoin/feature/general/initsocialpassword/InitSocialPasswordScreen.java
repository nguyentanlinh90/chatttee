package com.teecoin.feature.general.initsocialpassword;

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
import com.teecoin.base.TCSuccessfulDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralInitSocialPasswordRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.KeyStoreUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class InitSocialPasswordScreen extends TCWalletBaseFragment {

    @BindView(R.id.fragment_init_social_password_v_input_password)
    View vInputPassword;

    @BindView(R.id.fragment_init_social_password_et_input_password)
    EditText etInputPassword;

    @BindView(R.id.fragment_init_social_password_iv_clear_input_password)
    ImageView ivClearPassword;

    @BindView(R.id.fragment_init_social_password_tv_error_input_password)
    TextView tvErrorPassword;

    @BindView(R.id.fragment_init_social_password_v_confirm_password)
    View vInputConfirmPassword;

    @BindView(R.id.fragment_init_social_password_et_confirm_password)
    EditText etInputConfirmPassword;

    @BindView(R.id.fragment_init_social_password_iv_clear_confirm_password)
    ImageView ivClearConfirmPassword;

    @BindView(R.id.fragment_init_social_password_tv_error_confirm_password)
    TextView tvErrorConfirmPassword;


    public static InitSocialPasswordScreen getInstance() {
        return new InitSocialPasswordScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_init_social_password, container, false);
    }

    @Override
    public void onBaseResume() {
        showHeader();
        showFooter();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.new_password).toUpperCase());
    }

    @Override
    public void onBindView() {
        super.onBindView();
        initView();
    }

    private void initView() {

        TCUtils.editTextTextChange(etInputPassword, ivClearPassword, tvErrorPassword, vInputPassword, null);

        TCUtils.setBgWhenFocusView(etInputPassword, vInputPassword, vInputConfirmPassword);

        TCUtils.editTextTextChange(etInputConfirmPassword, ivClearConfirmPassword, tvErrorConfirmPassword, vInputConfirmPassword, null);

        TCUtils.setBgWhenFocusView(etInputConfirmPassword, vInputConfirmPassword, vInputPassword);

        registerSingleClick(R.id.tv_bt_right);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.tv_bt_right:
                validate();
        }
    }

    private void initSocialPassword() {
        requestApi(new GeneralInitSocialPasswordRequest(etInputPassword.getText().toString(), etInputConfirmPassword.getText().toString(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getSuccess()) {
                    new TCSuccessfulDialog(getActiveActivity(),
                            TCUtils.getString(R.string.text_successful),
                            TCUtils.getString(R.string.your_password_have_been_successfully_updated),
                            TCUtils.getString(R.string.text_done), null).show();

                    AccountModel accountModel = RealmController.getInstance().getAccount();
                    accountModel.setHave_password(true);
                    accountModel.setPassword(KeyStoreUtility.getInstance().encryptString(etInputPassword.getText().toString().trim()));
                    RealmController.getInstance().updateAccountModel(accountModel);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }


    @Override
    public void onBaseDestroyView() {

        unregisterSingleClick(R.id.tv_bt_right);
    }

    public void validate() {

        if (etInputPassword.getText().toString().isEmpty()) {
            tvErrorPassword.setText(TCUtils.getString(R.string.please_enter_your_password));
            tvErrorPassword.setVisibility(View.VISIBLE);
            etInputPassword.requestFocus();
            return;
        }

        if (!TCUtils.validatePassword(etInputPassword.getText().toString())) {
            tvErrorPassword.setText(TCUtils.getString(R.string.the_password_entered_is_invalid));
            tvErrorPassword.setVisibility(View.VISIBLE);
            etInputPassword.requestFocus();
            return;
        }

        if (etInputConfirmPassword.getText().toString().isEmpty()) {
            tvErrorConfirmPassword.setText(TCUtils.getString(R.string.please_enter_your_password));
            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            etInputConfirmPassword.requestFocus();
            return;
        }

        if (!etInputPassword.getText().toString().equals(etInputConfirmPassword.getText().toString())) {
            tvErrorConfirmPassword.setText(TCUtils.getString(R.string.validate_password_not_match));
            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
            etInputConfirmPassword.requestFocus();
            return;
        }

        initSocialPassword();

    }
}
