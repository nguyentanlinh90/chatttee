package com.teecoin.feature.general.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.feature.general.inputReferalCode.InputReferralCodeScreen;
import com.teecoin.feature.general.login.LoginScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.GuidelineAdapter;
import com.teecoin.model.general.GuidelineModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class NewWelcomeScreen extends TCGeneralBaseFragment {
    @BindView(R.id.view_button_signup_tv_signup)
    TextView tv_signup;
    @BindView(R.id.frg_welcome_tv_terms_of_use)
    TextView tv_terms_of_use;
    @BindView(R.id.frg_welcome_vp_guideline)
    ViewPager viewPager;
    @BindView(R.id.frg_welcome_cip_guideline_indicator)
    CircleIndicatorPager cpPage;
    @BindView(R.id.frg_welcome_iv_chattee_logo)
    View iv_chattee_logo;


    public static NewWelcomeScreen getInstance() {
        return new NewWelcomeScreen();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_welcome_screen, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideFooter();
        hideHeader();
    }


    @Override
    public void onBindView() {

        tv_signup.setText(TCUtils.getString(R.string.text_sign_up));
        //GetKeyHash.printKeyHash(getActiveActivity());

        ArrayList<GuidelineModel> guidelineModels = ((TCMainActivity) getActiveActivity()).getGuideLineData();
        GuidelineAdapter guidelineAdapter = new GuidelineAdapter(getContext(), guidelineModels);
        if (guidelineModels != null && guidelineModels.size() > 0) {
            viewPager.setAdapter(guidelineAdapter);
            cpPage.setViewPager(viewPager);
            viewPager.setVisibility(View.VISIBLE);
            cpPage.setVisibility(View.VISIBLE);
            iv_chattee_logo.setVisibility(View.GONE);
        } else {
            iv_chattee_logo.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            cpPage.setVisibility(View.GONE);
        }

        initClickEvent();
        tv_terms_of_use.setText(Html.fromHtml(TCUtils.getString(R.string.singin_terms_of_use)));
        ((TCMainActivity) getActiveActivity()).handleDeepLinkIntent(getActiveActivity().getIntent());
    }

    private void initClickEvent() {
        registerSingleClick(R.id.frg_welcome_tv_login, R.id.view_button_signup_ll_sign_up_with_email, R.id.frg_welcome_tv_terms_of_use, R.id.view_button_signup_ll_sign_up_with_facebook, R.id.view_button_signup_ll_sign_up_with_google);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_button_signup_ll_sign_up_with_email:
                if (isEmulator() && TCUtils.isProductionMode()) {
                    showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.you_cant_create_account_on_emulator), TCUtils.getString(R.string.text_ok), null, null);
                } else {
                    addFragment(InputReferralCodeScreen.getInstance(EnumMgr.SignUpType.Email));
                }
                break;
            case R.id.frg_welcome_tv_login:
                addFragment(LoginScreen.getInstance());
                break;
            case R.id.frg_welcome_tv_terms_of_use:
                openTermsScreen();
                break;
            case R.id.view_button_signup_ll_sign_up_with_facebook:
                // signInWithFacebook();
                if (isEmulator() && TCUtils.isProductionMode()) {
                    showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.you_cant_create_account_on_emulator), TCUtils.getString(R.string.text_ok), null, null);
                } else {
                    addFragment(InputReferralCodeScreen.getInstance(EnumMgr.SignUpType.Facebook));
                }
                break;
            case R.id.view_button_signup_ll_sign_up_with_google:
                //signInWithGoogle();
                if (isEmulator() && TCUtils.isProductionMode()) {
                    showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.you_cant_create_account_on_emulator), TCUtils.getString(R.string.text_ok), null, null);
                } else {
                    addFragment(InputReferralCodeScreen.getInstance(EnumMgr.SignUpType.Google));
                }
                break;
        }
    }


    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frg_welcome_tv_login, R.id.view_button_signup_ll_sign_up_with_email, R.id.frg_welcome_tv_terms_of_use, R.id.view_button_signup_ll_sign_up_with_facebook, R.id.view_button_signup_ll_sign_up_with_google);
    }
}
