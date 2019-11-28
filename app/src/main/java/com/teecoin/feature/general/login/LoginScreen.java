package com.teecoin.feature.general.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.teecoin.R;
import com.teecoin.base.TCSignUpBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.loginwithemail.LoginWithEmailScreen;
import com.teecoin.feature.walletSystem.importSecretKey.ImportSecretKeyScreen;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class LoginScreen extends TCSignUpBaseFragment {

    @BindView(R.id.view_button_signup_tv_signup)
    TextView tvSignUp;

    @BindView(R.id.login_button)
    LoginButton btFacebook;

    @BindView(R.id.sign_in_button)
    SignInButton btGoogle;


    public static LoginScreen getInstance() {
        return new LoginScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        hideFooter();
        updateTitleHeaderLowerCase(TCUtils.getString(R.string.welcome_back));
    }

    @Override
    public void onBindView() {
        disconnectFromFacebook();
        setupFacebook(btFacebook, false);
        setupGoogle(btGoogle, false);
        tvSignUp.setText(TCUtils.getString(R.string.text_login));
        initClickEvent();
    }

    private void initClickEvent() {
        registerSingleClick(R.id.fragment_login_tv_import_wallet,
                R.id.view_button_signup_ll_sign_up_with_email,
                R.id.frg_welcome_tv_terms_of_use,
                R.id.view_button_signup_ll_sign_up_with_facebook,
                R.id.view_button_signup_ll_sign_up_with_google);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.view_button_signup_ll_sign_up_with_facebook:

                signInWithFacebook("");

                break;
            case R.id.view_button_signup_ll_sign_up_with_google:

                signInWithGoogle("");

                break;

            case R.id.view_button_signup_ll_sign_up_with_email:

                addFragment(LoginWithEmailScreen.getInstance(""));

                break;

            case R.id.fragment_login_tv_import_wallet:

                gotoImportWallet();

                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.fragment_login_tv_import_wallet,
                R.id.frg_welcome_tv_login,
                R.id.view_button_signup_ll_sign_up_with_email,
                R.id.frg_welcome_tv_terms_of_use,
                R.id.view_button_signup_ll_sign_up_with_facebook,
                R.id.view_button_signup_ll_sign_up_with_google);
    }

    private void gotoImportWallet() {
        TCAppFlyerTrackingEvent.getInstance().trackImportWalletInit();
        addFragment(ImportSecretKeyScreen.getInstance());
    }


}
