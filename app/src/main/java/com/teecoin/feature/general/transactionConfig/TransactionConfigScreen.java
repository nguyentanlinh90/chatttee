package com.teecoin.feature.general.transactionConfig;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletUpdateAccountRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_CODE;
import static com.teecoin.model.general.TransactionConfigModel.DEFAULT_SYMBOL;

public class TransactionConfigScreen extends TCBaseFragment implements APIResponseListener, TCConfirmListener {

    @BindView(R.id.frg_transaction_config_et_exchange)
    EditText et_exchange;
    @BindView(R.id.frg_transaction_config_et_coinback)
    EditText et_coin_back;
    @BindView(R.id.frg_transaction_config_et_min_amount)
    EditText et_min_amount;
    @BindView(R.id.frg_transaction_config_et_max_payment_rate)
    EditText et_max_payment_rate;
    @BindView(R.id.ll_bt_update)
    LinearLayout ll_bt_update;
    @BindView(R.id.frg_transaction_config_tv_exchange_code)
    TextView tv_exchange_code;
    @BindView(R.id.frg_transaction_config_tv_min_amount_code)
    TextView tv_min_amount_code;

    private TransactionConfigModel transactionConfigModel;

    public static TransactionConfigScreen getInstance() {
        return new TransactionConfigScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_config, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        hideFooter();
    }

    @Override
    public void onBindView() {
        updateTitleHeader(TCUtils.getString(R.string.text_transaction_config).toUpperCase());
        getRealmData();
        onClick();
    }

    private void onClick() {
        ll_bt_update.setOnClickListener(v -> doUpdate());
    }

    private void getRealmData() {
        transactionConfigModel = new TransactionConfigModel(RealmController.getInstance().getData(TransactionConfigModel.class));
        et_exchange.setText(String.format("%s", transactionConfigModel.getCoinExchange()));
        et_coin_back.setText(String.format("%s", transactionConfigModel.getCoinBackRewardRate()));
        et_min_amount.setText(String.format("%s", transactionConfigModel.getMinAmount()));
        et_max_payment_rate.setText(String.format("%s", transactionConfigModel.getMaxPaymentRate()));
        tv_exchange_code.setText(transactionConfigModel.getCode());
        tv_min_amount_code.setText(transactionConfigModel.getCode());
        TCUtils.checkLimitDecimalPlaces(false, et_coin_back, TCConstant.LIMIT_DECIMAL_TWO, null);
        TCUtils.checkLimitDecimalPlaces(false, et_min_amount, TCConstant.LIMIT_DECIMAL_TWO, null);
        TCUtils.checkLimitDecimalPlaces(false, et_exchange, TCConstant.LIMIT_DECIMAL_SEVEN, null);
        TCUtils.checkLimitDecimalPlaces(false, et_max_payment_rate, TCConstant.LIMIT_DECIMAL_SEVEN, null);
    }

    private void doUpdate() {
        hideKeyBoardEditText();
        if (et_exchange.getText().toString().isEmpty() || TCUtils.convertToDouble(et_exchange.getText().toString()) <= 0) {
            et_exchange.setError(TCUtils.getString(R.string.text_please_enter));
            return;
        }
        if (et_coin_back.getText().toString().isEmpty()) {
            et_coin_back.setError(TCUtils.getString(R.string.text_please_enter));
            return;
        }
        if (TCUtils.convertToDouble(et_coin_back.getText().toString()) > TCConstant.DEFAULT_MAX_PAYMENT_RATE) {
            et_coin_back.setError(TCUtils.getString(R.string.text_please_enter_less_than_or_equal_100));
            return;
        }
        if (et_min_amount.getText().toString().isEmpty()) {
            et_min_amount.setError(TCUtils.getString(R.string.text_please_enter));
            return;
        }
        if (et_max_payment_rate.getText().toString().isEmpty()) {
            et_max_payment_rate.setError(TCUtils.getString(R.string.text_please_enter));
            return;
        }
        if (TCUtils.convertToDouble(et_max_payment_rate.getText().toString()) > TCConstant.DEFAULT_MAX_PAYMENT_RATE) {
            et_max_payment_rate.setError(TCUtils.getString(R.string.text_please_enter_less_than_or_equal_100));
            return;
        }
        if (TCUtils.convertToDouble(et_coin_back.getText().toString()) < TCConstant.DEFAULT_MIN_COIN_BACK_REWARD_PERCENT) {
            et_coin_back.setError(TCUtils.getString(R.string.text_please_enter_more_than_or_equal_one_percent));
            return;
        }
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        if (transactionConfigModel == null) {
            transactionConfigModel = new TransactionConfigModel(
                    TCUtils.convertToDouble(et_exchange.getText().toString()),
                    TCUtils.convertToDouble(et_coin_back.getText().toString()),
                    TCUtils.convertToDouble(et_min_amount.getText().toString()),
                    TCUtils.convertToDouble(et_max_payment_rate.getText().toString()),
                    DEFAULT_CODE, DEFAULT_SYMBOL, accountModel.getPublic_key());
        } else {
            transactionConfigModel.setCoinExchange(TCUtils.convertToDouble(et_exchange.getText().toString()));
            transactionConfigModel.setCoinBackRewardRate(TCUtils.convertToDouble(et_coin_back.getText().toString()));
            transactionConfigModel.setMinAmount(TCUtils.convertToDouble(et_min_amount.getText().toString()));
            transactionConfigModel.setMaxPaymentRate(TCUtils.convertToDouble(et_max_payment_rate.getText().toString()));
        }
        RealmController.getInstance().updateTransactionConfig(transactionConfigModel);
        accountModel.setMin_amount(TCUtils.convertToDouble(et_min_amount.getText().toString()));
        accountModel.setReturn_rate(TCUtils.convertToDouble(et_coin_back.getText().toString()));
        accountModel.setRate(TCUtils.convertToDouble(et_exchange.getText().toString()));
        accountModel.setLanguage(TCUtils.getLanguageCode());

        requestApi(new WalletUpdateAccountRequest(accountModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT) {
                    Toast.makeText(getActiveActivity(), "Transaction Updated", Toast.LENGTH_SHORT).show();
                    RealmController.getInstance().updateAccountModel(accountModel);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                if (requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT) {
                    Toast.makeText(getActiveActivity(), errorModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT) {
            Toast.makeText(getActiveActivity(), errorModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        tv_min_amount_code.setText(transactionConfigModel.getCode());
        tv_exchange_code.setText(transactionConfigModel.getCode());
    }
}
