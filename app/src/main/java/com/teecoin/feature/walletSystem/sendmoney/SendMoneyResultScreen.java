package com.teecoin.feature.walletSystem.sendmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.scanqrCode.InputWalletScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.walletsystem.QRCodeInfoModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetQRCodeInfoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SendMoneyResultScreen extends TCWalletBaseFragment {
    private static final String TRANSACTION_SUCCESS = "success";
    private static final String ERROR_MESSAGE = "ErrorMessage";
    private static final String TRANSFER_MONEY_MODEL = "TRANSFER_MONEY_MODEL";
    @BindView(R.id.frg_transaction_iv_succes)
    ImageView iv_success;
    @BindView(R.id.frg_transaction_tv_success)
    TextView tv_success;
    @BindView(R.id.frg_transaction_tv_error_message)
    TextView tv_error_message;
    @BindView(R.id.frg_transaction_result_ll_transfer_money_account_name)
    View ll_transfer_money_account_name;
    @BindView(R.id.frg_transaction_result_ll_transfer_money_amount)
    View ll_transfer_money_amount;
    @BindView(R.id.frg_transaction_result_tv_total_tee_coin)
    TextView tv_total_tee_coin;
    @BindView(R.id.frg_transaction_success)
    LinearLayout ll_rip_success;
    @BindView(R.id.frg_transaction_fail)
    LinearLayout ll_rip_fail;
    //    @BindView(R.id.frg_transaction_rip_success)
//    RippleBackground rip_success;
//    @BindView(R.id.frg_transaction_rip_fail)
//    RippleBackground rip_fail;
    @BindView(R.id.frg_transaction_result_tv_amount)
    TextView tv_amount;
    @BindView(R.id.frg_transaction_result_tv_transfer_money_account_name)
    TextView tv_transfer_money_account_name;

    @BindView(R.id.frg_send_money_result_ll_detail)
    LinearLayout ll_detail;



    private TransferMoneyModel transferMoneyModel;

    public static SendMoneyResultScreen getInstance(TransferMoneyModel transferMoneyModel, boolean success, String errorMessage) {
        SendMoneyResultScreen screen = new SendMoneyResultScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSFER_MONEY_MODEL, transferMoneyModel);
        bundle.putSerializable(TRANSACTION_SUCCESS, success);
        bundle.putSerializable(ERROR_MESSAGE, errorMessage);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_money_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        boolean isPaymentSuccess = false;
        String errorMessage = "";
        if (bundle != null) {
            isPaymentSuccess = bundle.getBoolean(TRANSACTION_SUCCESS);
            transferMoneyModel = (TransferMoneyModel) bundle.getSerializable(TRANSFER_MONEY_MODEL);
            errorMessage = bundle.getString(ERROR_MESSAGE);
        }
        checkFail(isPaymentSuccess, errorMessage);
        fillData();

        registerSingleClick(R.id.activity_main_bottom_cancel_next_button_tv_back_to_home,
                R.id.activity_main_bottom_cancel_next_button_tv_new_send);
    }

    @Override
    public void displayView() {

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_bottom_cancel_next_button_tv_new_send:
                startScanQR(EnumMgr.RequestCode.SCAN_USER_PUBLIC_KEY.getValue());
                break;
            case R.id.activity_main_bottom_cancel_next_button_tv_back_to_home:
                //openWalletScreen();
                openWalletScreen(false,true);
                break;
        }
    }

    private void tryAgain() {
        startTransferMoney();
    }

    private void checkFail(boolean isPaymentSuccess, String errorMessage) {
        if (isPaymentSuccess) {
            tv_success.setTextAppearance(getContext(), R.style.TCTextViewGoldBold);
            tv_success.setText(TCUtils.getString(R.string.text_successfully).toUpperCase());
            tv_error_message.setVisibility(View.GONE);
            ll_rip_fail.setVisibility(View.GONE);
            ll_rip_success.setVisibility(View.VISIBLE);
            ll_detail.setVisibility(View.VISIBLE);

//            rip_success.startRippleAnimation();
//            rip_fail.stopRippleAnimation();

            tv_error_message.setText(errorMessage);
        } else {
            tv_success.setTextAppearance(getContext(), R.style.TCTextViewGrayBold);
            tv_success.setText(TCUtils.getString(R.string.coupon_unsuccessful).toUpperCase());
            tv_error_message.setVisibility(View.VISIBLE);
            ll_rip_fail.setVisibility(View.VISIBLE);
            ll_rip_success.setVisibility(View.GONE);
            ll_detail.setVisibility(View.GONE);

//            rip_success.stopRippleAnimation();
            //rip_fail.startRippleAnimation();

            tv_error_message.setText(errorMessage);
        }

        ll_transfer_money_amount.setVisibility(View.VISIBLE);
        ll_transfer_money_account_name.setVisibility(View.VISIBLE);
    }

    private void fillData() {
        if (transferMoneyModel != null) {
            tv_transfer_money_account_name.setText(transferMoneyModel.getFull_name());
            tv_amount.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getAmount())));
            tv_total_tee_coin.setText(String.format(
                    TCUtils.getString(R.string.text_parameter_with_tec),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getTotalTeeCoin())));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (rip_fail != null) {
//            if (rip_fail.isRippleAnimationRunning()) {
//                rip_fail.stopRippleAnimation();
//            }
//        }
//        if (rip_success != null) {
//            if (rip_success.isRippleAnimationRunning()) {
//                rip_success.stopRippleAnimation();
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                if (data != null && TCConstant.KEY_PUT_TO_IMPORT_WALLET.equals(data.getStringExtra(TCConstant.KEY_PUT_TO_IMPORT_WALLET))) {
                    replaceFragment(InputWalletScreen.getInstance(), true);
                } else {
                    openWalletScreen();
                }
            } else {
                if (requestCode == EnumMgr.RequestCode.SCAN_USER_PUBLIC_KEY.getValue()) {
                    requestApi(new WalletGetQRCodeInfoRequest(result.getContents(), new APIResponseListener() {
                        @Override
                        public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                            QRCodeInfoModel qrCodeInfoModel = (QRCodeInfoModel) response.getResult();
                            addFragment(SendMoneyScreen.getInstance(false, qrCodeInfoModel));
                        }

                        @Override
                        public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                            showScanFailDialog(requestCode);
                        }
                    }));
                }
            }
        }
    }

    public void startTransferMoney() {
        StellarBusinessProcess.getInstance().startTransferMoney(transferMoneyModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                checkFail(true, "");
                AccountModel userModel = RealmController.getInstance().getData(AccountModel.class);
                userModel.setBalance(TCUtils.formatMoney(
                        TCConstant.SEVEN_DECIMAL_FORMAT,
                        TCUtils.subtract(transferMoneyModel.getTotalTeeCoin(),
                                TCUtils.convertToDouble(userModel.getBalance()))));
                RealmController.getInstance().updateAccountModel(userModel);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                checkFail(false, errorModel.getErrorMessage());
            }
        });
    }
}
