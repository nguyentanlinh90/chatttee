package com.teecoin.feature.general.recaptcha;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.retrofit.URL;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class RecaptchaDialog extends TCBaseDialog {

    private static final String reCaptchaURL = String.format("%s%s", URL.getServer(), "clients/reCaptcha_verify/");
    @BindView(R.id.webView)
    WebView webView;

    private GetRecaptchaListener getRecaptchaListener;

    public RecaptchaDialog(Context context, GetRecaptchaListener getRecaptchaListener) {
        super(context);
        this.getRecaptchaListener = getRecaptchaListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_webview);
    }

    @Override
    protected void initContentView() {
        setFullScreen(true);

        if (Build.VERSION.SDK_INT >= 25) {
            TCUtils.setLocale(TCUtils.getLanguageCode());
        }

       // webView.getSettings().setJavaScriptEnabled(true);
        TCUtils.settingWebView(webView);

        webView.addJavascriptInterface(new RecaptchaJavascriptHandler(reCaptcha -> {
            if (getRecaptchaListener != null)
                getRecaptchaListener.getRecaptcha(reCaptcha);
            dismiss();
        }), RecaptchaJavascriptHandler.TC_ANDROID);

        webView.loadUrl(reCaptchaURL);

        setCancelable(false);
    }

    @Override
    protected void onViewClick() {

    }
}
