package com.teecoin.base;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apimanager.GeneralApiManager;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apimanager.WalletApiManager;
import com.teecoin.myapi.apirequest.APIBaseRequest;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.apirequest.GeneralApiRequest;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.apirequest.WalletApiRequest;
import com.teecoin.realmdb.RealmController;
import com.teecoin.retrofit.RetrofitGenerator;
import com.teecoin.retrofit.URL;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import core.base.BaseActivity;
import core.base.BaseDialog;
import core.base.BaseFragment;

public abstract class TCBaseActivity extends BaseActivity implements TCBaseInterface {

    //    private APIManager apiManager = null;
    private CouponApiManager couponApiManager = null;
    private ReviewApiManager reviewApiManager = null;
    private WalletApiManager walletApiManager = null;
    private GeneralApiManager generalApiManager = null;
    private BaseDialog singleDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApiManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public int getFragmentContainerResId() {
        return R.id.activity_main_fl_main;
    }

    private void initializeApiManager() {
//        apiManager = RetrofitGenerator.createService(APIManager.class, URL.getServer());
        couponApiManager = RetrofitGenerator.createService(CouponApiManager.class, URL.getServer());
        reviewApiManager = RetrofitGenerator.createService(ReviewApiManager.class, URL.getServer());
        walletApiManager = RetrofitGenerator.createService(WalletApiManager.class, URL.getServer());
        generalApiManager = RetrofitGenerator.createService(GeneralApiManager.class, URL.getServer());
    }

    public String getUserToken() {
        return TCConstant.TOKEN + TCSharePreferenceManager.getInstance().getString(DataKey.Token);
    }

    @Override
    public void requestApi(APIBaseRequest apiBaseRequest) {
        if (!TCUtils.isNetworkConnectionAvailable()) {
            hideLoadingDialog();
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
            return;
        }

        if (apiBaseRequest != null && apiBaseRequest.isLoading())
            showLoadingDialog();

        if (apiBaseRequest instanceof CouponApiRequest) {
            ((CouponApiRequest) apiBaseRequest).requestApi(couponApiManager, this);
        } else if (apiBaseRequest instanceof ReviewApiRequest) {
            ((ReviewApiRequest) apiBaseRequest).requestApi(reviewApiManager, this);
        } else if (apiBaseRequest instanceof WalletApiRequest) {
            ((WalletApiRequest) apiBaseRequest).requestApi(walletApiManager, this);
        } else if (apiBaseRequest instanceof GeneralApiRequest) {
            ((GeneralApiRequest) apiBaseRequest).requestApi(generalApiManager, this);
        }
    }

    @Override
    public final boolean isAppUser() {
        return TCUtils.isUserApp();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isAlreadyLogin() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel != null && accountModel.isLogin();
    }

    public void addFragment(BaseFragment fragment) {
        addFragment(getFragmentContainerResId(), fragment);
    }

    public void replaceFragment(BaseFragment fragment, boolean clearStack) {
        replaceFragment(getFragmentContainerResId(), fragment, clearStack);
    }

    public BaseFragment getTopFragment() {
        return getTopFragment(getFragmentContainerResId());
    }

    public void backStack() {
        backStack(getFragmentContainerResId());
    }

    /**
     * this method control show only 1 dialog at 1 time
     *
     * @param dialog
     */
    public void showSingleDialog(BaseDialog dialog) {
        if (singleDialog != null)
            singleDialog.dismiss();
        singleDialog = dialog;
        if (singleDialog != null)
            singleDialog.show();
    }

}
