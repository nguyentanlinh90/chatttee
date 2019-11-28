package com.teecoin.feature.walletSystem.sendmoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.mywallet.MyWalletScreen;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SendMoneyConfirmationScreen extends TCWalletBaseFragment {
    private static final String TRANSFER_MONEY_MODEL = "TRANSFER_MONEY_MODEL";

    @BindView(R.id.frg_send_money_confirmation_iv_account_avatar)
    ImageView iv_account_avatar;
    @BindView(R.id.frg_send_money_confirmation_tv_account_name)
    TextView tv_account_name;
    @BindView(R.id.frg_send_money_confirmation_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_send_money_confirmation_tv_account_public_key)
    TextView tv_account_public_key;
    @BindView(R.id.frg_send_money_confirmation_tv_payment)
    TextView tv_payment;
    @BindView(R.id.frg_send_money_confirmation_tv_note)
    TextView tv_note;
    @BindView(R.id.frg_send_money_confirmation_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frg_send_money_confirmation_tv_total_tec)
    TextView tv_total_tec;
    @BindView(R.id.view_balance_tv_balance_amount)
    TextView tv_balance_amount;

    @BindView(R.id.view_balance_v_more_wallet)
    View v_more_wallet;

    @BindView(R.id.activity_main_bottom_cancel_next_button_tv_next)
    TextView cancel_next_button_tv_next;




    TransferMoneyModel transferMoneyModel;

    public static SendMoneyConfirmationScreen getInstance() {
        SendMoneyConfirmationScreen screen = new SendMoneyConfirmationScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    public static SendMoneyConfirmationScreen getInstance(TransferMoneyModel transferMoneyModel) {
        SendMoneyConfirmationScreen screen = new SendMoneyConfirmationScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSFER_MONEY_MODEL, transferMoneyModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_money_confirmation, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_send).toUpperCase());
        showButtonBackToolbar();
        hideFooter();
        cancel_next_button_tv_next.setEnabled(true);
    }

    @Override
    public void onBindView() {
        initClickEvent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            transferMoneyModel = (TransferMoneyModel) bundle.getSerializable(TRANSFER_MONEY_MODEL);
            fillData();
        }
        v_more_wallet.setVisibility(View.GONE);
        cancel_next_button_tv_next.setText(TCUtils.getString(R.string.confirm));
    }

    private void initClickEvent() {
//        iv_lose.setOnClickListener(v -> finishFragment());
//        tv_payment.addTextChangedListener(tvPaymentTextWatcher);
//        rl_scan_qr.setOnClickListener(v -> gotoScanQRCode(EnumMgr.RequestCode.SCAN_USER_PUBLIC_KEY.getValue()));
//        btn_submit_payment.setOnClickListener(v -> {
//        });
        registerSingleClick(R.id.activity_main_bottom_cancel_next_button_tv_next,
                R.id.activity_main_bottom_cancel_next_button_tv_cancel);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_bottom_cancel_next_button_tv_next:
                cancel_next_button_tv_next.setEnabled(false);
                startTransferMoney();
                break;
            case R.id.activity_main_bottom_cancel_next_button_tv_cancel:
                replaceFragment(isAppUser() ? WalletAccountScreen.getInstance() : MyWalletScreen.getInstance(true), true);
                break;
        }
    }

    private void fillData() {
        if (transferMoneyModel != null) {
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(transferMoneyModel.getAvatar()) ?
                    TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)
                    : transferMoneyModel.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_account_avatar);

            tv_account_name.setText(transferMoneyModel.getFull_name());
            tv_address.setText(String.format("%s:", TCUtils.getString(R.string.text_address)));
            tv_account_public_key.setText(transferMoneyModel.getDestination());
            tv_balance_amount.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, transferMoneyModel.getWalletBalance()));
            tv_note.setText(transferMoneyModel.getNote());
            tv_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, transferMoneyModel.getAmount()));
            tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getTeeCoinFee()));
            tv_total_tec.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT,
                    transferMoneyModel.getTotalTeeCoin()));

        }
    }

    public void startTransferMoney() {
        StellarBusinessProcess.getInstance().startTransferMoney(transferMoneyModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                TransferMoneyModel responseModel = (TransferMoneyModel) response.getResult();
                transferMoneyModel.setTransactionId(responseModel.getTransactionId());
//                addFragment(TransferMoneyResultScreen.getInstance(transferMoneyModel, true, ""));
                addFragment(SendMoneyResultScreen.getInstance(transferMoneyModel, true, ""));
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                addFragment(SendMoneyResultScreen.getInstance(transferMoneyModel, false, errorModel.getErrorMessage()));
            }
        });
    }

    @Override
    public void onDestroy() {
        if (cancel_next_button_tv_next != null) {
            cancel_next_button_tv_next.setEnabled(true);
        }
        super.onDestroy();
    }
}
