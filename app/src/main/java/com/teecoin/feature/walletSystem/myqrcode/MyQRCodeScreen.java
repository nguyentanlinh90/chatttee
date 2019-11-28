package com.teecoin.feature.walletSystem.myqrcode;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.orderDetail.ZoomOutQRCodeScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;

public class MyQRCodeScreen extends TCWalletBaseFragment {

    @BindView(R.id.frag_scan_tv_title_header)
    TextView tv_title_header;
    @BindView(R.id.frg_my_qr_code_iv_my_qr)
    ImageView frg_my_qr_code_iv_my_qr;
    @BindView(R.id.frg_my_qr_code_tv_public_key)
    TextView frg_my_qr_code_tv_public_key;
    @BindView(R.id.frag_scan_tv_input)
    TextView frag_scan_tv_input;

    public static MyQRCodeScreen getInstance() {
        return new MyQRCodeScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_qr_code, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        frag_scan_tv_input.setVisibility(View.GONE);
        tv_title_header.setText(TCUtils.getString(R.string.text_receive));
        initClickEvent();
        if (accountModel != null) {
            frg_my_qr_code_tv_public_key.setText(accountModel.getPublic_key());
            if (null != TCUtils.createQRCode(accountModel.getPublic_key()))
                frg_my_qr_code_iv_my_qr.setImageBitmap(TCUtils.createQRCode(accountModel.getPublic_key()));
        }
    }

    public void initClickEvent() {
        registerSingleClick(R.id.frg_scan_iv_back_toolbar, R.id.frg_my_qr_code_iv_my_qr, R.id.frg_my_qr_code_tv_copy_address);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_scan_iv_back_toolbar:
                finishFragment();
                break;
            case R.id.frg_my_qr_code_iv_my_qr:
                zoomOutQRCode();
                break;
            case R.id.frg_my_qr_code_tv_copy_address:
                copyPublicKey();
                break;
        }
    }

    private void copyPublicKey() {
        TCUtils.copyStringToClipboard(frg_my_qr_code_tv_public_key.getText().toString());
        Toast.makeText(getActiveActivity(), R.string.wallet_address_copied, Toast.LENGTH_LONG).show();
    }

    private void zoomOutQRCode() {
        Bitmap bitmap = ((BitmapDrawable) frg_my_qr_code_iv_my_qr.getDrawable()).getBitmap();
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addFragment(ZoomOutQRCodeScreen.getInstance(byteArray));
        }
    }

    private void finishFragment() {
        ((TCMainActivity) getActiveActivity()).setClockScreen(false);
        getActiveActivity().onBackPressed();
    }

}
