package com.teecoin.feature.general.appflyer;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.teecoin.base.TCApplication;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.HashMap;
import java.util.Map;

public class TCAppFlyerTrackingEvent {

    private static final String AF_DEV_KEY = "maeUtTt5E9zoFeSWYwnnra";
    /**
     * SenderId: get form firebase console ,
     * from login to project on firebase console,
     * choose project setting -> cloud messaging, will see the sender ID
     */
    private static final String senderId = "1019747353212";

    private static final String APPFLYER_CREATE_WALLET_SUCCESS = "create_wallet_success";
    private static final String APPFLYER_CREATE_WALLET_SUCCESS_EMAIL = "create_wallet_success_email";
    private static final String APPFLYER_CREATE_WALLET_SUCCESS_FACEBOOK = "create_wallet_success_facebook";
    private static final String APPFLYER_CREATE_WALLET_SUCCESS_GOOGLE = "create_wallet_success_google";

    /* CREATE WALLET TRACKING EVENT */
    private static final String CREATE_WALLET_INIT = "create_wallet_init";
    private static final String CREATE_WALLET_FORM1 = "create_wallet_form1";
    private static final String CREATE_CORY_SECRET_KEY = "create_wallet_copy_secretkey";
    private static final String CREATE_CHECKED_TOS = "create_wallet_checked_tos";
    /* IMPORT WALLET TRACKING EVENT */
    private static final String IMPORT_WALLET_INIT = "import_wallet_init";
    private static final String IMPORT_WALLET_FAILED_TOS = "import_wallet_failed_tos";
    private static final String IMPORT_WALLET_CHECKED_TOS = "import_wallet_checked_tos";
    private static final String IMPORT_WALLET_NOT_FOUND = "import_wallet_notfound";
    /* CREATE WALLET TRACKING EVENT */
    private static final String IMPORT_WALLET_SUCCESS = "import_wallet_success";
    /* REFERRAL TRACKING EVENT */
    private static final String REFERRAL_PAGE_INIT = "referralpage_init";
    private static final String REFERRAL_INVALID = "referral_invalid";
    private static final String REFERRAL_VALID = "referral_valid";
    private static final String REFERRAL_WALLET_FORM1 = "referral_wallet_form1";
    //    public static final String APPFLYER_IMPORT_WALLET_FORM1  = "import_wallet_form1";
    private static final String REFERRAL_WALLET_COPY_SECRET_KEY = "referral_wallet_copy_secretkey";
    private static final String REFERRAL_WALLET_CHECKED_TOS = "referral_wallet_checked_tos";
    private static final String ACCOUNT_REFERRAL_REFERRAL_CODE_COPY = "account_referral_referralcode_copy";
    private static final String ACCOUNT_REFERRAL_REFERRAL_CODE_SHARE_SNS = "account_referral_referralcode_shareSNS";
    /* REFERRAL TRACKING EVENT */

    /* PHONE NUMBER VALIDATION */
    private static final String PHONE_NUMBER_OTP_SUCCESS = "otp_success";
    private static final String PHONE_NUMBER_OTP_ERROR = "otp_error";
    private static final String PHONE_NUMBER_INVALID_PHONE_NUMBER = "invalid_phone_no";
    /* PHONE NUMBER VALIDATION */

    /* REVIEW */
    private static final String REVIEW_WRITE_REVIEW_BUTTON = "review_writereview_button";
    private static final String REVIEW_WRITE_REVIEW_SUBMIT_FAIL = "review_writereview_submit_fail";
    /* REVIEW */

    /* COUPON */
    private static final String COUPON_PURCHASE_CONFIRM = "coupons_purchase_confirm";
    private static final String COUPON_REDEMPTION_USE_NOW = "coupons_redemption_usenow";
    private static final String COUPON_REDEMPTION_SCAN_QR_CODE = "coupons_redemption_scanQRcode";
    /* COUPON */


    private static volatile TCAppFlyerTrackingEvent instance;
    private boolean didCreateWalletInitTriggered;
    private boolean didCreateWalletForm1Triggered;
    /* IMPORT WALLET TRACKING EVENT */
    private boolean didCreateWalletCopySecretKeyTriggered;
    private boolean didCreateWalletCheckedTosTriggered;
    private boolean didImportWalletFailTosTriggered;
    private boolean didImportWalletCheckedTosTriggered;
    private boolean didImportWalletNotFoundTriggered;
    private boolean didImportWalletSuccessTriggered;
    private boolean didImportWalletInitTriggered;
    private boolean didReferralInitTriggered;
    private boolean didReferralInvalidTriggered;
    private boolean didReferralValidTriggered;
    private boolean didReferralWalletForm1Triggered;
    private boolean didReferralWalletCopySecretKeyTriggered;
    private boolean didCreateWalletSuccessTriggered;
    /* REFERRAL TRACKING EVENT */
    private boolean didReferralWalletCheckedTosTriggered;

    /* PHONE NUMBER VALIDATION */
    private boolean didPhoneNumberOTPSuccessTriggered;
    private boolean didPhoneNumberOTPFailTriggered;
    private boolean didPhoneNumberInvalidTriggered;
    /* PHONE NUMBER VALIDATION */

    private TCAppFlyerTrackingEvent() {
        this.didImportWalletFailTosTriggered = false;
        this.didImportWalletCheckedTosTriggered = false;
        this.didImportWalletNotFoundTriggered = false;
        this.didImportWalletSuccessTriggered = false;
        this.didImportWalletInitTriggered = false;

        this.didCreateWalletInitTriggered = false;
        this.didCreateWalletForm1Triggered = false;
        this.didCreateWalletCopySecretKeyTriggered = false;
        this.didCreateWalletCheckedTosTriggered = false;

        didReferralInitTriggered = false;
        didReferralInvalidTriggered = false;
        didReferralValidTriggered = false;
        didReferralWalletForm1Triggered = false;
        didReferralWalletCopySecretKeyTriggered = false;
        didReferralWalletCheckedTosTriggered = false;

        didPhoneNumberOTPSuccessTriggered = false;
        didPhoneNumberOTPFailTriggered = false;
        didPhoneNumberInvalidTriggered = false;
    }

    public static TCAppFlyerTrackingEvent getInstance() {
        if (instance == null) {
            synchronized (TCAppFlyerTrackingEvent.class) {
                if (instance == null) instance = new TCAppFlyerTrackingEvent();
            }
        }
        return instance;
    }

    public void initAppFlyer() {
        if (canTrackEvent()) {
            AppsFlyerConversionListener appsFlyerConversionListener = new AppsFlyerConversionListener() {
                @Override
                public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                    for (String attrName : conversionData.keySet()) {
                        TCLog.d(AppsFlyerLib.LOG_TAG, "attribute onInstallConversionDataLoaded: " + attrName + " = " + conversionData.get(attrName));
                        if ("is_first_launch".equals(attrName) && Boolean.parseBoolean(conversionData.get(attrName))) {
                            String appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(TCApplication.getActiveActivity());
                            AppsFlyerLib.getInstance().setAppId(appsFlyerId);//set id appsflyer
                            //todo set imei id --> need request permission READ_PHONE_STATE
                            //AppsFlyerLib.newInstance().setImeiData("IMEI_DATA_HERE");
                            AppsFlyerLib.getInstance().setAndroidIdData(TCUtils.getUniquePseudoID());

                            //track when app create wallet success
                            AppsFlyerLib.getInstance().trackEvent(TCApplication.getActiveActivity(), "is_first_app_launch", null);
                        }
                    }
                }

                @Override
                public void onInstallConversionFailure(String errorMessage) {
                    TCLog.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {
                    for (String attrName : map.keySet()) {
                        TCLog.d(AppsFlyerLib.LOG_TAG, "attribute onInstallConversionDataLoaded: " + attrName + " = " + map.get(attrName));
                    }
                }

                @Override
                public void onAttributionFailure(String errorMessage) {
                    TCLog.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
                }
            };
            AppsFlyerLib.getInstance().init(AF_DEV_KEY, appsFlyerConversionListener, TCApplication.getActiveActivity().getApplication());
            AppsFlyerLib.getInstance().enableUninstallTracking(senderId);
            AppsFlyerLib.getInstance().startTracking(TCApplication.getActiveActivity().getApplication(), AF_DEV_KEY);
            AppsFlyerLib.getInstance().registerConversionListener(TCApplication.getActiveActivity().getApplication(), appsFlyerConversionListener);
        }
    }

    public void trackWalletCreationSuccessEmail() {
        if (canTrackEvent()) {
            Map<String, Object> event = new HashMap<>();
            event.put("uuid", TCUtils.isEmpty(RealmController.getInstance().getAccount().getUuid()) ?
                    "" : RealmController.getInstance().getAccount().getUuid());

            //track when app create wallet success
            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS_EMAIL, event);
            //set customer user id
            AppsFlyerLib.getInstance().setCustomerUserId(RealmController.getInstance().getAccount().getUuid());
            //set user email
            AppsFlyerLib.getInstance().setUserEmails(RealmController.getInstance().getAccount().getEmail());

            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS, event);
        }
    }

    public void trackWalletCreationSuccessFacebook() {
        if (canTrackEvent()) {
            Map<String, Object> event = new HashMap<>();
            event.put("uuid", TCUtils.isEmpty(RealmController.getInstance().getAccount().getUuid()) ?
                    "" : RealmController.getInstance().getAccount().getUuid());

            //track when app create wallet success
            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS_FACEBOOK, event);
            //set customer user id
            AppsFlyerLib.getInstance().setCustomerUserId(RealmController.getInstance().getAccount().getUuid());
            //set user email
            AppsFlyerLib.getInstance().setUserEmails(RealmController.getInstance().getAccount().getEmail());

            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS, event);
        }
    }

    public void trackWalletCreationSuccessGoogle() {
        if (canTrackEvent()) {
            Map<String, Object> event = new HashMap<>();
            event.put("uuid", TCUtils.isEmpty(RealmController.getInstance().getAccount().getUuid()) ?
                    "" : RealmController.getInstance().getAccount().getUuid());

            //track when app create wallet success
            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS_GOOGLE, event);
            //set customer user id
            AppsFlyerLib.getInstance().setCustomerUserId(RealmController.getInstance().getAccount().getUuid());
            //set user email
            AppsFlyerLib.getInstance().setUserEmails(RealmController.getInstance().getAccount().getEmail());

            trackAppFlyerEvent(APPFLYER_CREATE_WALLET_SUCCESS, event);
        }
    }

    public void trackCreateWalletInit() {
        if (canTrackEvent()) {
            TCLog.d("hung didCreateWalletInitTriggered:" + didCreateWalletInitTriggered);
            if (!didCreateWalletInitTriggered) {
                trackAppFlyerEvent(CREATE_WALLET_INIT);
                didCreateWalletInitTriggered = true;
            }
        }
    }

    public void trackCreateWalletForm1() {
        if (canTrackEvent()) {
            TCLog.d("hung didCreateWalletForm1Triggered:" + didCreateWalletForm1Triggered);
            if (!didCreateWalletForm1Triggered) {
                trackAppFlyerEvent(TCAppFlyerTrackingEvent.CREATE_WALLET_FORM1);
                didCreateWalletForm1Triggered = true;
            }
        }
    }

    public void trackCreateWalletCopySecretKey() {
        if (canTrackEvent()) {
            TCLog.d("hung didCreateWalletCopySecretKeyTriggered:" + didCreateWalletCopySecretKeyTriggered);
            if (!didCreateWalletCopySecretKeyTriggered) {
                trackAppFlyerEvent(TCAppFlyerTrackingEvent.CREATE_CORY_SECRET_KEY);
                didCreateWalletCopySecretKeyTriggered = true;
            }
        }
    }

    public void trackCreateWalletCheckedTos() {
        if (canTrackEvent()) {
            TCLog.d("hung didCreateWalletCheckedTosTriggered:" + didCreateWalletCheckedTosTriggered);
            if (!didCreateWalletCheckedTosTriggered) {
                trackAppFlyerEvent(TCAppFlyerTrackingEvent.CREATE_CHECKED_TOS);
                didCreateWalletCheckedTosTriggered = true;
            }
        }
    }

    public void trackImportWalletFailTos() {
        if (canTrackEvent()) {
            TCLog.d("hung didImportWalletFailTosTriggered:" + didImportWalletFailTosTriggered);
            if (!didImportWalletFailTosTriggered) {
                trackAppFlyerEvent(IMPORT_WALLET_FAILED_TOS);
                didImportWalletFailTosTriggered = true;
            }
        }
    }

    public void trackImportWalletCheckedTos() {
        if (canTrackEvent()) {
            TCLog.d("hung didImportWalletCheckedTosTriggered:" + didImportWalletCheckedTosTriggered);
            if (!didImportWalletCheckedTosTriggered) {
                trackAppFlyerEvent(IMPORT_WALLET_CHECKED_TOS);
                didImportWalletCheckedTosTriggered = true;
            }
        }
    }

    public void trackImportWalletNotFound() {
        if (canTrackEvent()) {
            TCLog.d("hung didImportWalletNotFoundTriggered:" + didImportWalletNotFoundTriggered);
            if (!didImportWalletNotFoundTriggered) {
                trackAppFlyerEvent(IMPORT_WALLET_NOT_FOUND);
                didImportWalletNotFoundTriggered = true;
            }
        }
    }

    public void trackImportWalletInit() {
        if (canTrackEvent()) {
            TCLog.d("hung didImportWalletInitTriggered:" + didImportWalletInitTriggered);
            if (!didImportWalletInitTriggered) {
                trackAppFlyerEvent(IMPORT_WALLET_INIT);
                didImportWalletInitTriggered = true;
            }
        }
    }

    public void trackImportWalletSuccess() {
        if (canTrackEvent()) {
            TCLog.d("hung didImportWalletSuccessTriggered:" + didImportWalletSuccessTriggered);
            if (!didImportWalletSuccessTriggered) {
                trackAppFlyerEvent(TCAppFlyerTrackingEvent.IMPORT_WALLET_SUCCESS);
                didImportWalletSuccessTriggered = true;
            }
        }
    }


    public void trackReferralInit() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralInitTriggered:" + didReferralInitTriggered);
            if (!didReferralInitTriggered) {
                trackAppFlyerEvent(REFERRAL_PAGE_INIT);
                didReferralInitTriggered = true;
            }
        }
    }

    public void trackReferralInvalid() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralInvalidTriggered:" + didReferralInvalidTriggered);
            if (!didReferralInvalidTriggered) {
                trackAppFlyerEvent(REFERRAL_INVALID);
                didReferralInvalidTriggered = true;
            }
        }
    }

    public void trackReferralValid() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralValidTriggered:" + didReferralValidTriggered);
            if (!didReferralValidTriggered) {
                trackAppFlyerEvent(TCAppFlyerTrackingEvent.REFERRAL_VALID);
                didReferralValidTriggered = true;
            }
        }
    }

    public void trackReferralWalletForm1() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralWalletForm1Triggered:" + didReferralWalletForm1Triggered);
            if (!didReferralWalletForm1Triggered) {
                trackAppFlyerEvent(REFERRAL_WALLET_FORM1);
                didReferralWalletForm1Triggered = true;
            }
        }
    }

    public void trackPhoneNumberOTPSuccess() {
        if (canTrackEvent()) {
            TCLog.d("hung didPhoneNumberOTPSuccessTriggered:" + didPhoneNumberOTPSuccessTriggered);
            if (!didPhoneNumberOTPSuccessTriggered) {
                trackAppFlyerEvent(PHONE_NUMBER_OTP_SUCCESS);
                didPhoneNumberOTPSuccessTriggered = true;
            }
        }
    }

    public void trackPhoneNumberOTPError() {
        if (canTrackEvent()) {
            TCLog.d("hung didPhoneNumberOTPFailTriggered:" + didPhoneNumberOTPFailTriggered);
            if (!didPhoneNumberOTPFailTriggered) {
                trackAppFlyerEvent(PHONE_NUMBER_OTP_ERROR);
                didPhoneNumberOTPFailTriggered = true;
            }
        }
    }

    public void trackPhoneNumberInvalid() {
        if (canTrackEvent()) {
            TCLog.d("hung didPhoneNumberInvalidTriggered:" + didPhoneNumberInvalidTriggered);
            if (!didPhoneNumberInvalidTriggered) {
                trackAppFlyerEvent(PHONE_NUMBER_INVALID_PHONE_NUMBER);
                didPhoneNumberInvalidTriggered = true;
            }
        }
    }



    public void trackReferralWalletCopySecretKey() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralWalletCopySecretKeyTriggered:" + didReferralWalletCopySecretKeyTriggered);
            if (!didReferralWalletCopySecretKeyTriggered) {
                trackAppFlyerEvent(REFERRAL_WALLET_COPY_SECRET_KEY);
                didReferralWalletCopySecretKeyTriggered = true;
            }
        }
    }

    public void trackReferralWalletCheckedTos() {
        if (canTrackEvent()) {
            TCLog.d("hung didReferralWalletCheckedTosTriggered:" + didReferralWalletCheckedTosTriggered);
            if (!didReferralWalletCheckedTosTriggered) {
                trackAppFlyerEvent(REFERRAL_WALLET_CHECKED_TOS);
                didReferralWalletCheckedTosTriggered = true;
            }
        }
    }

    public void trackAccountReferralReferralCodeCopy() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(ACCOUNT_REFERRAL_REFERRAL_CODE_COPY);
        }
    }

    public void trackAccountReferralReferralCodeShareSNS() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(ACCOUNT_REFERRAL_REFERRAL_CODE_SHARE_SNS);
        }
    }

    public void trackReviewWriteReviewButton() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(REVIEW_WRITE_REVIEW_BUTTON);
        }
    }

    public void trackReviewWriteReviewSubmitFail() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(REVIEW_WRITE_REVIEW_SUBMIT_FAIL);
        }
    }

    public void trackCouponPurchaseConfirm() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(COUPON_PURCHASE_CONFIRM);
        }
    }

    public void trackCouponRedemptionUseNow() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(COUPON_REDEMPTION_USE_NOW);
        }
    }

    public void trackCouponRedemptionScanQRCode() {
        if (canTrackEvent()) {
            trackAppFlyerEvent(COUPON_REDEMPTION_SCAN_QR_CODE);
        }
    }

    private void trackAppFlyerEvent(String eventName) {
        AppsFlyerLib.getInstance().trackEvent(TCApplication.getActiveActivity(), eventName, null);
    }

    private void trackAppFlyerEvent(String eventName, Map<String, Object> event) {
        AppsFlyerLib.getInstance().trackEvent(TCApplication.getActiveActivity(), eventName, event);
    }

    private boolean canTrackEvent() {
        return TCUtils.isProductionMode() && TCUtils.isUserApp();
//        return true;
    }

    public void reset() {
        this.didImportWalletFailTosTriggered = false;
        this.didImportWalletCheckedTosTriggered = false;
        this.didImportWalletNotFoundTriggered = false;
        this.didImportWalletSuccessTriggered = false;
        this.didImportWalletInitTriggered = false;

        this.didCreateWalletInitTriggered = false;
        this.didCreateWalletForm1Triggered = false;
        this.didCreateWalletCopySecretKeyTriggered = false;
        this.didCreateWalletCheckedTosTriggered = false;

        didReferralInitTriggered = false;
        didReferralInvalidTriggered = false;
        didReferralValidTriggered = false;
        didReferralWalletForm1Triggered = false;
        didReferralWalletCopySecretKeyTriggered = false;
        didReferralWalletCheckedTosTriggered = false;
    }

    public void updateNewTokenToAppFlyer(String newToken) {
        // Sending new token to AppsFlyer
        AppsFlyerLib.getInstance().updateServerUninstallToken(TCApplication.getActiveActivity().getApplicationContext(), newToken);
    }
}
