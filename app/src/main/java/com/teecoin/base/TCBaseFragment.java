package com.teecoin.base;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.loginwithemail.LoginWithEmailScreen;
import com.teecoin.feature.general.notification.FCMHandler;
import com.teecoin.feature.general.splash.SplashScreen;
import com.teecoin.feature.general.welcome.NewWelcomeScreen;
import com.teecoin.feature.reviewSystem.vendorDetail.VendorDetailScreen;
import com.teecoin.feature.walletSystem.recoveryPassword.InputPasswordScreen;
import com.teecoin.feature.walletSystem.scanqrCode.ScanQRCodeActivity;
import com.teecoin.feature.walletSystem.verifyphonenumber.VerifyPhoneNumberScreen;
import com.teecoin.javastellarsdk.stellar.TCGoogleAnalyticEventTrackingListener;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.BitMapSelectModel;
import com.teecoin.model.couponsystem.BitMapUnSelectModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.RegisterNotifyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.APIBaseRequest;
import com.teecoin.myapi.apirequest.general.GeneralNotificationRegistrationRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarObserver;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.stellar.StellarSubscriber;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.KeyboardManager;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import org.stellar.sdk.KeyPair;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import core.base.BaseDialog;
import core.base.BaseFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class TCBaseFragment extends BaseFragment implements TCBaseInterface, APIResponseListener {

    private Unbinder unbinder;
    private TCBaseAlertDialog dialogForceLogin;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, getView());
        onBindView();
        displayView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitleRightHeader("");

        analyticTrackScreen();
        onBaseResume();
    }

    public void onBaseResume() {
        hideKeyBoardEditText();
        hideFilterToolbar();
        showReadAllNotification(false);
    }

    public void displayView() {

    }

    public void hideKeyBoardEditText() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    public void hideKeyBoard() {
        KeyboardManager.hideSoftKeyboard(getActiveActivity());
    }

    public void showKeyboard(EditText editText) {
        KeyboardManager.showSoftKeyboardWithFocus(getActiveActivity(), editText);
    }

    protected void hideHeader() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideHeader();
        }
    }

    protected void hideFooter() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideFooter();
        }
    }

    protected void showFooter() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showFooter();
        }
    }

    protected void showHeader() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showHeader();
        }
    }

    protected void updateTitleHeader(String title) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).updateTitleHeader(title);
        }
    }

    protected void updateTitleHeaderLowerCase(String title) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).updateTitleHeaderLowerCase(title);
        }
    }

    protected void updateTitleRightHeader(String title) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).updateTitleRightHeader(title);
        }
    }

    protected void setOnClickListenerForTitleRightHeader(View.OnClickListener onClickListener) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).setOnClickListenerForTitleRightHeader(onClickListener);
        }
    }

    protected void showButtonBackToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showButtonBackToolbar();
        }
    }

    protected void hideButtonBackToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideButtonBackToolbar();
        }
    }

    protected void showViewQrCode() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showViewQrCode();
        }
    }

    protected void hideViewQrCode() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideViewQrCode();
        }
    }

    protected void loadQrCodeImage(String url) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).loadQrCodeImage(url);
        }
    }

    protected void showButtonAddCoupon() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showButtonAddCoupon();
        }
    }

    protected void hideButtonAddCoupon() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideButtonAddCoupon();
        }
    }

    protected void hideButtonDone() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideButtonDone();
        }
    }

    protected void showButtonDone() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showButtonDone();
        }
    }

    protected void hideButtonNext() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideButtonNext();
        }
    }

    public void showButtonNext() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showButtonNext();
        }
    }

    protected void showMenuNextBottom() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showMenuNextBottom();
        }
    }

    protected void hideMenuNextBottom() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideMenuNextBottom();
        }
    }

    protected void showTabMenuBottom() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showTabMenuBottom();
        }
    }

    protected void showFilterToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showFilterToolbar();
        }
    }

    protected void hideFilterToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideFilterToolbar();
        }
    }

    protected void showTextViewDoneToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showTextViewDone();
        }
    }

    protected void hideTextViewDoneToolbar() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideTextViewDone();
        }
    }

    protected void showCancelNextBottomView() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showCancelNextBottomView();
        }
    }

    protected void hideCancelNextBottomView() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).hideCancelNextBottomView();
        }
    }

    protected void setTextForNextBottomView(String text) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).setTextForNextBottomView(text);
        }
    }

    protected void setTextForCancelBottomView(String text) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).setTextForCancelBottomView(text);
        }
    }


    protected void handleBackPressed() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).handleBackPressed();
        }
    }

    protected void showBaseMessage(String title) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(title);
        }
    }

    protected void showDialogMessageAlert(boolean isSuccess, String message) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showDialogMessageAlert(isSuccess, message);
        }
    }

    protected void openWalletScreen() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).openWalletScreen();
        }
    }

    protected void openHomeScreen() {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).openHomeScreen();
        }
    }

    protected void openWalletScreen(boolean SGDWallet, boolean TecWallet) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).openWalletScreen(SGDWallet, TecWallet);
        }
    }

    protected void showMessage(String message) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(message);
        }
    }

    protected void showReadAllNotification(boolean show) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showReadAllNotification(show);
        }
    }

    protected void showViewNotification(boolean show) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showViewNotification(show);
        }
    }

    protected void setNumberNotification(String numder) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).setNumberNotification(numder);
        }
    }

    protected void showLoading(boolean show) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showLoading(show);
        }
    }

    @Override
    public void onDestroy() {
        if (getView() != null && getActiveActivity() != null && unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public final void requestApi(APIBaseRequest apiBaseRequest) {
        if (getActivity() != null
                && getActivity() instanceof TCBaseActivity)
            ((TCBaseActivity) getActivity()).requestApi(apiBaseRequest);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof TCBaseActivity)
            ((TCBaseActivity) getActiveActivity()).requestApi(apiBaseRequest);
    }

    @Override
    public final boolean isAppUser() {
        return TCUtils.isUserApp();
    }

    public void gotoScanQRCode(int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanQR(requestCode);
        }
    }

    protected void startScanQR(int requestCode) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setCaptureActivity(ScanQRCodeActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue()) {
            integrator.addExtra(TCConstant.SCAN_QR_CODE_WITH_VALIDATE_CODE, String.valueOf(EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue()));
        } else if (requestCode == EnumMgr.RequestCode.SCAN_USER_PUBLIC_KEY.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_SEND_MONEY.getValue()
        ) {
            integrator.addExtra(TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY, TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY);
        } else if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue()) {
            integrator.addExtra(TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY, String.valueOf(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue()));
        } else if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()) {
            integrator.addExtra(TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY, String.valueOf(EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()));
        }
        startActivityForResult(integrator.createScanIntent(), requestCode);
        ((TCMainActivity) getActiveActivity()).setClockScreen(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_SEND_MONEY.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_REWARD.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_ORDER.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_USER_PUBLIC_KEY.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()
                || requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue()
        ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanQR(requestCode);
            }
        }
    }

    protected void showScanFailDialog(int typeScan) {
        showDecisionDialog(View.NO_ID, TCUtils.getString(R.string.text_warning),
                TCUtils.getString(R.string.scan_qr_code_fail_do_you_want_scan_again),
                TCUtils.getString(R.string.text_yes), TCUtils.getString(R.string.text_no), "",
                new TCDecisionListener() {
                    @Override
                    public void onPositiveButtonClicked(int id, Object onWhat) {
                        gotoScanQRCode(typeScan);
                    }

                    @Override
                    public void onNegativeButtonClicked(int id, Object onWhat) {

                    }

                    @Override
                    public void onNeutralButtonClicked(int id, Object onWhat) {

                    }
                }, null);

    }

    protected void stellarGetBalance(
            String publicKey,
            boolean isLoading,
            StellarResponseListener listener) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).stellarGetBalance(publicKey, isLoading, listener);
        }
    }

    protected void stellarTrustTeeCoin(String sourceSecretSeed, boolean isLoading, TCGoogleAnalyticEventTrackingListener eventListener, StellarResponseListener listener) {
        if (!TCUtils.isNetworkConnectionAvailable()) {
            hideLoadingDialog();
            return;
        }
        if (isLoading)
            showLoadingDialog();
        StellarObserver.getInstance().trustTeeCoin(sourceSecretSeed, eventListener)
                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new StellarSubscriber<>(listener));
    }


    protected String stellarGetPublicKey(String sourceSecretSeed) {
        try {
            KeyPair keyPair = KeyPair.fromSecretSeed(sourceSecretSeed);
            if (keyPair != null) {
                return keyPair.getAccountId();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    protected boolean isTransferCoinToYourself(String publicKey) {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null && accountModel.getPublic_key().equals(publicKey)) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.cant_transfer_coin_to_yourself), TCUtils.getString(R.string.text_ok), null, null);
            return true;
        }
        return false;
    }

    protected boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.PRODUCT.equals("\".*_?sdk_?.*\"")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.PRODUCT.equals("vbox86p");
    }

    protected void isAccountTrustedWithTeeCoin(String publicKey, TCGoogleAnalyticEventTrackingListener eventListener, StellarResponseListener listener) {
        if (!TCUtils.isNetworkConnectionAvailable()) {
            hideLoadingDialog();
            return;
        }
        StellarObserver.getInstance().isAccountTrustedWithTeeCoin(publicKey, eventListener)
                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new StellarSubscriber<>(listener));
    }

    protected void nestedScrollToTop(NestedScrollView nestedScrollView) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(nestedScrollView, "scrollY", 0);
        objectAnimator.setDuration(1000);
        objectAnimator.start();

    }

    protected void nestedScrollToTopRecyclerView(TCRecyclerView recyclerView) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(recyclerView, "scrollY", 0);
        objectAnimator.setDuration(1000);
        objectAnimator.start();

    }

    protected void checkDeviceTokenExists() {
        AccountModel accountModel = RealmController.getInstance().getAccount();
        if (accountModel != null) {
            if (TCUtils.isEmpty(accountModel.getDevice_token())) {
                String deviceToken = new FCMHandler(getActiveActivity()).enableFCM();
                accountModel.setDevice_token(deviceToken);
                if (!TCUtils.isEmpty(deviceToken)) {
                    sendDeviceTokenToServer(accountModel);
                }
            }
        }
    }

    private void sendDeviceTokenToServer(AccountModel accountModel) {
        requestApi(new GeneralNotificationRegistrationRequest(new RegisterNotifyModel(accountModel.getDevice_token()),
                isAppUser() ? GeneralRequestTarget.USER_REGISTER_NOTIFY : GeneralRequestTarget.SHOP_REGISTER_NOTIFY, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == GeneralRequestTarget.USER_REGISTER_NOTIFY || requestTarget == GeneralRequestTarget.SHOP_REGISTER_NOTIFY) {
                    RealmController.getInstance().updateDeviceToken(accountModel);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                if (requestTarget == GeneralRequestTarget.USER_REGISTER_NOTIFY || requestTarget == GeneralRequestTarget.SHOP_REGISTER_NOTIFY) {
                    TCLog.e(" token fail");
                }
            }
        }));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        if (this instanceof UserMyCouponDetailScreen) {
            bundle.putString(TCGoogleAnalyticTrackingEvent.COUPON_ID, ((UserMyCouponDetailScreen) this).getCouponIdForGoogleAnalyticTracking());
        } else if (this instanceof VendorDetailScreen) {
            bundle.putString(TCGoogleAnalyticTrackingEvent.VENDOR_ID, ((VendorDetailScreen) this).getVendorId());
        }
        analyticLogEvent(TCGoogleAnalyticTrackingEvent.TrackType.ScreenIn,
                this.getClass().getSimpleName(), bundle);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        analyticLogEvent(TCGoogleAnalyticTrackingEvent.TrackType.ScreenOut,
                this.getClass().getSimpleName(), new Bundle());
    }

    protected void analyticTrackScreen() {
        TCGoogleAnalyticTrackingEvent.getInstance().trackScreen(this.getClass().getSimpleName());
    }

    protected void analyticLogEvent(TCGoogleAnalyticTrackingEvent.TrackType trackType, String title, Bundle bundle) {
        TCGoogleAnalyticTrackingEvent.getInstance().logEvent(trackType, title, bundle);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        analyticLogEvent(TCGoogleAnalyticTrackingEvent.TrackType.Press,
                v.getResources().getResourceEntryName(v.getId()), new Bundle());
    }


    public CountryCodeModel getCountryCodeModel() {
        CountryCodeModel countryCodeModel = null;
        if (getActiveActivity() instanceof TCMainActivity) {
            countryCodeModel = ((TCMainActivity) getActiveActivity()).getCountryCodeModel();
        }
        return countryCodeModel;
    }

    public ArrayList<VendorCategoryModel> getListCategorySort() {
        ArrayList<VendorCategoryModel> list = new ArrayList<>();
        if (getActiveActivity() instanceof TCMainActivity) {
            list = ((TCMainActivity) getActiveActivity()).getListCategorySort();
        }
        return list;
    }

    public ArrayList<BitMapSelectModel> getBitMapSelect() {
        ArrayList<BitMapSelectModel> list = new ArrayList<>();
        if (getActiveActivity() instanceof TCMainActivity) {
            list = ((TCMainActivity) getActiveActivity()).getBitMapSelect();
        }
        return list;
    }

    public ArrayList<BitMapUnSelectModel> getBitMapUnSelect() {
        ArrayList<BitMapUnSelectModel> list = new ArrayList<>();
        if (getActiveActivity() instanceof TCMainActivity) {
            list = ((TCMainActivity) getActiveActivity()).getBitMapUnSelect();
        }
        return list;
    }

    @Override
    public void onBaseDestroyView() {

    }

    public LatLng getLatLngCurrent() {
        LatLng latLng = null;
        if (getActiveActivity() instanceof TCMainActivity) {
            //  latLng = ((TCMainActivity) getActiveActivity()).getLatLngCurrent();
            latLng = TCUtils.getGPS(getActiveActivity());

        }
        if (latLng == null) {
            TCLog.e("latLng1 null");
            latLng = TCConstant.LOCATION_DEFAULT;
        }
        return latLng;
    }

    public void getLocationListener() {
        ((TCMainActivity) getActiveActivity()).getLocationListener();
    }

    public LatLng getLocation() {
        return ((TCMainActivity) getActiveActivity()).getLocation();
    }

    public void sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType trackType, String step, String eventLog) {
        String flow = "";
        if (this instanceof SplashScreen) {
            flow = "open_app";
        } else if (this instanceof InputPasswordScreen) {
            flow = "login_by_secretKey";
        } else if (this instanceof LoginWithEmailScreen) {
            flow = "login_by_email";
        } else if (this instanceof VerifyPhoneNumberScreen) {
            flow = "signup_by";
        } else if (this instanceof TCSignUpBaseFragment) {
            TCSignUpBaseFragment signUpBaseFragment = (TCSignUpBaseFragment) this;
            flow = "signup_login_by_" + signUpBaseFragment.signUpType;
        }
        Bundle bundle = new Bundle();
        bundle.putString("issue", String.format(
                TCGoogleAnalyticTrackingEvent.TRACK_TRUST_ISSUE_PARAM, flow, this.getClass().getSimpleName(), step, eventLog));
        analyticLogEvent(trackType, "", bundle);
    }

    /**
     * this method control show only 1 dialog at 1 time
     *
     * @param dialog
     */
    public void showSingleDialog(BaseDialog dialog) {
        if (getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) getActiveActivity()).showSingleDialog(dialog);
        }
    }

    public void onBackResult() {
        Intent intent = new Intent();
        finishWithResult(RESULT_OK, intent);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }
    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showBaseMessage(errorModel.getErrorMessage());
    }

    public void logout() {
        String topUpCountry = TCSharePreferenceManager.getInstance().getString(DataKey.TopUpCountry);
        TCSharePreferenceManager.getInstance().clear();
        //save again top up country
        TCSharePreferenceManager.getInstance().setString(DataKey.TopUpCountry, topUpCountry);
        RealmController.getInstance().deleteData();
        replaceFragment(NewWelcomeScreen.getInstance(), true);
        ((TCMainActivity) getActiveActivity()).stopTransactionService();
        ((TCMainActivity) getActiveActivity()).stopLocationTracking();
        hideFooter();
        TCAppFlyerTrackingEvent.getInstance().reset();
        ((TCMainActivity) getActiveActivity()).updateStatusButtonHowToEarnTec(true);
        new FCMHandler(getActiveActivity()).disableFCM();
        new FCMHandler(getActiveActivity()).enableFCM();
    }
}

