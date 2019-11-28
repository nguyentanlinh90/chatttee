package com.teecoin.feature.general.googleAnalyticTrackingEvent;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.teecoin.base.TCApplication;
import com.teecoin.model.general.AccountModel;
import com.teecoin.utils.TCUtils;

public class TCGoogleAnalyticTrackingEvent {

    private static final String USER_ID = "userID";
    private static final String EMAIL = "email";
    public static final String TRACK_TRUST_ISSUE_PARAM = "flow_%1$s_screen=%2$s_step_%3$s_event=%4$s";
    private static final String BALANCE = "balance";
    private static volatile TCGoogleAnalyticTrackingEvent instance;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String PUBLIC_KEY = "publickey";
    public static final String COUPON_ID = "coupon_id";
    public static final String VENDOR_ID = "vendor_id";

    private TCGoogleAnalyticTrackingEvent() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(TCApplication.getActiveActivity());
    }

    public static TCGoogleAnalyticTrackingEvent getInstance() {
        if (instance == null) {
            synchronized (TCGoogleAnalyticTrackingEvent.class) {
                if (instance == null) instance = new TCGoogleAnalyticTrackingEvent();
            }
        }
        return instance;
    }

    public void trackScreen(String screenName) {
        if (mFirebaseAnalytics != null && canTrackEvent())
            mFirebaseAnalytics.setCurrentScreen(TCApplication.getActiveActivity(), screenName, screenName);
    }

    public void logEvent(TrackType trackType, String title, Bundle bundle) {
        if (mFirebaseAnalytics != null && canTrackEvent()) {
            if (trackType.equals(TrackType.ScreenIn)) {
                title = TrackType.ScreenIn.getValue() + title;
            } else if (trackType.equals(TrackType.ScreenOut)) {
                title = TrackType.ScreenOut.getValue() + title;
            } else if (trackType.equals(TrackType.Press)) {
                title = TrackType.Press.getValue() + title;
            } else if (trackType.equals(TrackType.IsAccountTrustedWithTeeCoin)) {
                title = TrackType.IsAccountTrustedWithTeeCoin.getValue();
            } else if (trackType.equals(TrackType.TrustTeeCoin)) {
                title = TrackType.TrustTeeCoin.getValue();
            } else if (trackType.equals(TrackType.PurchaseSuccess)) {
                title = TrackType.PurchaseSuccess.getValue();
            }

            mFirebaseAnalytics.logEvent(title, bundle);
        }
    }

    public void setUserProperty(AccountModel accountModel) {
        if (mFirebaseAnalytics != null && canTrackEvent()) {
            mFirebaseAnalytics.setUserProperty(USER_ID, TCUtils.getUniquePseudoID());
            mFirebaseAnalytics.setUserProperty(EMAIL, accountModel != null ? accountModel.getEmail() : "");
            mFirebaseAnalytics.setUserProperty(PUBLIC_KEY,
                    accountModel != null && !TCUtils.isEmpty(accountModel.getPublic_key()) ?
                            accountModel.getPublic_key() : "");
            mFirebaseAnalytics.setUserProperty(BALANCE, accountModel != null && !TCUtils.isEmpty(accountModel.getPublic_key()) ?
                    accountModel.getBalance() : "");
        }
    }

    private boolean canTrackEvent() {
        return TCUtils.isProductionMode() && TCUtils.isUserApp();
//        return true;
    }

    public enum TrackType {
        ScreenIn("screen_in_"),
        ScreenOut("screen_out_"),
        Press("press_"),
        IsAccountTrustedWithTeeCoin("isTrusted"),
        TrustTeeCoin("trustTeeCoin"),
        PurchaseSuccess("purchase_success");

        private String value;

        TrackType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


}
