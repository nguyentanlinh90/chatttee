package com.teecoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teecoin.base.TCBaseActivity;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCheckInResultScreen;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserRedeemResultScreen;
import com.teecoin.feature.couponSystem.user.discover.DiscoverScreen;
import com.teecoin.feature.couponSystem.user.getCouponResult.UserGetCouponResultScreen;
import com.teecoin.feature.general.account.AccountScreen;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.bitmap.MyBitmap;
import com.teecoin.feature.general.catalogue.MyCatalogue;
import com.teecoin.feature.general.changeLanguage.LanguageModel;
import com.teecoin.feature.general.firebase.MyFireBase;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.inputReferalCode.InputReferralCodeScreen;
import com.teecoin.feature.general.languagesetting.MyLanguage;
import com.teecoin.feature.general.location.MyLocation;
import com.teecoin.feature.general.locationtracking.LocationTrackingService;
import com.teecoin.feature.general.login.LoginScreen;
import com.teecoin.feature.general.loginwithemail.LoginWithEmailScreen;
import com.teecoin.feature.general.mywallet.MyWalletScreen;
import com.teecoin.feature.general.notification.NotificationScreen;
import com.teecoin.feature.general.notification.TCFireBaseMessagingService;
import com.teecoin.feature.general.notification.TCNotificationProcess;
import com.teecoin.feature.general.popup.DialogMessageAlert;
import com.teecoin.feature.general.popup.MessageBaseScreen;
import com.teecoin.feature.general.profileShop.ProfileShopScreen;
import com.teecoin.feature.general.setPasscode.SetPasscodeDialog;
import com.teecoin.feature.general.setPasscode.TCSetPassCodeListener;
import com.teecoin.feature.general.signupaccount.SignUpAccountEmailScreen;
import com.teecoin.feature.general.signupfacebook.SignUpAccountWithFaceBookScreen;
import com.teecoin.feature.general.splash.SplashScreen;
import com.teecoin.feature.general.universallink.TCUniversalLinkProcess;
import com.teecoin.feature.general.welcome.NewWelcomeScreen;
import com.teecoin.feature.payment.shopInputPayment.ShopInputPaymentScreen;
import com.teecoin.feature.payment.shopPaymentQRCode.ShopPaymentQRCodeScreen;
import com.teecoin.feature.payment.userPaymentSuccess.UserPaymentSuccessScreen;
import com.teecoin.feature.payment.userReviewPayment.UserReviewPaymentScreen;
import com.teecoin.feature.reviewSystem.favourite.FavouriteScreen;
import com.teecoin.feature.reviewSystem.filter.FilterModel;
import com.teecoin.feature.reviewSystem.filter.FilterSheetDialog;
import com.teecoin.feature.reviewSystem.listReviews.ListReviewScreen;
import com.teecoin.feature.reviewSystem.location.LocationScreen;
import com.teecoin.feature.reviewSystem.restaurant.RestaurantScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.feature.walletSystem.coinback.CoinBackScreen;
import com.teecoin.feature.walletSystem.coinbackResult.CoinBackResultScreen;
import com.teecoin.feature.walletSystem.confirmPurchase.ConfirmPurchaseScreen;
import com.teecoin.feature.walletSystem.convertToTec.ConvertToTecScreen;
import com.teecoin.feature.walletSystem.convertToTecSuccess.ConvertToTecSuccessScreen;
import com.teecoin.feature.walletSystem.history.HistoryScreen;
import com.teecoin.feature.walletSystem.importSecretKey.ImportSecretKeyScreen;
import com.teecoin.feature.walletSystem.purchase.PurchaseScreen;
import com.teecoin.feature.walletSystem.recoveryPassword.InputPasswordScreen;
import com.teecoin.feature.walletSystem.scanqrCode.InputWalletScreen;
import com.teecoin.feature.walletSystem.sendmoney.SendMoneyResultScreen;
import com.teecoin.feature.walletSystem.verifyphonenumber.VerifyPhoneNumberScreen;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.feature.walletSystem.withdraw.WithdrawScreen;
import com.teecoin.feature.walletSystem.withdrawSuccess.WithdrawSuccessScreen;
import com.teecoin.javastellarsdk.stellar.TransactionListener;
import com.teecoin.javastellarsdk.stellar.TransactionService;
import com.teecoin.javastellarsdk.stellar.model.StellarPayment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.BitMapSelectModel;
import com.teecoin.model.couponsystem.BitMapUnSelectModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.FilterCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.GuidelineModel;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.general.ReadNotificationRequestModel;
import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralCheckSecretKeyRequest;
import com.teecoin.myapi.apirequest.general.GeneralNotificationReadRequest;
import com.teecoin.myapi.apirequest.general.GeneralStoreSecretKeyRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNotificationRequest;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverSubmitBannerClickRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetTransactionDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarObserver;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.stellar.StellarSubscriber;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.KeyStoreUtility;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.DRAGGING;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.HIDDEN;
import static com.teecoin.model.general.PaymentThresholdModel.DEFAULT_MAXIMUM;
import static com.teecoin.model.general.PaymentThresholdModel.DEFAULT_PAYMENT_THRESHOLD;

public class TCMainActivity extends TCBaseActivity implements APIResponseListener, TCSetPassCodeListener, StellarResponseListener, TransactionListener, SlidingUpPanelLayout.PanelSlideListener {

    // ----header
    @BindView(R.id.activity_main_rl_header)
    View activity_main_rl_header;
    @BindView(R.id.activity_main_iv_bt_back)
    ImageView activity_main_ll_back_toolbar;
    @BindView(R.id.activity_main_tv_middle_title)
    TextView activity_main_tv_title_header;
    @BindView(R.id.activity_main_iv_bt_filter)
    ImageView iv_filter_toolbar;
    @BindView(R.id.activity_main_iv_read_all_notification)
    ImageView read_all_notification;

    @BindView(R.id.activity_main_rl_notification)
    View v_notification;
    @BindView(R.id.activity_main_tv_count_notification)
    TextView tv_count_notification;

    @BindView(R.id.activity_main_tv_done)
    TextView activity_main_tv_done;
    @BindView(R.id.activity_main_tv_right_title)
    TextView activity_main_tv_title_right_header;
    //    ------ bottom next
    @BindView(R.id.activity_main_rl_bottom)
    View activity_main_rl_bottom;
    @BindView(R.id.activity_main_iv_next)
    ImageView activity_main_iv_next;
    @BindView(R.id.activity_main_iv_done)
    ImageView activity_main_iv_done;
    @BindView(R.id.activity_main_iv_back)
    ImageView activity_main_iv_back;
    @BindView(R.id.activity_main_ll_next_bottom)
    LinearLayout activity_main_ll_next_bottom;

    /*  View cancel next bottom view  */
    @BindView(R.id.view_cancel_next_bottom_ll_parent_layout)
    View view_cancel_next_bottom_ll_parent_layout;
    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView view_cancel_next_bottom_tv_cancel;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView view_cancel_next_bottom_tv_next;

    //---- tab menu
    @BindView(R.id.activity_main_ll_tab_menu)
    LinearLayout ll_tab_menu;

    @BindView(R.id.activity_main_ll_tab_payment)
    View ll_tab_payment;
    @BindView(R.id.activity_main_iv_tab_payment)
    ImageView iv_tab_payment;
    @BindView(R.id.activity_main_tv_tab_payment)
    TextView tv_tab_payment;

    @BindView(R.id.activity_main_ll_tab_account)
    LinearLayout ll_tab_account;
    @BindView(R.id.activity_main_iv_tab_account)
    ImageView iv_tab_account;
    @BindView(R.id.activity_main_tv_tab_account)
    TextView tv_tab_account;

    @BindView(R.id.activity_main_ll_tab_notification)
    View ll_tab_notification;
    @BindView(R.id.activity_main_iv_tab_notification)
    ImageView iv_tab_notification;
    @BindView(R.id.activity_main_tv_tab_count_notification)
    TextView tv_tab_count_notification;
    @BindView(R.id.activity_main_tv_tab_notifications)
    TextView tv_tab_notifications;

    @BindView(R.id.activity_main_ll_tab_favourite)
    LinearLayout ll_tab_favourite;
    @BindView(R.id.activity_main_iv_tab_favourite)
    ImageView iv_tab_favourite;
    @BindView(R.id.activity_main_tv_tab_favourite)
    TextView tv_tab_favourite;

    @BindView(R.id.activity_main_ll_tab_discover)
    LinearLayout ll_tab_discover;
    @BindView(R.id.activity_main_iv_tab_discover)
    ImageView iv_tab_discover;
    @BindView(R.id.activity_main_tv_tab_discover)
    TextView tv_tab_discover;

    @BindView(R.id.activity_main_ll_tab_profile)
    LinearLayout ll_tab_profile;
    @BindView(R.id.activity_main_iv_tab_profile)
    ImageView iv_tab_profile;
    @BindView(R.id.activity_main_tv_tab_profile)
    TextView tv_tab_profile;

    @BindView(R.id.activity_main_ll_tab_coupon_user)
    LinearLayout ll_tab_coupon_user;
    @BindView(R.id.activity_main_iv_tab_coupon_user)
    ImageView iv_tab_coupon_user;
    @BindView(R.id.activity_main_tv_tab_coupon_user)
    TextView tv_tab_coupon_user;

    @BindView(R.id.activity_main_rl_qr_code)
    View rlQrCode;
    @BindView(R.id.activity_main_iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.activity_main_iv_add_coupon)
    ImageView ivAddCoupon;

    @BindView(R.id.activity_main_spl_main_slide_panel)
    SlidingUpPanelLayout spl_main_slide_panel;
    @BindView(R.id.main_activity_rl_slide_up_view)
    ViewGroup rl_slide_up_view;
    BroadcastReceiver transactionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            StellarPayment payment = (StellarPayment) intent.getSerializableExtra(TransactionService.TRANSACTION_STELLAR_PAYMENT_INTENT);
            if (payment != null && isAlreadyLogin()) {
                requestApi(new WalletGetTransactionDetailRequest(payment.getTransactionHash(), TCMainActivity.this));
            }
        }
    };

    private boolean clockScreen = true;
    BroadcastReceiver unClockScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_USER_PRESENT.equals(action)) {
                checkLockScreen();
            }
        }
    };
    private MyFireBase myFireBase;
    private MyLanguage myLanguage;
    private MyLocation myLocation;
    private MyBitmap myBitmap;
    private MyCatalogue myCatalogue;
    private Timer mTimer;
    private boolean isOpening;

    private double rate = 1;
    private double fee = 0;
    private double spread = 0;
    private double basic_fee_amount = 0;

    public static void showAllThread() {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.RUNNABLE) {
                if (t.getName().contains("jersey-sse-event-source")) {
                    TCLog.d("hung showAllThread Thread name=" + t.getName() + " isAlive=" + t.isAlive() + " isInterrupt=" + t.isInterrupted());
                }
            }
        }
    }

    private static Intent getServiceIntent(Context c, String publicKey) {
        Intent intent = new Intent(c, TransactionService.class);
        intent.putExtra(TransactionService.TRANSACTION_STELLAR_PUBLIC_KEY_INTENT, publicKey);
        intent.putExtra(TransactionService.TRANSACTION_LAST_TOKEN_INTENT,
                TCSharePreferenceManager.getInstance().getString(DataKey.PagingToken));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLanguage = new MyLanguage(this);
        setContentView(R.layout.activity_main);
        if (null == myFireBase)
            myFireBase = new MyFireBase(this);
        myLocation = new MyLocation(this);
        if (isAlreadyLogin())
            getLocationListener();

        myBitmap = new MyBitmap();
        myCatalogue = new MyCatalogue();

        TCAppFlyerTrackingEvent.getInstance().initAppFlyer();
        LocalBroadcastManager.getInstance(this).registerReceiver(transactionReceiver,
                new IntentFilter(TransactionService.TRANSACTION_INTENT));

        updateStatusButtonHowToEarnTec(true);

        replaceFragment(SplashScreen.getInstance(), true);
    }

    @Override
    public void onBindView() {
        initializeSlideUpPanel();
        initClickEvent();
        initView();
    }

    //location
    public void setReloadDiscoverScreen() {
        myLocation.setReloadDiscoverScreen();
    }

    public void getLocationListener() {
        myLocation.getLocationListener();
    }

    public LatLng getLocation() {
        return myLocation.getLocation();
    }

    public void getCountryCode() {
        myLocation.getCountryCode();
    }

    public void setCountryCodeModel() {
        myLocation.setCountryCodeModel();
    }

    public CountryCodeModel getCountryCodeModel() {
        return myLocation.getCountryCodeModel();
    }

    public void setCountryCodeModel(CountryCodeModel countryCodeModel) {
        myLocation.setCountryCodeModel(countryCodeModel);
    }

    public ArrayList<CountryCodeModel> getListCountry() {
        return myLocation.getListCountry();
    }

    public void setListCountry(ArrayList<CountryCodeModel> listCountry) {
        myLocation.setListCountry(listCountry);
    }

    //bitmap
    public ArrayList<BitMapSelectModel> getBitMapSelect() {
        return myBitmap.getBitMapSelect();
    }

    public void setBitMapSelect(ArrayList<BitMapSelectModel> bitMapSelect) {
        myBitmap.setBitMapSelect(bitMapSelect);
    }

    public ArrayList<BitMapUnSelectModel> getBitMapUnSelect() {
        return myBitmap.getBitMapUnSelect();
    }

    public void setBitMapUnSelect(ArrayList<BitMapUnSelectModel> bitMapUnSelect) {
        myBitmap.setBitMapUnSelect(bitMapUnSelect);
    }

    //catalogue
    public ArrayList<FilterCatalogueModel> getListFilterCatalogue() {
        return myCatalogue.getListFilterCatalogue();
    }

    public ArrayList<VendorCategoryModel> getListCategorySort() {
        return myCatalogue.getListCategorySort();
    }

    public void setListCategoryToSort(ArrayList<VendorCategoryModel> listCategorySort) {
        myCatalogue.setListCategoryToSort(listCategorySort);
    }

    public VendorCategoryModel getVendorCategorySelected() {
        return myCatalogue.getVendorCategorySelected();
    }

    public void getListFilterCategory() {
        myCatalogue.getListFilterCategory();
    }

    public void updateListFilter(FilterCatalogueModel filterCatalogueModel) {
        myCatalogue.updateListFilter(filterCatalogueModel);
    }

    private void initView() {
        /* Init bottom menu tab icon 1.Coupon, 2.review, 3.wallet,4.Account (remove history tab */
        /* new ui tab menu: 1.discover , 2.coupon, 3.favourite, 4.notify, 5.account */
        /*ll_tab_menu.removeAllViews();
        ll_tab_menu.addView(ll_tab_discover);
        ll_tab_menu.addView(ll_tab_coupon_user);
        ll_tab_menu.addView(ll_tab_favourite);
        ll_tab_menu.addView(ll_tab_notification);
        ll_tab_menu.addView(ll_tab_account);
        *//* Init bottom menu tab icon 1.Coupon, 2.review, 3.wallet,4.Account (remove history tab *//*

        ll_tab_coupon_user.setVisibility(!BuildConfig.IS_CHATEE_APP ? View.GONE :
                BuildConfig.IS_APP_USER ? View.VISIBLE : View.GONE);
        ll_tab_discover.setVisibility(!BuildConfig.IS_CHATEE_APP ? View.GONE :
                BuildConfig.IS_APP_USER ? View.VISIBLE : View.GONE);*/

        ll_tab_discover.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
        ll_tab_profile.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        ll_tab_coupon_user.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
        ll_tab_favourite.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
        // ll_tab_notification.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
        ll_tab_payment.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        ll_tab_profile.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);

        if (isAppUser() && isAlreadyLogin())
            checkNewNotification();
    }

    private void initializeSlideUpPanel() {
        spl_main_slide_panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        spl_main_slide_panel.setPanelHeight(0);
        spl_main_slide_panel.addPanelSlideListener(this);
    }

    public void hideHeader() {
        activity_main_rl_header.setVisibility(View.GONE);
    }

    public void hideFooter() {
        activity_main_rl_bottom.setVisibility(View.GONE);
    }

    public void showHeader() {
        activity_main_rl_header.setVisibility(View.VISIBLE);
    }

    public void showFooter() {
        activity_main_rl_bottom.setVisibility(View.VISIBLE);
    }

    public void updateTitleHeader(String title) {
        activity_main_tv_title_header.setText(title);
        activity_main_tv_title_header.setAllCaps(true);
    }

    public void updateTitleHeaderLowerCase(String title) {
        activity_main_tv_title_header.setText(title);
        activity_main_tv_title_header.setAllCaps(false);
    }

    public void showReadAllNotification(boolean show) {
        read_all_notification.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showViewNotification(boolean show) {
        v_notification.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setNumberNotification(String number) {
        tv_count_notification.setVisibility(View.VISIBLE);
        tv_count_notification.setText(number);
    }

    public void updateTitleRightHeader(String title) {
        activity_main_tv_title_right_header.setText(title);
    }

    public void setOnClickListenerForTitleRightHeader(View.OnClickListener onClickListener) {
        activity_main_tv_title_right_header.setOnClickListener(onClickListener);
    }

    public void showButtonBackToolbar() {
        activity_main_ll_back_toolbar.setVisibility(View.VISIBLE);
    }

    public void showMenuNextBottom() {
        activity_main_ll_next_bottom.setVisibility(View.VISIBLE);
        ll_tab_menu.setVisibility(View.GONE);
    }

    public void hideMenuNextBottom() {
        activity_main_ll_next_bottom.setVisibility(View.GONE);
    }

    public void showTabMenuBottom() {
        ll_tab_menu.setVisibility(View.VISIBLE);
        activity_main_ll_next_bottom.setVisibility(View.GONE);
        view_cancel_next_bottom_ll_parent_layout.setVisibility(View.GONE);
    }

    public void hideButtonBackToolbar() {
        activity_main_ll_back_toolbar.setVisibility(View.GONE);
    }

    public void hideButtonDone() {
        activity_main_iv_done.setVisibility(View.GONE);
    }

    public void showButtonDone() {
        activity_main_iv_done.setVisibility(View.VISIBLE);
        activity_main_iv_done.setEnabled(true);
        activity_main_iv_back.setEnabled(true);
    }

    public void hideButtonNext() {
        activity_main_iv_next.setVisibility(View.GONE);
    }

    public void showButtonNext() {
        activity_main_iv_next.setVisibility(View.VISIBLE);
        activity_main_iv_next.setEnabled(true);
        activity_main_iv_back.setEnabled(true);
    }

    public void showFilterToolbar() {
        iv_filter_toolbar.setVisibility(View.VISIBLE);
    }

    public void hideFilterToolbar() {
        iv_filter_toolbar.setVisibility(View.GONE);
    }

    public void showTextViewDone() {
        activity_main_tv_done.setVisibility(View.VISIBLE);
    }

    public void hideTextViewDone() {
        activity_main_tv_done.setVisibility(View.GONE);
    }

    public void showViewQrCode() {
        rlQrCode.setVisibility(View.VISIBLE);
    }

    public void hideViewQrCode() {
        rlQrCode.setVisibility(View.GONE);
    }

    public void showCancelNextBottomView() {
        view_cancel_next_bottom_ll_parent_layout.setVisibility(View.VISIBLE);
        ll_tab_menu.setVisibility(View.GONE);
    }

    public void hideCancelNextBottomView() {
        view_cancel_next_bottom_ll_parent_layout.setVisibility(View.GONE);
    }

    public void setTextForNextBottomView(String text) {
        view_cancel_next_bottom_tv_next.setText(text);
    }

    public void setTextForCancelBottomView(String text) {
        view_cancel_next_bottom_tv_cancel.setText(text);
    }

    public void loadQrCodeImage(String url) {
        Glide.with(getActiveActivity()).load(TCUtils.isEmpty(url) ?
                TCUtils.getDrawable(R.drawable.ic_logo_chattee) : url).into(ivQrCode);
    }

    public void showButtonAddCoupon() {
        ivAddCoupon.setVisibility(View.VISIBLE);
    }

    public void hideButtonAddCoupon() {
        ivAddCoupon.setVisibility(View.GONE);
    }

    public void showBaseMessage(String message) {
        MessageBaseScreen notificationBaseScreen = new MessageBaseScreen(getActiveActivity(), message);
        notificationBaseScreen.show();
    }

    public void showDialogMessageAlert(boolean isSuccess, String message) {
        DialogMessageAlert dialogMessageAlert = new DialogMessageAlert(getActiveActivity(), isSuccess, message);
        dialogMessageAlert.show();
    }

    private void initClickEvent() {
        registerSingleClick(activity_main_ll_back_toolbar,
                activity_main_iv_back, activity_main_iv_next,
                activity_main_iv_done,
                ll_tab_discover, ll_tab_favourite, ll_tab_payment, ll_tab_profile, ll_tab_account, ll_tab_coupon_user,
                ll_tab_notification, iv_filter_toolbar,
                rlQrCode, read_all_notification, ivAddCoupon,
                view_cancel_next_bottom_tv_cancel, view_cancel_next_bottom_tv_next);
        spl_main_slide_panel.setFadeOnClickListener(v ->
        {
            if (isFilterPanelOpened())
                closeFilterPanel();
        });
//        view_cancel_next_bottom_tv_next.setOnTouchListener((v, event) -> {
//            gotoNextScreen();
//            return false;
//        });
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_cancel_next_bottom_tv_cancel:
                checkClickCancelButton();
                break;
            case R.id.view_cancel_next_bottom_tv_next:
                gotoNextScreen();
                break;
            case R.id.activity_main_iv_bt_back:
                // handleBackPressed();
                checkBackFromScreen();
                break;
            case R.id.activity_main_iv_back:
                handleBackPressed();
                break;
            case R.id.activity_main_iv_next:
                gotoNextScreen();
                break;
            case R.id.activity_main_iv_done:
                gotoNextScreen();
                break;
            case R.id.activity_main_ll_tab_discover:
                gotoDiscoverScreen();
                break;
            case R.id.activity_main_ll_tab_profile:
                gotoProfileScreen();
                break;
            case R.id.activity_main_ll_tab_favourite:
                gotoMyFavouriteScreen();
                break;
            case R.id.activity_main_ll_tab_payment:
                gotoPaymentScreen();
                break;
            case R.id.activity_main_ll_tab_account:
                gotoAccountScreen();
                break;
            case R.id.activity_main_ll_tab_coupon_user:
                openCouponUserScreen();
                break;
            case R.id.activity_main_ll_tab_notification:
                gotoNotificationScreen();
                break;
            case R.id.activity_main_iv_bt_filter:
                openFilterPanel();
                break;
            case R.id.activity_main_iv_read_all_notification:
                readAllNotification();
                break;
            default:
                break;
        }
    }

    private void checkClickCancelButton() {
        if (getTopFragment() != null) {
            if (getTopFragment() instanceof InputReferralCodeScreen) {
                InputReferralCodeScreen inputReferalCodeScreen = (InputReferralCodeScreen) getTopFragment();
                inputReferalCodeScreen.handleSignUp("");
            } else if (getTopFragment() instanceof SignUpAccountEmailScreen || getTopFragment() instanceof SignUpAccountWithFaceBookScreen) {
                replaceFragment(NewWelcomeScreen.getInstance(), true);
            } else if (getTopFragment() instanceof ImportSecretKeyScreen
                    || getTopFragment() instanceof InputPasswordScreen) {
                replaceFragment(LoginScreen.getInstance(), true);
            } else {
                handleBackPressed();
            }
        }
    }

    private void gotoPaymentScreen() {
        if (!(getTopFragment() instanceof ShopInputPaymentScreen)) {
            replaceFragment(ShopInputPaymentScreen.getInstance(), true);
            unSelectTab();
            iv_tab_payment.setSelected(true);
            tv_tab_payment.setSelected(true);
        }
    }

    private void gotoAccountScreen() {
        unSelectTab();
        iv_tab_account.setSelected(true);
        tv_tab_account.setSelected(true);
        if (!(getTopFragment() instanceof AccountScreen)) {
            replaceFragment(AccountScreen.getInstance(), true);
            if (isAppUser()) {
                checkNewNotification();
            }
        }
    }

    public void gotoDiscoverScreen() {
        if (!(getTopFragment() instanceof DiscoverScreen)) {
            replaceFragment(DiscoverScreen.getInstance(), true);
            unSelectTab();
            iv_tab_discover.setSelected(true);
            tv_tab_discover.setSelected(true);
            checkNewNotification();
        }
    }

    public void gotoProfileScreen() {
        if (!(getTopFragment() instanceof ProfileShopScreen)) {
            // replaceFragment(ProfileScreen.getInstance(), true);
            replaceFragment(ProfileShopScreen.getInstance(), true);
            unSelectTab();
            iv_tab_profile.setSelected(true);
            tv_tab_profile.setSelected(true);
        }
    }

    private void gotoMyFavouriteScreen() {
        if (!(getTopFragment() instanceof FavouriteScreen)) {
            replaceFragment(FavouriteScreen.getInstance(), true);
            unSelectTab();
            iv_tab_favourite.setSelected(true);
            tv_tab_favourite.setSelected(true);
            checkNewNotification();
        }
    }

    public void gotoNotificationScreen() {
        if (!(getTopFragment() instanceof NotificationScreen)) {
            replaceFragment(NotificationScreen.getInstance(), true);
            unSelectTab();
            iv_tab_notification.setSelected(true);
            tv_tab_notifications.setSelected(true);
            //tv_tab_count_notification.setVisibility(View.GONE);
        }
    }

    private void gotoNextScreen() {
        Fragment fragment = getTopFragment();
        if (fragment != null) {
            if (fragment instanceof InputPasswordScreen) {
                InputPasswordScreen inputPasswordScreen = (InputPasswordScreen) fragment;
                if (inputPasswordScreen.validate()) {
                    inputPasswordScreen.getAccountInfoAndGoToNextScreen();
                }
            } else if (fragment instanceof CoinBackScreen) {
                CoinBackScreen coinBackScreen = (CoinBackScreen) fragment;
                coinBackScreen.gotoCoinBackResult();
            } else if (fragment instanceof ImportSecretKeyScreen) {

                ImportSecretKeyScreen importPublicKeyScreen = (ImportSecretKeyScreen) fragment;
                importPublicKeyScreen.checkSecretKey();

            } else if (fragment instanceof UserReviewShopScreen) {
                UserReviewShopScreen userReviewShopScreen = (UserReviewShopScreen) fragment;
                userReviewShopScreen.validate();
            } else if (fragment instanceof PurchaseScreen) {
                PurchaseScreen purchaseScreen = (PurchaseScreen) fragment;
                purchaseScreen.validate();
            } else if (fragment instanceof ConfirmPurchaseScreen) {
                ConfirmPurchaseScreen confirmPurchaseScreen = (ConfirmPurchaseScreen) fragment;
                confirmPurchaseScreen.validate();
            } else if (fragment instanceof InputWalletScreen) {
                InputWalletScreen inPutWalletScreen = (InputWalletScreen) fragment;
                inPutWalletScreen.gotoTransferScreen();
            } else if (fragment instanceof VerifyPhoneNumberScreen) {
                VerifyPhoneNumberScreen verifyPhoneNumberScreen = (VerifyPhoneNumberScreen) fragment;
                verifyPhoneNumberScreen.validate();
            } else if (fragment instanceof SignUpAccountEmailScreen) {
                SignUpAccountEmailScreen signUpAccountScreen = (SignUpAccountEmailScreen) fragment;
                if (signUpAccountScreen.validate())
                    signUpAccountScreen.startSignUp();
            } else if (fragment instanceof LoginWithEmailScreen) {
                LoginWithEmailScreen loginScreen = (LoginWithEmailScreen) fragment;
                loginScreen.validateLogin();
            } else if (fragment instanceof SignUpAccountWithFaceBookScreen) {
                SignUpAccountWithFaceBookScreen signUpAccountScreen = (SignUpAccountWithFaceBookScreen) fragment;
                if (signUpAccountScreen.validate()) {
                    signUpAccountScreen.validateExistEmail();
                }
            } else if (fragment instanceof InputReferralCodeScreen) {
                InputReferralCodeScreen inputReferalCodeScreen = (InputReferralCodeScreen) fragment;
                inputReferalCodeScreen.checkReferralCode();
            }
        }
    }

    public void openWalletScreen() {
        if (!(getTopFragment() instanceof WalletAccountScreen)) {
            unSelectTab();
            iv_tab_account.setSelected(true);
            tv_tab_account.setSelected(true);
            replaceFragment(WalletAccountScreen.getInstance(), true);
            checkNewNotification();
        }

    }

    public void openHomeScreen() {
        unSelectTab();
        if (isAppUser()) {
            iv_tab_discover.setSelected(true);
            tv_tab_discover.setSelected(true);
            replaceFragment(DiscoverScreen.getInstance(), true);
            checkNewNotification();
            checkAndStoreSecretKey();
        } else {
            iv_tab_payment.setSelected(true);
            iv_tab_payment.setSelected(true);
            replaceFragment(ShopInputPaymentScreen.getInstance(), true);
        }
    }

    public void openHomeScreen(String vendorId, boolean isGotoVendorCouponList) {
        unSelectTab();
        iv_tab_discover.setSelected(true);
        tv_tab_discover.setSelected(true);
        //replaceFragment(UserCouponScreen.getInstance(vendorId, isGotoVendorCouponList), true);
        replaceFragment(DiscoverScreen.getInstance(vendorId, isGotoVendorCouponList), true);
    }

    public void selectDiscover() {
        unSelectTab();
        iv_tab_discover.setSelected(true);
        tv_tab_discover.setSelected(true);
    }

    public void selectCoupon() {
        unSelectTab();
        iv_tab_coupon_user.setSelected(true);
        iv_tab_coupon_user.setSelected(true);
    }

    public void openHistoryScreen() {
        if (!(getTopFragment() instanceof HistoryScreen)) {
            unSelectTab();
            replaceFragment(HistoryScreen.getInstance(), true);
        }
    }

    public void unSelectTab() {
        iv_tab_account.setSelected(false);
        tv_tab_account.setSelected(false);

        iv_tab_coupon_user.setSelected(false);
        tv_tab_coupon_user.setSelected(false);

        iv_tab_notification.setSelected(false);
        tv_tab_notifications.setSelected(false);

        iv_tab_discover.setSelected(false);
        tv_tab_discover.setSelected(false);

        iv_tab_favourite.setSelected(false);
        tv_tab_favourite.setSelected(false);

        iv_tab_payment.setSelected(false);
        tv_tab_payment.setSelected(false);

        iv_tab_profile.setSelected(false);
        tv_tab_profile.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        registerReceiver(unClockScreenReceiver, filter);
        if (myFireBase != null) {
            myFireBase.checkVersionAppUpdate();
        }
        setOpening(true);
    }

    public void processNotification() {
        if (isAlreadyLogin()) {
            Intent intent = getIntent();
            if (intent != null) {
                PushNotificationModel pushNotificationIntent = (PushNotificationModel) intent.getSerializableExtra(TCFireBaseMessagingService.PUSH_NOTIFICATION_MODEL_INTENT);
                if (pushNotificationIntent != null) {
                    new TCNotificationProcess(this, pushNotificationIntent);
                } else {
                    PushNotificationModel pushNotificationBundle = new PushNotificationModel(intent.getExtras());
                    if (pushNotificationBundle.getData() != null)
                        new TCNotificationProcess(this, pushNotificationBundle);
                }
            }
        }

    }

    public void setClockScreen(boolean clock) {
        clockScreen = clock;
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_TRANSACTION_DETAIL) {
            TransactionDetailModel transactionDetailModel = (TransactionDetailModel) response.getResult();
            if (!TCUtils.isEmpty(transactionDetailModel.getPaging_token())) {
                TCSharePreferenceManager.getInstance().setString(DataKey.PagingToken, transactionDetailModel.getPaging_token());
            }
            RealmController.getInstance().insertData(transactionDetailModel, TransactionDetailModel.PRIMARY_KEY, transactionDetailModel.getTransaction_hash());
            AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
            if (accountModel != null && !TCUtils.isEmpty(accountModel.getPublic_key())) {
                stellarGetBalance(accountModel.getPublic_key(), false, this);
            }
            if (getTopFragment() instanceof WalletAccountScreen) {
                WalletAccountScreen accountScreen = (WalletAccountScreen) getTopFragment();
                accountScreen.getTransactionData();
            } else if (getTopFragment() instanceof ShopPaymentQRCodeScreen) {
                ShopPaymentQRCodeScreen shopScreen = (ShopPaymentQRCodeScreen) getTopFragment();
                if (transactionDetailModel.getType().equals(EnumMgr.TransactionType.Payment.getValue())
                        && transactionDetailModel.getInvoice().equals(shopScreen.getInvoidID())) {
                    shopScreen.userFinishPayment(transactionDetailModel);
                }
            }
        } else if (requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT) {

        } else if (requestTarget == GeneralRequestTarget.USER_READ_NOTIFY || requestTarget == GeneralRequestTarget.SHOP_READ_NOTIFY) {
            if (getTopFragment() instanceof NotificationScreen) {
                NotificationScreen notificationScreen = (NotificationScreen) getTopFragment();
                notificationScreen.updateMarkAll();
                checkNewNotification();
            }
        } else if (requestTarget == ReviewRequestTarget.USER_GET_NOTIFY || requestTarget == ReviewRequestTarget.SHOP_GET_NOTIFY) {
            String unread = ((BaseResultsResponseModel) response.getResult()).getUnread();
            tv_tab_count_notification.setVisibility(!TCUtils.isEmpty(unread)
                    && !unread.equals("0") ? View.VISIBLE : View.GONE);
            tv_tab_count_notification.setText(unread);

            if (tv_tab_count_notification.getVisibility() == View.VISIBLE && tv_tab_count_notification.getText().toString().length() > 1) {
                tv_tab_count_notification.setTextSize(TypedValue.COMPLEX_UNIT_PX, TCUtils.getDimension(R.dimen.ts_7));
            }
        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
    }

    public void insertInitData(AccountModel accountModel, MyFireBase.GetAppConfigListener appConfigListener) {
        if (accountModel != null) {
            try {
                showLoadingDialog();
                accountModel.setConfirm_password("");
                if (!TCUtils.isEmpty(accountModel.getPassword())) {
                    accountModel.setPassword(KeyStoreUtility.getInstance().encryptString(accountModel.getPassword()));
                }

                RealmController.getInstance().insertData(accountModel);


                PaymentThresholdModel paymentThresholdModel = new PaymentThresholdModel(accountModel.getPublic_key(), DEFAULT_PAYMENT_THRESHOLD, DEFAULT_MAXIMUM);
                RealmController.getInstance().insertData(paymentThresholdModel);

                if (myFireBase != null)
                    myFireBase.getAppConfig(accountModel, appConfigListener);
                if (myLanguage != null) {
                    myLanguage.updateLanguage(accountModel);
                    refreshBottomLayoutWhenChangeLanguage();
                }
                updateGoogleAnalyticUserProperty(accountModel);
                hideLoadingDialog();

            } catch (Exception e) {
                hideLoadingDialog();
            }
        }
    }

    public void openCouponUserScreen() {
        if (!(getTopFragment() instanceof UserCouponScreen)) {
            unSelectTab();
            iv_tab_coupon_user.setSelected(true);
            tv_tab_coupon_user.setSelected(true);
            replaceFragment(UserCouponScreen.getInstance(), true);
            checkNewNotification();
        }
    }

    public void openCouponUserScreen(String couponId) {
        //because default screen is Coupon, show replace it
        unSelectTab();
        iv_tab_coupon_user.setSelected(true);
        tv_tab_coupon_user.setSelected(true);
        replaceFragment(UserCouponScreen.getInstance(couponId), true);
        checkNewNotification();
    }

    public void openWalletScreen(boolean SGDWallet, boolean TecWallet) {
        replaceFragment(AccountScreen.getInstance(SGDWallet, TecWallet), true);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(transactionReceiver);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        updateStatusButtonHowToEarnTec(true);
        setOpening(false);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    private void checkBackFromScreen() {
        Fragment fragment = getTopFragment();
        if (fragment != null) {
            if (fragment instanceof ShopPaymentQRCodeScreen) {
                ShopPaymentQRCodeScreen shopPaymentQRCodeScreen = (ShopPaymentQRCodeScreen) fragment;
                shopPaymentQRCodeScreen.onBack();
            } else {
                handleBackPressed();
            }
        }
    }

    public void handleBackPressed() {
        if (isFilterPanelOpened()) {
            closeFilterPanel();
            return;
        }

        Fragment fragment = getTopFragment();
        if (fragment != null) {
            if (fragment instanceof CoinBackResultScreen
                    || fragment instanceof UserCheckInResultScreen
                    || fragment instanceof UserRedeemResultScreen
                    || fragment instanceof SendMoneyResultScreen
                    || fragment instanceof UserPaymentSuccessScreen
                    || fragment instanceof ConvertToTecSuccessScreen
                    || fragment instanceof WithdrawSuccessScreen
                    || fragment instanceof UserGetCouponResultScreen
            ) {
                //do nothing, disable backPress
            } else if (fragment instanceof LocationScreen) {
                ((LocationScreen) fragment).putLocationResult();
            } else if (fragment instanceof InputWalletScreen) {
//                openWalletScreen();
                replaceFragment(WalletAccountScreen.getInstance(TCConstant.INPUT_WALLET), true);
            } else if (fragment instanceof UserReviewPaymentScreen) {
                replaceFragment(WalletAccountScreen.getInstance(TCConstant.USER_PAYMENT_REVIEW), true);
            } else if (fragment instanceof WalletAccountScreen && getBackStackCount() <= 1) {
                replaceFragment(AccountScreen.getInstance(), true);
            } else if (fragment instanceof MyWalletScreen && getBackStackCount() <= 1) {
                replaceFragment(AccountScreen.getInstance(), true);
            } else if (fragment instanceof LoginScreen) {
                replaceFragment(NewWelcomeScreen.getInstance(), true);
            } else if (fragment instanceof VendorScreen) {
                if (getBackStackCount() > 1) {
                    backStack();
                } else {
                    replaceFragment(DiscoverScreen.getInstance(), true);
                }

            } else if (fragment instanceof ConvertToTecScreen) {
                if (((ConvertToTecScreen) fragment).isConvertScreen()) {
                    backStack();
                }
            } else if (fragment instanceof WithdrawScreen) {
                if (((WithdrawScreen) fragment).isWithdrawScreen()) {
                    backStack();
                }
            } else if (getBackStackCount() > 0) {
                backStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void checkLockScreen() {
        Fragment fragment = getTopFragment();
        if (fragment != null) {
            if (fragment instanceof SplashScreen) {
                return;
            }
        }
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null && accountModel.isLogin() && !TextUtils.isEmpty(TCSharePreferenceManager.getInstance().getString(DataKey.Passcode))) {
            if (clockScreen) {
                SetPasscodeDialog setPasscodeDialog = new SetPasscodeDialog(getActiveActivity(), true, false, false, this);
                setPasscodeDialog.show();
            }
        }
        clockScreen = true;
    }

    @Override
    public void onCloseApp(boolean isClose) {
        if (isClose)
            finish();
    }

    @Override
    public void onUnClockSuccess(boolean succeess) {
    }

    @Override
    public void onFinishChangePassCode() {

    }

    @Override
    public void onRemovePassCode() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(unClockScreenReceiver);
        myLocation.removeUpdatesLocationManager();
        setOpening(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processNotification();
        handleDeepLinkIntent(intent);
    }

    public void stellarGetBalance(String publicKey, boolean isLoading, StellarResponseListener listener) {
        if (isLoading)
            showLoadingDialog();
        try {
            StellarObserver.getInstance().getBalance(publicKey)
                    .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new StellarSubscriber<>(listener));
        } catch (Exception e) {
            showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public void onStellarSuccess(Object object) {
        if (object instanceof String) {
            String teeCoinAmount = (String) object;
            AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
            accountModel.setBalance(TCUtils.isEmpty(teeCoinAmount) ? "0" : teeCoinAmount);
            RealmController.getInstance().updateAccountModel(accountModel);
            updateGoogleAnalyticUserProperty(accountModel);
            if (getTopFragment() instanceof WalletAccountScreen) {
                WalletAccountScreen accountScreen = (WalletAccountScreen) getTopFragment();
                accountScreen.getBalance();
            } else if (getTopFragment() instanceof DiscoverScreen) {
                DiscoverScreen discoverScreen = (DiscoverScreen) getTopFragment();
                discoverScreen.setBalance(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, accountModel.getBalance()));
            }
        }
    }

    @Override
    public void onStellarFail(Throwable o) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onReceiveTransaction(StellarPayment stellarPayment) {
        if (stellarPayment != null && !TCUtils.isEmpty(stellarPayment.getTransactionHash())
                && isAlreadyLogin()) {
            requestApi(new WalletGetTransactionDetailRequest(stellarPayment.getTransactionHash(), this));
        } else {
            TCSharePreferenceManager.getInstance().setString(DataKey.PagingToken, stellarPayment.getLastToken());
        }
    }

    public void startTransactionService(String publicKey) {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    showAllThread();
                }
            }, 0, TransactionService.TIMER_REPEAT_CHECK);
        }
        if (!TCUtils.isEmpty(publicKey) && !TransactionService.isExistThread(publicKey)) {
            TCLog.d("hung start stream service");
            startService(getServiceIntent(getActiveActivity(), publicKey));
        }
    }

    public void stopTransactionService() {
        TransactionService.stopThread();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        stopService(getServiceIntent(getActiveActivity(), ""));
        System.gc();
    }

    public void openFilterPanel() {
        if (spl_main_slide_panel == null)
            return;

        if (spl_main_slide_panel.getPanelState() == ANCHORED || spl_main_slide_panel.getPanelState() == DRAGGING)
            return;

        FilterModel filterModel = null;
        if (getTopFragment() instanceof ListReviewScreen) {
            ListReviewScreen listReviewScreen = (ListReviewScreen) getTopFragment();
            filterModel = listReviewScreen.getFilter();
        } else if (getTopFragment() instanceof RestaurantScreen) {
            RestaurantScreen restaurantScreen = (RestaurantScreen) getTopFragment();
            filterModel = restaurantScreen.getFilter();

        }
        FilterModel finalFilterModel = filterModel;
        runOnUiThread(() -> {
            FilterSheetDialog filterSheetDialog = new FilterSheetDialog(getActiveActivity(),
                    getTopFragment() instanceof ListReviewScreen ? FilterSheetDialog.FilterTypeDialog.TopReviewFilter : FilterSheetDialog.FilterTypeDialog.RestaurantFilter,
                    finalFilterModel, filterList -> {
                if (getTopFragment() instanceof ListReviewScreen) {
                    ListReviewScreen listReviewScreen = (ListReviewScreen) getTopFragment();
                    listReviewScreen.filter(filterList);
                } else if (getTopFragment() instanceof RestaurantScreen) {
                    RestaurantScreen restaurantScreen = (RestaurantScreen) getTopFragment();
                    restaurantScreen.filter(filterList);
                }
            });
            filterSheetDialog.show(getSupportFragmentManager(), "FilterSheetDialog");
        });
    }

    public void closeFilterPanel() {
        if (spl_main_slide_panel == null)
            return;

        if (spl_main_slide_panel.getPanelState() == HIDDEN || spl_main_slide_panel.getPanelState() == COLLAPSED || spl_main_slide_panel.getPanelState() == DRAGGING)
            return;

        spl_main_slide_panel.setPanelState(HIDDEN);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if ((previousState == ANCHORED || previousState == EXPANDED) && newState == HIDDEN)
            closeFilterPanel();
    }

    public boolean isFilterPanelOpened() {
        return spl_main_slide_panel.getPanelState() == EXPANDED || spl_main_slide_panel.getPanelState() == ANCHORED;
    }

    private void readAllNotification() {
        requestApi(new GeneralNotificationReadRequest(new ReadNotificationRequestModel(true),
                isAppUser() ? GeneralRequestTarget.USER_READ_NOTIFY : GeneralRequestTarget.SHOP_READ_NOTIFY, this));
    }

    public void refreshBottomLayoutWhenChangeLanguage() {
        tv_tab_discover.setText(TCUtils.getString(R.string.discover));
        tv_tab_coupon_user.setText(TCUtils.getString(R.string.text_coupons));
        tv_tab_favourite.setText(TCUtils.getString(R.string.favourite));
        tv_tab_notifications.setText(TCUtils.getString(R.string.text_notifications));
        tv_tab_account.setText(TCUtils.getString(R.string.main_activity_tab_account));
        tv_tab_profile.setText(TCUtils.getString(R.string.profile));
        tv_tab_payment.setText(TCUtils.getString(R.string.payment));
    }


    public ArrayList<LanguageModel> getLanguageList() {
        if (myLanguage != null)
            return myLanguage.getLanguageList();
        return null;
    }

    public void disableDoneButton() {
        activity_main_iv_done.setEnabled(false);//when click done button, disable to prevent user multiple click
        activity_main_iv_back.setEnabled(false);
    }

    public void startLocationTracking() {
        startService(new Intent(this, LocationTrackingService.class));
    }

    public void stopLocationTracking() {
        stopService(new Intent(this, LocationTrackingService.class));
    }

    public boolean isHaveGuideLineData() {
        return myFireBase != null
                && myFireBase.getGuidelineModels() != null
                && myFireBase.getGuidelineModels().size() > 0;
    }

    public ArrayList<GuidelineModel> getGuideLineData() {
        return myFireBase.getGuidelineModels();
    }

    public void checkNewNotification() {
        requestApi(new ReviewUserGetNotificationRequest(1,
                this));
    }

    private void checkAndStoreSecretKey() {
        requestApi(new GeneralCheckSecretKeyRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (!response.getSuccess()) {
                    requestApi(new GeneralStoreSecretKeyRequest(new APIResponseListener() {
                        @Override
                        public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        }

                        @Override
                        public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                            TCLog.d(errorModel.getErrorMessage());
                        }
                    }));
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    public void handleDeepLinkIntent(Intent intent) {
        if (!intent.getBooleanExtra("DeepLinkDone", false)) {
            Uri appLinkData = intent.getData();

            if (appLinkData != null) {
                if (appLinkData.toString().contains("page.link")) {
                    FirebaseDynamicLinks.getInstance().getDynamicLink(appLinkData)
                            .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    if (pendingDynamicLinkData != null) {
                                        new TCUniversalLinkProcess(TCMainActivity.this, pendingDynamicLinkData.getLink());
                                        getIntent().putExtra("DeepLinkDone", true);
                                    }
                                }
                            });
                } else {
                    new TCUniversalLinkProcess(this, appLinkData);
                    getIntent().putExtra("DeepLinkDone", true);
                }
            }
        }
    }

    public void updateGoogleAnalyticUserProperty(AccountModel accountModel) {
        if (accountModel != null) {
            TCGoogleAnalyticTrackingEvent.getInstance().setUserProperty(accountModel);//send user property to google analytic
        }
    }

    public void updateStatusButtonHowToEarnTec(boolean show) {
        TCSharePreferenceManager.getInstance().setBoolean(DataKey.ShowDailyTecAway, show);
        TCSharePreferenceManager.getInstance().setBoolean(DataKey.ShowHowToEarnTec, show);
    }

    public boolean isOpening() {
        return isOpening;
    }

    public void setOpening(boolean opening) {
        isOpening = opening;
    }

    public void submitBannerClick(DiscoverBannerModel discoverBannerModel) {
        if (!TCUtils.isEmpty(discoverBannerModel.getUrl_type())) {
            if (discoverBannerModel.getUrl_type().equals(EnumMgr.DiscoverBanner.BaseURL.getValue())) {
                addFragment(WebViewScreen.getInstance(discoverBannerModel.getUrl(), discoverBannerModel.getName()));
            } else {
                handleDeepLink(discoverBannerModel.getUrl());
            }
        }
        discoverBannerModel.setBanner_id(discoverBannerModel.getId());
        requestApi(new UserDiscoverSubmitBannerClickRequest(discoverBannerModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                // todo
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));

    }

    private void handleDeepLink(String link) {
        Uri uri = Uri.parse(link);
        if (uri == null)
            return;
        FirebaseDynamicLinks.getInstance().getDynamicLink(uri)
                .addOnSuccessListener(getActiveActivity(), pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        new TCUniversalLinkProcess(((TCMainActivity) getActiveActivity()), pendingDynamicLinkData.getLink());

                    }
                });
    }

    public double getRate() {
        return rate;
    }

    public double getFee() {
        return fee;
    }

    public double getSpread() {
        return spread;
    }

    public double getBasic_fee_amount() {
        return basic_fee_amount;
    }

    public void setExChangeRates(double rate, double fee, double spread, double basic_fee_amount) {
        this.rate = rate;
        this.fee = fee;
        this.spread = spread;
        this.basic_fee_amount = basic_fee_amount;
    }

    public MyFireBase getMyFireBase() {
        if (null == myFireBase) {
            myFireBase = new MyFireBase(this);
        }
        return myFireBase;
    }


    public void updateLanguage() {
        if (myLanguage != null) {
            myLanguage.updateLanguage();
        }
    }
}
