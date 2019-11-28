package com.teecoin.feature.reviewSystem.referralInfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

import butterknife.BindView;

public class ReferralInfoScreen extends TCReviewBaseFragment implements APIResponseListener {

    private static final String REFERRAL_URL = "REFERRAL_URL";
    @BindView(R.id.frg_referral_intro_wv_introduction)
    WebView wv_introduction;

    public static ReferralInfoScreen getInstance(String referralUrl) {
        ReferralInfoScreen screen = new ReferralInfoScreen();
        Bundle bundle = new Bundle();
        bundle.putString(REFERRAL_URL, referralUrl);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_referral_info, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String referralUrl = bundle.getString(REFERRAL_URL);
            wv_introduction.getSettings().setJavaScriptEnabled(true);
            wv_introduction.loadUrl(referralUrl);
            wv_introduction.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return false;
                }
            });
        }
    }

    private void initClickEvent() {

    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

    }
}
