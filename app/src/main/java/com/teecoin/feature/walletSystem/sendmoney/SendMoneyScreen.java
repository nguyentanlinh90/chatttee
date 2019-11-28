package com.teecoin.feature.walletSystem.sendmoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.mywallet.MyWalletScreen;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordDialog;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordListener;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.model.walletsystem.MyQRCodeModel;
import com.teecoin.model.walletsystem.QRCodeInfoModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SendMoneyScreen extends TCWalletBaseFragment implements TCDecisionListener {
    private static final String MY_QR_CODE_MODEL = "MyQRCodeModel";
    private static final String IS_FROM_WALLET_SCREEN = "isFromWalletScreen";
    @BindView(R.id.frg_send_money_iv_account_avatar)
    ImageView iv_account_avatar;
    @BindView(R.id.frg_send_money_tv_account_name)
    TextView tv_account_name;
    @BindView(R.id.frg_send_money_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_send_money_tv_account_public_key)
    TextView tv_account_public_key;
    @BindView(R.id.frg_send_money_et_payment)
    EditText et_payment;
    @BindView(R.id.frg_send_money_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frg_send_money_tv_payment_converted_to_currency)
    TextView tv_payment_converted_to_currency;
    @BindView(R.id.frg_send_money_et_note)
    EditText et_note;
    @BindView(R.id.view_balance_tv_balance_amount)
    TextView tv_balance_amount;
    @BindView(R.id.frg_send_money_tv_total_tec)
    TextView tv_total_tec;
    @BindView(R.id.frg_send_money_tv_minimum_50)
    TextView tv_minimum_50;
    @BindView(R.id.view_balance_v_more_wallet)
    View v_more_wallet;
    @BindView(R.id.view_balance_tv_balance_tec)
    View tv_balance_tec;


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            String textForCheckLength = s.toString().trim();
//            textForCheckLength = textForCheckLength.replace(" ", "").replace("\n", "");
//            int space = 0;
//            for (int i = 0; i < s.length(); i++) {
//                if (Character.isWhitespace(s.charAt(i))) space++;
//            }
//            int maxlength = textForCheckLength.length();ấ
//
//            if (maxlength < 50) {
//                et_note.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50 + space)});
//                tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_maximum), maxlength));
//
//            } else {
//                // et_note.setFilters(new InputFilter[] {new InputFilter.LengthFilter(textForCheckLength.length())});
//                tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_maximum), textForCheckLength.length()));
//            }

            byte[] stringToBytes = s.toString().trim().getBytes();
            tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_maximum), stringToBytes.length));
        }
    };

    TextWatcher tvPaymentTextWatcher = new TextWatcher() {
        String textBeforeChange = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textBeforeChange = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculateAmount(s.toString(), textBeforeChange);
        }
    };
    private boolean checkKeyboard = true;// check change fragment and resume

    public static SendMoneyScreen getInstance(boolean isFromWalletScreen, MyQRCodeModel myQRCodeModel) {
        SendMoneyScreen screen = new SendMoneyScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_WALLET_SCREEN, isFromWalletScreen);
        bundle.putSerializable(MY_QR_CODE_MODEL, myQRCodeModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static SendMoneyScreen getInstance(boolean isFromWalletScreen, QRCodeInfoModel qrCodeInfoModel) {
        SendMoneyScreen screen = new SendMoneyScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_WALLET_SCREEN, isFromWalletScreen);
        bundle.putSerializable(MY_QR_CODE_MODEL, qrCodeInfoModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_money, container, false);
    }

    private TransferMoneyModel transferMoneyModel;

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_send).toUpperCase());
        showButtonBackToolbar();
        hideFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkKeyboard = false;
    }

    private void initClickEvent() {
        et_payment.addTextChangedListener(tvPaymentTextWatcher);
        et_payment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        registerSingleClick(R.id.activity_main_bottom_cancel_next_button_tv_next,
                R.id.activity_main_bottom_cancel_next_button_tv_cancel);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_bottom_cancel_next_button_tv_next:
                gotoNextScreen();
                break;
            case R.id.activity_main_bottom_cancel_next_button_tv_cancel:
                replaceFragment(isAppUser() ? WalletAccountScreen.getInstance() : MyWalletScreen.getInstance(true), true);
                break;
        }
    }

    private void calculateAmount(String payment, String paymentBeforeChange) {
        if (!TCUtils.checkLimitDecimalPlaces(payment, TCConstant.LIMIT_DECIMAL_FIVE)) {
            setValueToPaymentEditText(TCUtils.convertToDouble(paymentBeforeChange));
        } else {
            if (validate(payment)) {
                fillCalculatedAmount();
            } else {
                transferMoneyModel.calculate(0);
                fillCalculatedAmount();
            }
        }
    }

    private void checkPaymentThreshold(String payment) {
        PaymentThresholdModel paymentThresholdModel = RealmController.getInstance().getData(PaymentThresholdModel.class);
        if (paymentThresholdModel.isPayment_threshold() && TCUtils.convertToDouble(payment) + TCConstant.TEECOIN_FEE > paymentThresholdModel.getMaximum()) {
            InputPasswordDialog dialog = new InputPasswordDialog(getActiveActivity(), EnumMgr.TypeDialog.ConfirmPaymentGreaterThanPaymentThreshold.getValue(), new InputPasswordListener() {
                @Override
                public void onSubmit(String inputPassword) {
                    if (validate(payment)) {
                        checkKeyboard = false;
                        transferMoneyModel.setNote(et_note.getText().toString());
                        addFragment(SendMoneyConfirmationScreen.getInstance(transferMoneyModel));
                    }
                }

                @Override
                public void onCancel() {

                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            if (validate(payment)) {
                checkKeyboard = false;
                transferMoneyModel.setNote(et_note.getText().toString());
                addFragment(SendMoneyConfirmationScreen.getInstance(transferMoneyModel));
            }
        }
    }

    private void setValueToPaymentEditText(double payment) {
        et_payment.removeTextChangedListener(tvPaymentTextWatcher);
        et_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, payment));//set payment again if payment >= balance or payment=""
        et_payment.addTextChangedListener(tvPaymentTextWatcher);
        et_payment.setSelection(et_payment.getText().toString().length());
    }

    private void finishFragment() {
        openHomeScreen();
    }

    @Override
    public void onBindView() {
        initClickEvent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean isFromWalletScreen = bundle.getBoolean(IS_FROM_WALLET_SCREEN);
            if (isFromWalletScreen) {
                QRCodeInfoModel qrCodeInfoModel = (QRCodeInfoModel) bundle.getSerializable(MY_QR_CODE_MODEL);
                if (qrCodeInfoModel != null)
                    transferMoneyModel = new TransferMoneyModel(qrCodeInfoModel);
            } else {
                QRCodeInfoModel qrCodeInfoModel = (QRCodeInfoModel) bundle.getSerializable(MY_QR_CODE_MODEL);
                if (qrCodeInfoModel != null)
                    transferMoneyModel = new TransferMoneyModel(qrCodeInfoModel);
            }
        }
        tv_address.setText(String.format("%s:", TCUtils.getString(R.string.text_address)));
        fillData();
        et_note.addTextChangedListener(textWatcher);
        tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_maximum), 0));
        v_more_wallet.setVisibility(View.GONE);
        tv_payment_converted_to_currency.setText(
                String.format(TCUtils.getString(R.string.string_format_1), "0", transferMoneyModel.getExchangeCurrency()));
        tv_balance_tec.setVisibility(View.VISIBLE);
        et_note.setFilters(new InputFilter[]{new ByteLengthFilter(TCConstant.SEND_MONEY_NOTE_MAX_LENGTH_IN_BYTES, "UTF-8")});
    }

    private void fillData() {
        if (transferMoneyModel != null) {
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(transferMoneyModel.getAvatar()) ?
                    TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) :
                    transferMoneyModel.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_account_avatar);


            tv_account_name.setText(transferMoneyModel.getFull_name());
            tv_account_public_key.setText(transferMoneyModel.getDestination());
//            tv_exchange.setText(String.format(TCUtils.getString(R.string.general_exchange_coin), transferMoneyModel.getExchangeRate(), transferMoneyModel.getExchangeCurrency()));
            AccountModel userModel = RealmController.getInstance().getData(AccountModel.class);
            tv_balance_amount.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, userModel.getBalance()));
            transferMoneyModel.setWalletBalance(TCUtils.convertToDouble(userModel.getBalance()));
            transferMoneyModel.setSource(userModel.getPublic_key());
            tv_fee.setText(String.format("%s", TCConstant.TEECOIN_FEE));
            tv_total_tec.setText(String.format("%s", TCConstant.TEECOIN_FEE));

        }
    }

    @Override
    public void onPositiveButtonClicked(int id, Object onWhat) {
        if (id == EnumMgr.CalculationType.OverTeeCoin.getValue()) {
            transferMoneyModel.calculateMaxTeeCoin();
        }
        fillCalculatedAmount();
        setValueToPaymentEditText(transferMoneyModel.getAmount());
    }

    @Override
    public void onNegativeButtonClicked(int id, Object onWhat) {
        calculateAmount("0", "0");
        fillCalculatedAmount();
    }

    @Override
    public void onNeutralButtonClicked(int id, Object onWhat) {

    }

    public boolean validate(String payment) {
        if (TCUtils.isStringNumeric(payment) && !TCUtils.isEmpty(payment)) {
            transferMoneyModel.calculate(TCUtils.convertToDouble(payment));
            if (transferMoneyModel.getAmount() + transferMoneyModel.getTeeCoinFee() > transferMoneyModel.getWalletBalance()) {
                showDecisionDialog(EnumMgr.CalculationType.OverTeeCoin.getValue(), TCUtils.getString(R.string.tec_insufficient),
                        String.format(TCUtils.getString(
                                R.string.transfer_is_greater_than_balance_do_you_want_to_transfer_max_tec),
                                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, TCUtils.subtract(transferMoneyModel.getWalletBalance(), transferMoneyModel.getTeeCoinFee()))),

                        "", TCUtils.getString(R.string.text_cancel), TCUtils.getString(R.string.text_yes), this, null);
                return false;

            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void gotoNextScreen() {
        //hideKeyBoardEditText();
        //hideKeyBoard();
        et_note.clearFocus();
        et_payment.clearFocus();

        if (!isTransferCoinToYourself(transferMoneyModel.getDestination())) {
            String payment = et_payment.getText().toString();
            if (TCUtils.isPaymentZeroOrEmpty(EnumMgr.EditTextValidType.Amount, payment, et_payment)) {
                checkPaymentThreshold(payment);
            }
        }
    }

    private void fillCalculatedAmount() {
        tv_payment_converted_to_currency.setText(String.format(TCUtils.getString(R.string.string_format_1),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT,
                        transferMoneyModel.getPaymentAmountConverted()),
                transferMoneyModel.getExchangeCurrency()));
        tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT,
                transferMoneyModel.getTeeCoinFee()));

        tv_total_tec.setText(
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT,
                        transferMoneyModel.getTotalTeeCoin()));
    }

}
