package com.teecoin.feature.walletSystem.scanqrCode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.mywallet.MyWalletScreen;
import com.teecoin.feature.walletSystem.sendmoney.SendMoneyScreen;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.QRCodeInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetQRCodeInfoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class InputWalletScreen extends TCWalletBaseFragment implements APIResponseListener {

    @BindView(R.id.frg_input_wallet_et_input)
    EditText et_input;
    @BindView(R.id.frg_input_wallet_iv_clear_secret)
    ImageView ivClearSecret;

    public static InputWalletScreen getInstance() {
        return new InputWalletScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_wallet, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        hideFooter();
        updateTitleHeader(TCUtils.getString(R.string.send_money_input_address));
    }

    @Override
    public void onBindView() {
        showKeyboard(et_input);
        registerSingleClick(R.id.activity_main_bottom_cancel_next_button_tv_next,
                R.id.activity_main_bottom_cancel_next_button_tv_cancel, R.id.frg_input_wallet_iv_clear_secret);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClearSecret.setVisibility(s.length()>0?View.VISIBLE:View.GONE);

            }
        });
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_bottom_cancel_next_button_tv_next:
                gotoTransferScreen();
                break;
            case R.id.activity_main_bottom_cancel_next_button_tv_cancel:
                replaceFragment(isAppUser() ? WalletAccountScreen.getInstance() : MyWalletScreen.getInstance(true), true);
                break;
            case R.id.frg_input_wallet_iv_clear_secret:
                et_input.setText("");
                ivClearSecret.setVisibility(View.GONE);
                break;
        }
    }

    public void gotoTransferScreen() {
        if(TCUtils.isEmpty(et_input.getText().toString())){
            showBaseMessage(TCUtils.getString(R.string.please_input_wallet_address));
            return;
        }
        requestApi(new WalletGetQRCodeInfoRequest(et_input.getText().toString(), this));
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.activity_main_bottom_cancel_next_button_tv_next,
                R.id.activity_main_bottom_cancel_next_button_tv_cancel, R.id.frg_input_wallet_iv_clear_secret);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_QR_CODE_INFO) {
            QRCodeInfoModel qrCodeInfoModel = (QRCodeInfoModel) response.getResult();
//            addFragment(TransferMoneyScreen.getInstance(true, qrCodeInfoModel));
            addFragment(SendMoneyScreen.getInstance(true, qrCodeInfoModel));
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        // showDialogMessageAlert(false, errorModel.getErrorMessage());
        //  showBaseMessage(errorModel.getErrorMessage());
        showBaseMessage(TCUtils.getString(R.string.account_does_not_exists));
    }
}
