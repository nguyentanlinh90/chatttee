package com.teecoin.feature.general.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.feature.general.accountsecurity.AccountSecurityScreen;
import com.teecoin.feature.general.accountsetting.AccountSettingScreen;
import com.teecoin.feature.general.changeLanguage.ChangeLanguageScreen;
import com.teecoin.feature.general.initsocialpassword.InitSocialPasswordScreen;
import com.teecoin.feature.general.login.ShopLoginScreen;
import com.teecoin.feature.general.mywallet.MyWalletScreen;
import com.teecoin.feature.general.secretKey.SecretKeyScreen;
import com.teecoin.feature.general.setPasscode.SetPasscodeDialog;
import com.teecoin.feature.general.setPasscode.TCSetPassCodeListener;
import com.teecoin.feature.reviewSystem.myReviews.MyReviewScreen;
import com.teecoin.feature.reviewSystem.referralDetail.ReferralDetailScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.AvatarResponseModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.general.ProfileModel;
import com.teecoin.model.general.RegisterNotifyModel;
import com.teecoin.model.general.UpdateAvatarModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralGetProfileAccountRequest;
import com.teecoin.myapi.apirequest.general.GeneralGetShopBalanceRequest;
import com.teecoin.myapi.apirequest.general.GeneralLogoutAccountRequest;
import com.teecoin.myapi.apirequest.general.GeneralUpdateAvatarRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AccountScreen extends TCGeneralBaseFragment implements TCConfirmListener, APIResponseListener, TCSetPassCodeListener {
    private static final String GOTO_SGD_WALLET = "GOTO_SGD_WALLET";
    private static final String GOTO_TEC_WALLET = "GOTO_TEC_WALLET";
    @BindView(R.id.frg_account_tv_version)
    TextView tv_version;
    @BindView(R.id.frg_account_tv_name)
    TextView tv_name;
    @BindView(R.id.frg_account_rl_account_info)
    View vAccountInfo;
    @BindView(R.id.frg_account_iv_billionaire_diamond)
    ImageView iv_billionaire_diamond;
    @BindView(R.id.frg_account_iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.frg_account_tv_change_passcode)
    View tv_change_pass_code;

    @BindView(R.id.frg_account_rl_shop_account_info)
    View vShopAccountInfo;
    @BindView(R.id.frg_account_tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.frg_account_tv_title_wallet_sgd)
    TextView tvTitleWalletSGD;
    @BindView(R.id.frg_account_tv_wallet_sgd)
    TextView tvWalletSGD;
    @BindView(R.id.frg_account_tv_title_wallet_tec)
    TextView tvTitleWalletTEC;
    @BindView(R.id.frg_account_tv_wallet_tec)
    TextView tvWalletTEC;
    @BindView(R.id.frg_account_iv_shop_avatar)
    ImageView iv_shop_avatar;

    @BindView(R.id.frg_account_rl_refer_a_friend)
    View vReferAFriend;

    private boolean isChangePassCode;
    private boolean gotoSGDWalletScreen;
    private boolean gotoTECWalletScreen;

    public static AccountScreen getInstance() {
        return new AccountScreen();
    }

    public static AccountScreen getInstance(boolean gotoSGDetScreen, boolean gotoWalletScreen) {
        AccountScreen screen = new AccountScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GOTO_SGD_WALLET, gotoSGDetScreen);
        bundle.putBoolean(GOTO_TEC_WALLET, gotoWalletScreen);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.account_user_title));
        showFooter();
        showTabMenuBottom();
        hideButtonBackToolbar();
        hideViewQrCode();
        hideButtonAddCoupon();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            gotoSGDWalletScreen = bundle.getBoolean(GOTO_SGD_WALLET);
            gotoTECWalletScreen = bundle.getBoolean(GOTO_TEC_WALLET);
            handleGotoWalletScreen();
        }
        initView();

        tv_version.setText(String.format(TCUtils.getString(R.string.string_format_1), TCUtils.getString(R.string.version_app), TCUtils.getVersionApp()));

        registerSingleClick(
                R.id.frg_account_rl_my_wallet, R.id.frg_account_rl_my_review,
                R.id.frg_account_rl_refer_a_friend, R.id.frg_account_rl_credit_card,
                R.id.frg_account_rl_my_review, R.id.frg_account_rl_setting,
                R.id.frg_account_rl_term_of_use, R.id.frg_account_ll_faq,
                R.id.frg_account_rl_avatar, R.id.frg_account_rl_set_pass_code,
                R.id.frg_account_setting_rl_languages, R.id.frg_account_ll_logout,
                R.id.frg_account_rl_security, R.id.frg_account_ll_support,
                R.id.frg_account_v_wallet_sgd, R.id.frg_account_v_wallet_tec
        );
        //((TCMainActivity)getActiveActivity()).startTransactionService(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());// temporary remove
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(
                R.id.frg_account_rl_my_wallet, R.id.frg_account_rl_my_review,
                R.id.frg_account_rl_refer_a_friend, R.id.frg_account_rl_credit_card,
                R.id.frg_account_rl_my_review, R.id.frg_account_rl_setting,
                R.id.frg_account_rl_term_of_use, R.id.frg_account_ll_faq,
                R.id.frg_account_rl_avatar, R.id.frg_account_rl_set_pass_code,
                R.id.frg_account_setting_rl_languages, R.id.frg_account_ll_logout,
                R.id.frg_account_rl_security, R.id.frg_account_ll_support,
                R.id.frg_account_v_wallet_sgd, R.id.frg_account_v_wallet_tec
        );
    }

    private void initView() {
        if (isAppUser()) {
            vShopAccountInfo.setVisibility(View.GONE);

            requestApi(new GeneralGetProfileAccountRequest(isAppUser() ? GeneralRequestTarget.GET_PROFILE_USER : GeneralRequestTarget.GET_PROFILE_SHOP, this));
            checkPasscode();
        } else {
            vReferAFriend.setVisibility(View.GONE);
            vAccountInfo.setVisibility(View.GONE);

            requestApi(new GeneralGetShopBalanceRequest(GeneralRequestTarget.SHOP_GET_BALANCE, this));

            Vendor vendor = RealmController.getInstance().getData(Vendor.class);
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(vendor.getLogo()) ? TCUtils.getDrawable(R.drawable.ic_shop_icon)
                    : vendor.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_shop_avatar);
            tvShopName.setText(vendor.getName());

            ViewTreeObserver viewTreeObserver = tvTitleWalletSGD.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tvTitleWalletSGD.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    tvTitleWalletTEC.getLayoutParams().width = tvTitleWalletSGD.getMeasuredWidth();
                }
            });
        }
    }

    private void gotoFAQ() {
        addFragment(WebViewScreen.getInstance(TCConstant.DEFAULT_FAQ_URL, TCUtils.getString(R.string.account_faq)));
    }

    private void gotoSupport() {
        addFragment(WebViewScreen.getInstance(TCConstant.DEFAULT_SUPPORT_URL, TCUtils.getString(R.string.account_support)));
    }

    private void askLogout() {
        AskLogoutDialog askLogoutDialog = new AskLogoutDialog(getActiveActivity(), this);
        askLogoutDialog.show();
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        if (id == AccountAccess.ACCOUNT_SECRET_KEY.getValue()) {
            addFragment(SecretKeyScreen.getInstance());
        } else if (id == AccountAccess.ACCOUNT_LOGOUT.getValue()) {
            handleLogout();
        }
    }

    private void checkApp() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        Glide.with(getActiveActivity()).load(TCUtils.isEmpty(accountModel.getAvatar()) ? TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)
                : accountModel.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(iv_avatar);

        tv_name.setText(isAppUser() ? accountModel.getFull_name()
                : String.format("%s\n%s", accountModel.getName(), TCUtils.getString(R.string.account_user_title)));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

        if (requestTarget == GeneralRequestTarget.UPDATE_USER_AVATAR) {

            String url = ((AvatarResponseModel) response.getResult()).getUrl();

            Glide.with(getActiveActivity()).load(url).apply(TCUtils.optionsCircleImageAvatar()).into(iv_avatar);

            AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));

            accountModel.setAvatar(url);

            RealmController.getInstance().updateAccountModel(accountModel);

        } else if (requestTarget == GeneralRequestTarget.USER_LOGOUT) {

            logout();

        } else if (requestTarget == GeneralRequestTarget.SHOP_LOGOUT) {
            RealmController.getInstance().deleteData();
            replaceFragment(ShopLoginScreen.getInstance(), true);
            hideFooter();

        } else if (requestTarget == GeneralRequestTarget.GET_PROFILE_USER || requestTarget == GeneralRequestTarget.GET_PROFILE_SHOP) {
            ProfileModel profileModel = (ProfileModel) response.getResult();
            if (profileModel != null) {
                AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
                accountModel.updateProfile(profileModel);
                RealmController.getInstance().updateAccountModel(accountModel);
                if (accountModel.getVendor() != null) {
                    RealmController.getInstance().updateVendor(accountModel.getVendor());
                }
            }
            checkApp();
        } else if (requestTarget == GeneralRequestTarget.SHOP_GET_BALANCE) {
            BalanceModel balanceModel = (BalanceModel) response.getResult();
            if (null != balanceModel) {
                tvWalletSGD.setText(Html.fromHtml(String.format("<font color = '#b2973f'>%s</font>&nbsp;" + balanceModel.getCurrency_code(),
                        TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, balanceModel.getFiat_amount()))));
                tvWalletTEC.setText(Html.fromHtml(String.format("<font color = '#b2973f'>%s</font>&nbsp;TEC",
                        TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, balanceModel.getTec_balance()))));
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == GeneralRequestTarget.GET_PROFILE_USER || requestTarget == GeneralRequestTarget.GET_PROFILE_SHOP) {
            checkApp();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActiveActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EnumMgr.RequestCode.READ_EXTERNAL_STORAGE.getValue());
        } else if (ContextCompat.checkSelfPermission(getActiveActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), EnumMgr.RequestCode.CHOOSE_PHOTO.getValue());
        ((TCMainActivity) getActiveActivity()).setClockScreen(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EnumMgr.RequestCode.CHOOSE_PHOTO.getValue() && data != null) {
            Uri selectedImage = data.getData();
            InputStream iStream = null;
            try {
                if (selectedImage != null)
                    iStream = getActiveActivity().getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            byte[] inputData = new byte[0];
            if (iStream != null) {
                inputData = TCUtils.getBytes(iStream);
            }

            RequestBody requestImage = null;
            if (inputData != null) {
                requestImage = RequestBody.create(MediaType.parse("image/jpg"), inputData);
            }

            if (requestImage != null) {
                MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                        UpdateAvatarModel.MEDIA_FIELD, UpdateAvatarModel.FILE_EXTENSION, requestImage);
                requestApi(new GeneralUpdateAvatarRequest(filePart, GeneralRequestTarget.UPDATE_USER_AVATAR, this));
            }
        }
    }

    private void handleLogout() {
        AccountModel accountModel = RealmController.getInstance().getAccount();
        RegisterNotifyModel notifyModel = new RegisterNotifyModel();
        notifyModel.setToken(accountModel.getDevice_token());
        requestApi(new GeneralLogoutAccountRequest(notifyModel,
                isAppUser() ? GeneralRequestTarget.USER_LOGOUT : GeneralRequestTarget.SHOP_LOGOUT, this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.READ_EXTERNAL_STORAGE.getValue() && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            }
        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue()) {
            refreshFragment(this);
        }
    }

    @Override
    public void onCloseApp(boolean isClose) {

    }

    @Override
    public void onUnClockSuccess(boolean success) {

    }

    @Override
    public void onFinishChangePassCode() {
        tv_change_pass_code.setVisibility(View.VISIBLE);
        isChangePassCode = true;
    }

    @Override
    public void onRemovePassCode() {
        tv_change_pass_code.setVisibility(View.GONE);
        isChangePassCode = false;
    }

    private void gotoSetPasscodeScreen(boolean isChangePassCode) {
        SetPasscodeDialog setPasscodeDialog = new SetPasscodeDialog(getActiveActivity(), false, isChangePassCode, false, this);
        setPasscodeDialog.show();
    }

    private void gotoChangeLanguage() {
        addFragmentForResult(EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue(), ChangeLanguageScreen.getInstance());
    }

    private void checkPasscode() {
        if (checkHasPasscode()) {
            tv_change_pass_code.setVisibility(View.VISIBLE);
            isChangePassCode = true;
        } else {
            tv_change_pass_code.setVisibility(View.GONE);
            isChangePassCode = false;
        }
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_account_rl_my_wallet:
                if (isAppUser())
                    addFragment(WalletAccountScreen.getInstance());
                else
                    addFragment(MyWalletScreen.getInstance(false));
                break;
            case R.id.frg_account_rl_my_review:
                addFragment(MyReviewScreen.getInstance());
                break;
            case R.id.frg_account_rl_refer_a_friend:
                addFragment(ReferralDetailScreen.getInstance());
                break;
            case R.id.frg_account_rl_credit_card:
                break;
            case R.id.frg_account_rl_setting:
                addFragmentForResult(EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue(), AccountSettingScreen.getInstance());
                break;
            case R.id.frg_account_rl_term_of_use:
                addFragment(WebViewScreen.getInstance(TCConstant.URL_TERMS, TCUtils.getString(R.string.terms_of_use)));
                break;
            case R.id.frg_account_ll_faq:
                gotoFAQ();
                break;
            case R.id.frg_account_ll_logout:
                askLogout();
                break;
            case R.id.frg_account_rl_avatar:
                checkPermission();
                break;
            case R.id.frg_account_rl_set_pass_code:
                gotoSetPasscodeScreen(isChangePassCode);
                break;
            case R.id.frg_account_setting_rl_languages:
                gotoChangeLanguage();
                break;
            case R.id.frg_account_rl_security:
                checkInitSocialPassword();
                break;
            case R.id.frg_account_ll_support:
                gotoSupport();
                break;
            case R.id.frg_account_v_wallet_sgd:
                addFragment(MyWalletScreen.getInstance(false));
                break;
            case R.id.frg_account_v_wallet_tec:
                addFragment(MyWalletScreen.getInstance(true));
                break;
        }
    }

    private void checkInitSocialPassword() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (!TCUtils.isEmpty(accountModel.getProvider())
                && (accountModel.getProvider().equals(EnumMgr.SocialProvider.Facebook.getValue())
                || accountModel.getProvider().equals(EnumMgr.SocialProvider.Google.getValue()))) {
            if (!accountModel.isHave_password()) {
                addFragment(InitSocialPasswordScreen.getInstance());
            } else {
                addFragment(AccountSecurityScreen.getInstance());
            }
        } else {
            addFragment(AccountSecurityScreen.getInstance());
        }
    }

    private void handleGotoWalletScreen() {
        if (gotoTECWalletScreen) {
            if (isAppUser()) {
                addFragment(WalletAccountScreen.getInstance());
            } else {
                addFragment(MyWalletScreen.getInstance(true));
            }
        } else if (gotoSGDWalletScreen) {
            addFragment(MyWalletScreen.getInstance(false));
        }
    }

    public enum AccountAccess {
        ACCOUNT_SECRET_KEY(1),
        ACCOUNT_LOGOUT(2);

        private int value;

        AccountAccess(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
