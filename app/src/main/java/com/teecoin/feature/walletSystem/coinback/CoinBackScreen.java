package com.teecoin.feature.walletSystem.coinback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.coinbackResult.CoinBackResultScreen;
import com.teecoin.feature.walletSystem.recoveryPassword.RecoveryPasswordConfirmationDialog;
import com.teecoin.feature.walletSystem.recoveryPassword.RecoveryPasswordConfirmationListener;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.model.walletsystem.CoinBackDetailModel;
import com.teecoin.model.walletsystem.CoinBackModel;
import com.teecoin.model.walletsystem.PaymentDetailModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.Locale;

import butterknife.BindView;

import static com.teecoin.utils.TCConstant.SEVEN_DECIMAL_FORMAT;
import static com.teecoin.utils.TCConstant.THREE_DECIMAL_FORMAT;

public class CoinBackScreen extends TCWalletBaseFragment {

    private static final String PAYMENT_DETAIL_MODEL = "PaymentDetailModel";
    @BindView(R.id.frg_coin_back_tv_payment_id)
    TextView tv_payment_id;
    @BindView(R.id.frg_coin_back_tv_is_success)
    TextView tv_is_success;
    @BindView(R.id.frg_coin_back_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_coin_back_tv_coinback)
    TextView tv_coinback_percent;
    @BindView(R.id.frg_coin_back_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_coin_back_tv_balance)
    TextView tv_balance;
    @BindView(R.id.frg_coin_back_tv_invoice_id)
    TextView tv_invoice_id;
    @BindView(R.id.frg_coin_back_tv_remain)
    TextView tv_remain;
    @BindView(R.id.frg_coin_back_tv_payment_currency)
    TextView tv_payment_currency;
    @BindView(R.id.frg_coin_back_tv_convert_payment)
    TextView tv_convert_payment;
    @BindView(R.id.frg_coin_back_tv_coin_back)
    TextView tv_coin_back;
    @BindView(R.id.frg_coin_back_tv_coin_back_fee)
    TextView tv_coin_back_fee;
    @BindView(R.id.frg_coin_back_tv_total_tee)
    TextView tv_total_tee;
    private CoinBackModel coinBackModel;

    public static CoinBackScreen getInstance(PaymentDetailModel paymentDetailModel) {
        CoinBackScreen screen = new CoinBackScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_DETAIL_MODEL, paymentDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coin_back, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_coin_back).toUpperCase());
        showFooter();
        showMenuNextBottom();
        hideButtonNext();
        showButtonDone();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PaymentDetailModel paymentDetailModel = (PaymentDetailModel) bundle.getSerializable(PAYMENT_DETAIL_MODEL);
            if (paymentDetailModel != null) {
                fillData(paymentDetailModel);
            }
        }
    }

    private void fillData(PaymentDetailModel paymentDetailModel) {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);

        tv_payment_id.setText(String.format(TCUtils.getString(R.string.string_format_1), TCUtils.getString(R.string.history_detail_payment_id), paymentDetailModel.getTransactionId()));
        tv_invoice_id.setText(paymentDetailModel.getInvoice());
        tv_balance.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), accountModel.getBalance()));
        tv_is_success.setText(TCUtils.getString(R.string.text_fail));
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(paymentDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_address.setText(String.format(TCUtils.getString(R.string.coin_back_address), paymentDetailModel.getDestination()));
        //tv_payment.setText(TCUtils.formatMoney(FIVE_DECIMAL_FORMAT, paymentDetailModel.getAmount()));

        tv_coinback_percent.setText(String.format(TCUtils.getString(R.string.string_format_6),
                TCUtils.getString(R.string.coin_back_coinback), paymentDetailModel.getReturn_rate(), TCUtils.getString(R.string.text_percent)));

        double amountConvertToTEC = TCUtils.convertToDouble(paymentDetailModel.getRemainAmount()) / TCUtils.convertToDouble(paymentDetailModel.getRate());

        tv_remain.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, paymentDetailModel.getRemainAmount()));
        tv_payment_currency.setText(paymentDetailModel.getCurrencyCode());

        tv_convert_payment.setText(String.format(TCUtils.getString(R.string.string_format_5),
                TCUtils.formatMoney(THREE_DECIMAL_FORMAT, TCUtils.roundFloor(amountConvertToTEC, TCConstant.ROUND_THREE_DECIMAL))));

        double remain_amount = TCUtils.convertToDouble(paymentDetailModel.getRemainAmount());
        double coinBackAmount = 0;
        //Change: Coin Back is based on Remain: %Coin Back x (Remain / Exchange Rate)
        if (remain_amount > 0) {
            coinBackAmount = CoinBackDetailModel.calculateCoinBack(
                    TCUtils.convertReturnRateToDecimal(
                            TCUtils.convertToDouble(paymentDetailModel.getReturn_rate())
                    ),
                    TCUtils.convertToDouble(paymentDetailModel.getRemainAmount()),
                    TCUtils.convertToDouble(paymentDetailModel.getRate())
            );
        } else {
            coinBackAmount = TCUtils.roundCeil(
                    TCUtils.multiply(
                            TCUtils.convertToDouble(paymentDetailModel.getAmount()),
                            TCUtils.convertReturnRateToDecimal(
                                    TCUtils.convertToDouble(paymentDetailModel.getReturn_rate())
                            )
                    ),
                    TCConstant.ROUND_SEVEN_DECIMAL);
        }


        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
//        double coinBackFee = coinBackAmount * TCConstant.TEECOIN_FEE_RATE;
        double coinBackFee = TCUtils.calculateTeeCoinFee(TCUtils.multiply(coinBackAmount,
                TCUtils.convertReturnRateToDecimal(
                        TCUtils.convertToDouble(feeConfigModel.getFeeRate()))),
                TCConstant.ROUND_SEVEN_DECIMAL);
        //set minimum of CoinBackFee is less than 0.01, set equal to 0.01
        if (coinBackFee < TCConstant.TEECOIN_FEE)
            coinBackFee = TCConstant.TEECOIN_FEE;

        coinBackModel = new CoinBackModel(
                paymentDetailModel.getDestination(),
                paymentDetailModel.getSource(),
                TCUtils.formatMoney(SEVEN_DECIMAL_FORMAT, coinBackAmount, Locale.US),
                TCUtils.formatMoney(SEVEN_DECIMAL_FORMAT, coinBackFee, Locale.US),
                paymentDetailModel.getTransactionHash(),
                paymentDetailModel.getTransactionId(),
                paymentDetailModel.getInvoice(),
                TCUtils.formatMoney(SEVEN_DECIMAL_FORMAT, coinBackAmount + coinBackFee, Locale.US),
                paymentDetailModel.getReturn_rate());


        tv_coin_back.setText(coinBackModel.getAmount());
        tv_coin_back_fee.setText(coinBackModel.getFee_amount());
        tv_total_tee.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), coinBackModel.getTotalAmount()));
    }

    public void gotoCoinBackResult() {
        PaymentThresholdModel paymentThresholdModel = RealmController.getInstance().getData(PaymentThresholdModel.class);
        if (coinBackModel != null && paymentThresholdModel != null) {
            if (coinBackModel.getAmount() != null && coinBackModel.getAmount().length() > 0) {
                double amount = TCUtils.convertToDouble(coinBackModel.getAmount());
                if (amount > paymentThresholdModel.getMaximum()) {
                    RecoveryPasswordConfirmationDialog dialog = new RecoveryPasswordConfirmationDialog(getActiveActivity(), EnumMgr.TransactionType.CoinBack, new RecoveryPasswordConfirmationListener() {
                        @Override
                        public void onPasswordConfirmationSuccess() {
                            addFragment(CoinBackResultScreen.getInstance(coinBackModel));
                        }

                        @Override
                        public void onPasswordConfirmationCancel() {
                        }
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    return;
                }
            }
        }
        addFragment(CoinBackResultScreen.getInstance(coinBackModel));
        ((TCMainActivity) getActiveActivity()).disableDoneButton();
    }
}
