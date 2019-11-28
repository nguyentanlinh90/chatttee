package com.teecoin.feature.general.recaptcha;

import android.webkit.JavascriptInterface;

import com.teecoin.utils.TCLog;

public class RecaptchaJavascriptHandler {

    public static final String TC_ANDROID = "TCAndroid";

    private GetRecaptchaListener getRecaptchaListener;

    public RecaptchaJavascriptHandler(GetRecaptchaListener getRecaptchaListener) {
        this.getRecaptchaListener = getRecaptchaListener;
    }

    /**
     * Key point here is the annotation @JavascriptInterface
     */
    @JavascriptInterface
    public void getRecaptcha(String reCaptcha) {
        TCLog.d("hung", "captcha1:" + reCaptcha);
        if (getRecaptchaListener != null)
            getRecaptchaListener.getRecaptcha(reCaptcha);
    }

}
