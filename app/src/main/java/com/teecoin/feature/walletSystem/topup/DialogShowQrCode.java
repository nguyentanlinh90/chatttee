package com.teecoin.feature.walletSystem.topup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogShowQrCode extends TCBaseDialog {

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @BindView(R.id.tv_address_wallet)
    TextView tvAddressWallet;

    private String title;
    private String walletAddress;

    DialogShowQrCode(Context context, String title, String walletAddress) {
        super(context);
        this.title = title;
        this.walletAddress = walletAddress;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_qr_code);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = TCUtils.createQRCode(walletAddress);
                if (null != bitmap)
                    ivQrCode.setImageBitmap(bitmap);
            }
        }, 100L);


        tvTitle.setText(title);
        tvAddressWallet.setText(walletAddress);
    }

    @Override
    protected void onViewClick() {
        ivClose.setOnClickListener(view -> dismiss());
    }
}
