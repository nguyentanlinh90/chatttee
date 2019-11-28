package com.teecoin.feature.general.terms;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class TermsOfServiceScreen extends TCBaseDialog {
    @BindView(R.id.webView_iv_back)
    ImageView iv_back;
    @BindView(R.id.webView)
    WebView webview;
    @BindView(R.id.webView_tv_title)
    TextView tv_title;

    private String title;
    private String url;

    public TermsOfServiceScreen(Context context,String title,String url) {
        super(context);
        this.title =title;
        this.url =url;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_terms);
    }

    @Override
    protected void initContentView() {
        tv_title.setText(title);
        if(title.equals(TCUtils.getString(R.string.text_how_to_earn_tec))){
            tv_title.setAllCaps(false);
        }
        setFullScreen(false);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.loadUrl(url);

    }

    @Override
    protected void onViewClick() {
        iv_back.setOnClickListener(v -> dismiss());
    }
}
