package com.teecoin.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.loginwithemail.LoginWithEmailScreen;
import com.teecoin.feature.general.signupfacebook.DialogSignUpFail;
import com.teecoin.feature.general.signupfacebook.SignUpAccountWithFaceBookScreen;
import com.teecoin.feature.general.signupfacebook.SignUpFailListener;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.SocialInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralSignUpSocialRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.Arrays;

public class TCSignUpBaseFragment extends TCGeneralBaseFragment implements GoogleApiClient.OnConnectionFailedListener, APIResponseListener, SignUpFailListener {

    private static final String FB_FIELDS = "fields";
    private static final String FB_FIELDS_PARAMS = "id,name,email,gender, birthday,picture.type(large)";
    SignInButton btGoogle;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton btFacebook;

    private CallbackManager callbackManager;

    private boolean isSignUp;

    private String referral_code = "";
    public EnumMgr.SignUpType signUpType;

    private SocialInfoModel socialInfoModel;

    private String email = "";

    @Override
    public void onBindView() {

        FacebookSdk.sdkInitialize(getActiveActivity());
        disconnectFromFacebook();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupFacebook(LoginButton login_button, boolean isSignUp) {
        this.isSignUp = isSignUp;
        btFacebook = login_button;
        callbackManager = CallbackManager.Factory.create();
        //  btFacebook.setReadPermissions("public_profile", "email", "user_friends");

        btFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //   TCLog.e("loginResult "+loginResult.toString());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            socialInfoModel = new SocialInfoModel(
                                    object, AccessToken.getCurrentAccessToken(), referral_code);
                            email = socialInfoModel.getEmail();
                            submitSocialInfo();
                        });
                Bundle parameters = new Bundle();
                parameters.putString(FB_FIELDS, FB_FIELDS_PARAMS);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                TCLog.d("Facebok onCancel ");
            }

            @Override
            public void onError(FacebookException error) {
                TCLog.d("FacebookException " + error.getMessage());
            }
        });

    }

    public void setupGoogle(SignInButton signInButton, boolean isSignUp) {
        this.isSignUp = isSignUp;
        btGoogle = signInButton;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(TCUtils.getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActiveActivity())
                .enableAutoManage(getActiveActivity() /* FragmentActivity */, 0, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btGoogle.setScopes(gso.getScopeArray());
        // logout
        //logoutGoogle();
    }

    @Override
    public void onPause() {
        super.onPause();
        logoutGoogle();

    }

    private void logoutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    status -> {
                        TCLog.e("logount google ");
                    });
            mGoogleApiClient.stopAutoManage(getActiveActivity());
            mGoogleApiClient.disconnect();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        //  TCLog.e("requestCode 1 "+requestCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EnumMgr.RequestCode.GOOGLE_LOGIN.getValue()) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInGoogleResult(task);

        }

    }

    private void handleSignInGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String token = account.getIdToken();
                String personName = account.getDisplayName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                // TCLog.e("token google "+token);
                socialInfoModel = new SocialInfoModel(EnumMgr.SocialProvider.Google.getValue(), personId, token, personName, personEmail, referral_code);
                email = account.getEmail();
                submitSocialInfo();
                //sendGoogleToken(account.getIdToken(),account.getId());
            } else {
                TCLog.e("signInResult:account is null");
                showBaseMessage("GoogleSignInAccount is null");
            }
        } catch (ApiException e) {
            if (e.getStatusCode() == EnumMgr.GoogleSignInStatusCode.SignInCancel.getValue()) {
                //do nothing
            } else if (e.getStatusCode() == EnumMgr.GoogleSignInStatusCode.SignInCurrentlyInProgress.getValue()) {
                showBaseMessage("SignIn in progress");
            } else if (e.getStatusCode() == EnumMgr.GoogleSignInStatusCode.SignInFail.getValue()) {
                showBaseMessage("SignIn Fail");
            } else {
                showBaseMessage("SignIn Error:" + e.getMessage());
            }

        }
    }

    public void submitSocialInfo() {
        if (socialInfoModel == null)
            return;
        if (TCUtils.isEmpty(socialInfoModel.getAccess_token())) {
            showBaseMessage("Error get Token");
            return;
        }
        if (isSignUp) {
            SignUpAccountWithFaceBookScreen signUpAccountWithFaceBookScreen = SignUpAccountWithFaceBookScreen.newInstance(socialInfoModel);
            signUpAccountWithFaceBookScreen.setButtonFacebook(btFacebook);
            addFragment(signUpAccountWithFaceBookScreen);
        } else {
            requestApi(new GeneralSignUpSocialRequest(socialInfoModel, GeneralRequestTarget.SOCIAL_LOGIN, this));
        }

    }

    public void signInWithFacebook(String referral_code) {
        this.referral_code = referral_code;
        if (btFacebook == null) {
            TCLog.e("button login googl is null");
            return;
        }
        signUpType = EnumMgr.SignUpType.Facebook;
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    public void signInWithGoogle(String referral_code) {
        this.referral_code = referral_code;
        if (btGoogle == null) {
            TCLog.e("button login googl is null");
            return;
        }
        signUpType = EnumMgr.SignUpType.Google;
        btGoogle.performClick();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, EnumMgr.RequestCode.GOOGLE_LOGIN.getValue());
    }


    @Override
    public void doLogin() {
        if (signUpType == EnumMgr.SignUpType.Email) {
            addFragment(LoginWithEmailScreen.getInstance(email));
        } else {
            isSignUp = false;
            submitSocialInfo();
        }

    }

    @Override
    public void doRestart() {
        handleBackPressed();
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == GeneralRequestTarget.SOCIAL_REGISTER || requestTarget == GeneralRequestTarget.SOCIAL_LOGIN) {
            AccountModel accountModel = (AccountModel) response.getResult();
            accountModel.setLogin(true);
            // check InputPasswordScreen
            TCSharePreferenceManager.getInstance().setString(DataKey.Token, accountModel.getToken());
            ((TCMainActivity) getActiveActivity()).insertInitData(accountModel, () -> openHomeScreen());
            if (signUpType == EnumMgr.SignUpType.Facebook) {
                TCAppFlyerTrackingEvent.getInstance().trackWalletCreationSuccessFacebook();
            } else if (signUpType == EnumMgr.SignUpType.Google) {
                TCAppFlyerTrackingEvent.getInstance().trackWalletCreationSuccessGoogle();
            }

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
                                }, this);
                    }
                }

                @Override
                public void onStellarFail(Throwable t) {
                }
            });

        }
        showLoading(false);
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

        if (requestTarget == GeneralRequestTarget.SOCIAL_REGISTER) {
            TCLog.e("errorModel " + errorModel.getError_code());
            if (!TCUtils.isEmpty(errorModel.getError_code())) {
                if (errorModel.getError_code().equals(EnumMgr.ErrorCodeSocialRegister.ERR004.getValue())) {
                    signUpType = EnumMgr.SignUpType.Email;
                    new DialogSignUpFail(getActiveActivity(),
                            TCUtils.getString(R.string.email_exists),
                            errorModel.getErrorMessage(),
                            signUpType, false, this).show();
                } else if (errorModel.getError_code().equals(EnumMgr.ErrorCodeSocialRegister.ERR073.getValue())) {
                    new DialogSignUpFail(getActiveActivity(),
                            TCUtils.getString(R.string.account_exists),
                            errorModel.getErrorMessage(),
                            signUpType, false, this).show();
                } else if (errorModel.getError_code().equals(EnumMgr.ErrorCodeSocialRegister.ERR074.getValue())) {
                    signUpType = EnumMgr.SignUpType.Google;
                    new DialogSignUpFail(getActiveActivity(),
                            TCUtils.getString(R.string.account_exists),
                            errorModel.getErrorMessage(),
                            signUpType, false, this).show();
                } else {
                    new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.try_again)).show();
                }
            } else {
                new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.try_again)).show();

            }


        } else if (requestTarget == GeneralRequestTarget.SOCIAL_LOGIN) {
            new DialogSignUpFail(getActiveActivity(),
                    TCUtils.getString(R.string.account_does_not_exists),
                    errorModel.getErrorMessage(),
                    signUpType, true, this).show();
        }
        showLoading(false);
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        showLoading(true);

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                showLoading(false);
            }
        }).executeAsync();
    }
}
