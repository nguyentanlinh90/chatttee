package com.teecoin.feature.reviewSystem.reviewforUser;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class WebViewScreen extends TCBaseFragment {
    private static final String URL_DETAIL = "URL_DETAIL";
    private static final String ID = "ID";
    private static final String TITLE = "TITLE";
    private final static int REQUEST_SELECT_FILE_LEGACY = 1;
    private final static int REQUEST_SELECT_FILE = 2;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.view_no_data_tv)
    TextView tv_no_data;
    private String urlDetail;
    private String id;
    private String title = "";
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback mUploadMessageArr;

    public static WebViewScreen getInstance(String url, String title) {
        WebViewScreen screen = new WebViewScreen();
        Bundle bundle = new Bundle();
        bundle.putString(URL_DETAIL, url);
        bundle.putString(TITLE, title);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //TCUtils.setLocale(TCUtils.getLanguageCode());
            ((TCMainActivity) getActiveActivity()).updateLanguage();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            urlDetail = bundle.getString(URL_DETAIL);
            title = bundle.getString(TITLE);

            if (!TCConstant.SHOP_REGISTER.equals(urlDetail)) {
                showFooter();

            }
        }

        if (!title.equals(TCUtils.getString(R.string.text_how_to_earn_tec))) {
            updateTitleHeader(title);
        } else {
            updateTitleHeaderLowerCase(TCUtils.getString(R.string.text_how_to_earn_tec));
        }

        if (!TCUtils.isEmpty(urlDetail)) {

            TCUtils.settingWebView(webView);

            webView.setWebChromeClient(new WebChromeClient() {
                // For Android 5.0+
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (mUploadMessageArr != null) {
                        mUploadMessageArr.onReceiveValue(null);
                        mUploadMessageArr = null;
                    }
                    mUploadMessageArr = filePathCallback;
                    Intent intent = fileChooserParams.createIntent();
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");

                    try {
                        startActivityForResult(intent, REQUEST_SELECT_FILE);
                    } catch (ActivityNotFoundException e) {
                        mUploadMessageArr = null;
                        Toast.makeText(getActivity(), "Select error", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    return true;
                }
            });

            webView.loadUrl(urlDetail);

        } else {
            webView.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
            if (mUploadMessage == null) return;

            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessageArr == null) return;
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessageArr = null;
        }

    }
}
