package com.teecoin.feature.general.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.feature.general.googleAnalyticTrackingEvent.TCGoogleAnalyticTrackingEvent;
import com.teecoin.feature.general.login.ShopLoginScreen;
import com.teecoin.feature.general.setPasscode.SetPasscodeDialog;
import com.teecoin.feature.general.setPasscode.TCSetPassCodeListener;
import com.teecoin.feature.general.welcome.NewWelcomeScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCSharePreferenceManager;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.isUserApp;

public class SplashScreen extends TCBaseFragment implements TCSetPassCodeListener, StellarResponseListener {

    @BindView(R.id.frg_splash_screen_white_iv_logo)
    ImageView iv_logo;

    public static SplashScreen getInstance() {
        return new SplashScreen();
    }


    private Handler checkLoginHandler;
    private Runnable checkLoginRunnable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen_white, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        hideFooter();
        hideHeader();
        if (checkLoginHandler != null)
            checkLoginHandler.postDelayed(checkLoginRunnable, 2000L);
    }

    @Override
    public void onBindView() {
        initApp();
    }

    private void initApp() {
        Glide.with(this).load(R.drawable.ic_chattee_logo).into(iv_logo);
        checkLoginHandler = new Handler();
        checkLoginRunnable = this::checkLogin;
    }

    private void checkLogin() {
        if (((TCMainActivity) getActiveActivity()).isAlreadyLogin()) {
            if (checkPasscode()) {
                SetPasscodeDialog setPasscodeDialog = new SetPasscodeDialog(getActiveActivity(), true, false, false, this);
                setPasscodeDialog.show();
            } else {
                checkTrustTeeCoinAndGotoHomeScreen();
            }
        } else {
            replaceFragment(isUserApp() ? NewWelcomeScreen.getInstance() : ShopLoginScreen.getInstance(), true);
        }
    }

    private boolean checkPasscode() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel != null && accountModel.isLogin() && !TextUtils.isEmpty(TCSharePreferenceManager.getInstance().getString(DataKey.Passcode));
    }

    @Override
    public void onCloseApp(boolean isClose) {
        if (isClose)
            getActiveActivity().finish();
    }

    @Override
    public void onUnClockSuccess(boolean success) {
        checkTrustTeeCoinAndGotoHomeScreen();
    }

    @Override
    public void onFinishChangePassCode() {

    }

    @Override
    public void onRemovePassCode() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkLoginHandler != null)
            checkLoginHandler.removeCallbacks(checkLoginRunnable);
    }

    @Override
    public void onStellarSuccess(Object result) {
        if (result != null)// check app crash when not connect internet
            Toast.makeText(getActiveActivity(),
                    (boolean) result ?
                            R.string.trust_tee_coin_success : R.string.trust_tee_coin_fail,
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStellarFail(Throwable t) {

    }

    private void checkTrustTeeCoinAndGotoHomeScreen() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        isAccountTrustedWithTeeCoin(accountModel.getPublic_key(), (step, eventLog) -> {
            sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.IsAccountTrustedWithTeeCoin, step, eventLog);
        }, new StellarResponseListener() {
            @Override
            public void onStellarSuccess(Object o) {
                if (o == null || !(boolean) o) {
                    stellarTrustTeeCoin(SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                            true, (step, eventLog) -> {
                                sendTrackTrustIssueToAnalytic(TCGoogleAnalyticTrackingEvent.TrackType.TrustTeeCoin, step, eventLog);
                            }, SplashScreen.this);
                }
            }

            @Override
            public void onStellarFail(Throwable t) {
            }
        });
        ((TCMainActivity) getActiveActivity()).openHomeScreen();
        ((TCMainActivity) getActiveActivity()).processNotification();
    }
}
