package com.teecoin.feature.walletSystem.buyTeeCoin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.retrofit.URL;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class BuyTeeCoinScreen extends TCWalletBaseFragment implements APIResponseListener {

    private static final String BUY_TEC_COIN_PATH = "payments/%s"; //%s is destination public key
    private static final String BUY_TEE_COIN_URL = String.format("%s%s", URL.getServer(), BUY_TEC_COIN_PATH);
    private static final String BUY_TEE_COIN_SUCCESS_URL = "teecoin://buy_coin/success";
    @BindView(R.id.frg_buy_tee_coin_wv_buy_tec)
    WebView wv_buy_tec;

    public static BuyTeeCoinScreen getInstance() {
        return new BuyTeeCoinScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy_tee_coin, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_buy_tee_coin).toUpperCase());
        showButtonBackToolbar();
        hideFooter();
        showMenuNextBottom();
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
        //change show soft keyboard into Adjust_resize which allow showing keyboard with layout size for keyboard showing at the bottom
        (getActiveActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void fillData() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        wv_buy_tec.getSettings().setJavaScriptEnabled(true);
        wv_buy_tec.loadUrl(String.format(BUY_TEE_COIN_URL, accountModel.getPublic_key()));
        wv_buy_tec.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().equals(BUY_TEE_COIN_SUCCESS_URL)) {
                    ((TCMainActivity) getActiveActivity()).handleBackPressed();
                    return true;
                } else {
                    view.loadUrl(request.getUrl().toString());
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                if (accountModel != null) {
                    wv_buy_tec.loadUrl("javascript:set_default('" + accountModel.getPublic_key() + "', '" + accountModel.getEmail() + "')");
                }
            }

        });
    }

    private void initClickEvent() {

    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onDestroy() {
        //change show soft keyboard into Adjust_Pan which allow showing keyboard without bottom layout
        (getActiveActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onDestroy();
    }
}
