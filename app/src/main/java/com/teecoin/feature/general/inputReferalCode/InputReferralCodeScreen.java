package com.teecoin.feature.general.inputReferalCode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCSignUpBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.signupaccount.SignUpAccountEmailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralCheckRewardStatusRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserCheckReferralCodeRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class InputReferralCodeScreen extends TCSignUpBaseFragment {
    private static final String TYPE_SIGNUP = "TYPE_SIGNUP";
    @BindView(R.id.frg_input_referral_nested_scroll)
    NestedScrollView nested_scroll;
    @BindView(R.id.frg_input_referral_input)
    View vInput;
    @BindView(R.id.frg_input_referral_tv_message_input)
    TextView tv_message_input;
    @BindView(R.id.frg_input_referral_tv_content)
    TextView tv_content;
    @BindView(R.id.frg_input_referral_ed_code)
    EditText ed_code;
    @BindView(R.id.frg_input_referral_tv_not_have_referral)
    TextView tvNotHaveReferral;
    @BindView(R.id.frg_input_referral_tv_welcome_text)
    TextView tv_welcome_text;
    @BindView(R.id.frg_input_referral_iv_gift)
    ImageView iv_gift;
    @BindView(R.id.login_button)
    LoginButton btFacebook;
    @BindView(R.id.sign_in_button)
    SignInButton btGoogle;
    private EnumMgr.SignUpType typeSignup;
    private String titleToolbar = "";

    public static InputReferralCodeScreen getInstance(EnumMgr.SignUpType typeSignup) {
        InputReferralCodeScreen screen = new InputReferralCodeScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TYPE_SIGNUP, typeSignup);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_referral_code, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        hideMenuNextBottom();
        showCancelNextBottomView();
        setTextForNextBottomView(TCUtils.getString(R.string.text_next));
        setTextForCancelBottomView(TCUtils.getString(R.string.referral_code_skip));
        updateTitleHeaderLowerCase(titleToolbar);
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeSignup = (EnumMgr.SignUpType) bundle.getSerializable(TYPE_SIGNUP);
        }
        disconnectFromFacebook();

        tvNotHaveReferral.setText(Html.fromHtml(TCUtils.getString(R.string.input_referral_code_do_you_not_have_referral_code)));
        tv_message_input.setVisibility(View.GONE);
        checkRewardStatus();
        if (btFacebook != null)
            setupFacebook(btFacebook, true);
        if (btGoogle != null)
            setupGoogle(btGoogle, true);
        ed_code.setOnFocusChangeListener((v, hasFocus) -> scrollToBottom());
        ed_code.setOnClickListener(v -> scrollToBottom());
        iv_gift.setVisibility(View.INVISIBLE);
        tv_content.setVisibility(View.INVISIBLE);
        tv_welcome_text.setVisibility(View.INVISIBLE);

    }

    private void scrollToBottom() {
        nested_scroll.postDelayed(() -> {
            View lastChild = nested_scroll.getChildAt(nested_scroll.getChildCount() - 1);
            int bottom = lastChild.getBottom() + nested_scroll.getPaddingBottom();
            int sy = nested_scroll.getScrollY();
            int sh = nested_scroll.getHeight();
            int delta = bottom - (sy + sh);
            nested_scroll.smoothScrollBy(0, delta);
        }, 200);
    }

    private void checkRewardStatus() {
        requestApi(new GeneralCheckRewardStatusRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                tv_welcome_text.setText(TCUtils.getString(response.getSuccess() ?
                        R.string.input_referral_code_welcome_chattee_
                        : R.string.input_referral_code_welcome_back));

                tv_content.setText(TCUtils.getString(response.getSuccess() ?
                        R.string.input_referral_code_content_not_been_registered
                        : R.string.input_referral_code_content_has_been_registered));

                updateTitleHeaderLowerCase(TCUtils.getString(response.getSuccess() ?
                        R.string.input_referral_code_welcome
                        : R.string.input_referral_code_welcome_chattee));
                titleToolbar = (TCUtils.getString(response.getSuccess() ?
                        R.string.input_referral_code_welcome
                        : R.string.input_referral_code_welcome_chattee));

                Glide.with(getActiveActivity()).load(
                        TCUtils.getDrawable(response.getSuccess() ?
                                R.drawable.ic_input_referral_welcome
                                : R.drawable.ic_input_referral_welcome_back))
                        .into(iv_gift);
                iv_gift.setVisibility(View.VISIBLE);
                tv_content.setVisibility(View.VISIBLE);
                tv_welcome_text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    public void checkReferralCode() {
        if (TCUtils.isEmpty(ed_code.getText().toString())) {
            vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius));
            tv_message_input.setVisibility(View.VISIBLE);
            return;
        }

        validationReferralCode();
    }

    private void validationReferralCode() {
        requestApi(new ReviewUserCheckReferralCodeRequest(ed_code.getText().toString().trim(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                handleSignUp(ed_code.getText().toString());
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.invalid_referral_code),
                        errorModel.getErrorMessage(), TCUtils.getString(R.string.try_again)).show();
//                R.string.the_referral_code_you_enter_is_invalid
            }
        }));
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
    }

    public void handleSignUp(String referral_code) {
        if (typeSignup == EnumMgr.SignUpType.Facebook) {
            signInWithFacebook(referral_code);
        } else if (typeSignup == EnumMgr.SignUpType.Google) {
            signInWithGoogle(referral_code);
        } else if (typeSignup == EnumMgr.SignUpType.Email) {
            AccountModel accountModel = new AccountModel();
            accountModel.setReferralCode(referral_code);
            TCAppFlyerTrackingEvent.getInstance().trackCreateWalletInit();
            addFragment(SignUpAccountEmailScreen.getInstance(true, accountModel));
        }
    }
}