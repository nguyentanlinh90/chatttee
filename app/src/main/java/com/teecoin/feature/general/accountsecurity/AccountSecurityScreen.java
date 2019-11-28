package com.teecoin.feature.general.accountsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.feature.general.changeRecoveryPassword.ChangePasswordScreen;
import com.teecoin.feature.general.forgetrecoverypassword.ForgetRecoveryPasswordFlow;
import com.teecoin.feature.general.initsocialpassword.InitSocialPasswordScreen;
import com.teecoin.feature.general.secretKey.SecretKeyScreen;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordDialog;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordListener;
import com.teecoin.feature.walletSystem.paymentThreshold.PaymentThresholdScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;


public class AccountSecurityScreen extends TCGeneralBaseFragment {


    public static AccountSecurityScreen getInstance() {
        return new AccountSecurityScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_security, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.account_security).toUpperCase());
        hideViewQrCode();
        hideButtonAddCoupon();
    }

    @Override
    public void onBindView() {
        initClickEvent();
    }

    private void initClickEvent() {
        registerSingleClick(R.id.frg_account_security_rl_secret_key,
                R.id.frg_account_security_rl_change_password,
                R.id.frg_account_security_rl_forgot_password,
                R.id.frg_account_security_rl_payment_threshold);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_account_security_rl_secret_key:
                openSecretKeyScreen();
                break;
            case R.id.frg_account_security_rl_change_password:
                checkShowPassword();
                break;
            case R.id.frg_account_security_rl_forgot_password:
                showForgetRecoveryPasswordDialog();
                break;
            case R.id.frg_account_security_rl_payment_threshold:
                openPaymentThresholdScreen();
                break;
        }
    }

    private void openSecretKeyScreen() {
        inputPassword(SecretKeyScreen.getInstance(), EnumMgr.TypeDialog.InputPassword);
    }

    private void openPaymentThresholdScreen() {
        inputPassword(PaymentThresholdScreen.getInstance(), EnumMgr.TypeDialog.InputPassword);
    }

    private void checkShowPassword() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (!TCUtils.isEmpty(accountModel.getProvider())
                && (accountModel.getProvider().equals(EnumMgr.SocialProvider.Facebook.getValue())
                || accountModel.getProvider().equals(EnumMgr.SocialProvider.Google.getValue()))) {
            if (!accountModel.isHave_password()) {
                addFragment(InitSocialPasswordScreen.getInstance());
            } else {
                showPopupChangePasswordPassword();
            }
        } else {
            showPopupChangePasswordPassword();
        }
    }

    private void showPopupChangePasswordPassword() {

        //inputPassword(ChangePasswordScreen.getInstance(), EnumMgr.TypeDialog.ChangePassword);

        InputPasswordDialog dialog = new InputPasswordDialog(getActiveActivity(), EnumMgr.TypeDialog.ChangePassword.getValue(), new InputPasswordListener() {
            @Override
            public void onSubmit(String inputPassword) {
                addFragment(ChangePasswordScreen.getInstance(false, inputPassword));
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }


    private void showForgetRecoveryPasswordDialog() {
        new ForgetRecoveryPasswordFlow(getActiveActivity(),
                verificationCode -> addFragment(
                        ChangePasswordScreen.getInstance(true, verificationCode)));
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue()) {
            refreshFragment(this);
        }
    }
}
