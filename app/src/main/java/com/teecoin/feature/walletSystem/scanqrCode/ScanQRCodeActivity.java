package com.teecoin.feature.walletSystem.scanqrCode;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.teecoin.R;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.teecoin.utils.TCConstant.KEY_PUT_TO_IMPORT_WALLET;

public class ScanQRCodeActivity extends AppCompatActivity implements TCConfirmListener {
    @BindView(R.id.frg_scan_iv_back_toolbar)
    ImageView iv_back;
    @BindView(R.id.frag_scan_tv_title_header)
    TextView tv_title_header;
    @BindView(R.id.frg_scan_qr_tv_title)
    TextView tv_title;
    @BindView(R.id.frg_scan_qr_view_my_qr_code)
    LinearLayout ll_view_qr_code;
    @BindView(R.id.frg_scan_qr_tv_use_validate_code)
    TextView tvUseValidateCode;
    @BindView(R.id.frag_scan_tv_input)
    TextView frag_scan_tv_input;
    private CaptureManager capture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_scan_qr_code);

        ButterKnife.bind(this);
        DecoratedBarcodeView barcodeScannerView = findViewById(R.id.frg_scan_qr_dbv_zxing_scanner);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
        Bundle bundle = getIntent().getExtras();
        tv_title_header.setText(TCUtils.getString(R.string.text_send).toUpperCase());
        if (bundle != null) {

            String text = bundle.getString(TCConstant.SCAN_QR_CODE_WITH_VALIDATE_CODE);
            if (text != null && text.equals(String.valueOf(EnumMgr.RequestCode.SCAN_COUPON_CODE.getValue()))) {
                tvUseValidateCode.setVisibility(View.VISIBLE);
                tv_title_header.setText(TCUtils.getString(R.string.text_scan_qr_code));
            }

            text = bundle.getString(TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY);
            if (text != null && text.equals(String.valueOf(EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()))) {
                tv_title_header.setText(TCUtils.getString(R.string.text_check_in));
            } else if (text != null && text.equals(String.valueOf(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue()))) {
                tv_title_header.setText(TCUtils.getString(R.string.input_payment_payment));
                tv_title.setVisibility(View.VISIBLE);
            }

            frag_scan_tv_input.setVisibility((!TCUtils.isEmpty(text)
                    && text.equals(TCConstant.SCAN_QR_CODE_FOR_TRANSFER_MONEY))
                    ? View.VISIBLE : View.GONE);
        }
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    private void initView() {
        //   StatusBarUtil.setTransparent(this);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initClickEvent();
    }

    private void initClickEvent() {
        iv_back.setOnClickListener(v -> onFinishActivity());
        tvUseValidateCode.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(ScanQRCodeActivity.RESULT_OK, intent);
            onFinishActivity();
        });
        frag_scan_tv_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_PUT_TO_IMPORT_WALLET, KEY_PUT_TO_IMPORT_WALLET);
                setResult(RESULT_OK, intent);
                onFinishActivity();
            }
        });
    }

    private void onFinishActivity() {
        finish();
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        //barcodeScannerView.decodeSingle(callback);
    }
}
