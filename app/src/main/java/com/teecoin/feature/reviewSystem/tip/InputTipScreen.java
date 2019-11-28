package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.BuildConfig;
import com.teecoin.R;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputTipScreen extends LinearLayout {
    private static final String NUMBER_ZERO = "0";
    private static final String NUMBER_ONE = "1";
    private static final String NUMBER_FIVE = "5";
    private static final String NUMBER_TEN = "10";
    private static final String NUMBER_TWENTY = "20";
    private static final String NUMBER_FIFTY = "50";
    @BindView(R.id.view_input_tip_et_tip_amount)
    AutoCompleteTextView et_tip_amount;
    @BindView(R.id.view_input_tip_tv_balance)
    TextView tv_balance;
    @BindView(R.id.view_input_tip_tv_exchange_rate)
    TextView tv_exchange_rate;
    @BindView(R.id.view_input_tip_tv_tip_exchange_to_currency)
    TextView tv_tip_exchange_to_currency;
    @BindView(R.id.view_input_tip_tv_cancel)
    View view_input_tip_tv_cancel;
    @BindView(R.id.view_input_tip_tv_next)
    View view_input_tip_tv_next;
    @BindView(R.id.view_input_tip_ll_parent)
    View ll_parent;
    @BindView(R.id.view_input_tip_tv_amount_zero)
    View tv_amount_zero;
    @BindView(R.id.view_input_tip_tv_amount_one)
    View tv_amount_one;
    @BindView(R.id.view_input_tip_tv_amount_five)
    View tv_amount_five;
    @BindView(R.id.view_input_tip_tv_amount_ten)
    View tv_amount_ten;
    @BindView(R.id.view_input_tip_tv_amount_twenty)
    View tv_amount_twenty;
    @BindView(R.id.view_input_tip_tv_amount_fifty)
    View tv_amount_fifty;
    private TipEventListener listener;
    private TipModel tipModel;
//    private TransactionConfigModel transactionConfigModel;
//    private ExchangeRateModel exchangeRateModel;

    public InputTipScreen(Context context, TipModel tipModel, TipEventListener listener) {
        super(context);
        this.listener = listener;
        this.tipModel = tipModel;
        init();
    }

    private InputTipScreen(Context context) {
        super(context);
        init();
    }

    private InputTipScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private InputTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private InputTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        View view = inflate(getContext(), R.layout.view_input_tip, this);
        ButterKnife.bind(InputTipScreen.this, view);
        initData();
        initClickEvent();
    }

    private void initData() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null) {
            tv_balance.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                    TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, accountModel.getBalance())));
        }

        setExchangeRate();

        if (tipModel != null) {
            et_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT,
                    TCUtils.isEmpty(tipModel.getAmount()) ? "" : tipModel.getAmount()));
        }
        tv_tip_exchange_to_currency.setText(String.format("≈ %s %s",
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, 0),
                TCConstant.EXCHANGE_CODE_USD));
//        et_tip_amount.clearFocus();
//        ll_parent.requestFocus();
    }

    private void setExchangeRate() {
        if (BuildConfig.IS_APP_USER) {
            AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
            tv_exchange_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                    accountModel.getRate(), TCConstant.EXCHANGE_CODE_USD));
        } else {
            TransactionConfigModel transactionConfigModel = RealmController.getInstance().getData(TransactionConfigModel.class);
            if (transactionConfigModel != null) {
                tv_exchange_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                        transactionConfigModel.getCoinExchange(), transactionConfigModel.getCode()));
            }
        }

    }

    private void initClickEvent() {
        view_input_tip_tv_next.setOnClickListener(v -> next());
        view_input_tip_tv_cancel.setOnClickListener(v -> close());
        tv_amount_zero.setOnClickListener(v -> et_tip_amount.setText(NUMBER_ZERO));
        tv_amount_one.setOnClickListener(v -> et_tip_amount.setText(NUMBER_ONE));
        tv_amount_five.setOnClickListener(v -> et_tip_amount.setText(NUMBER_FIVE));
        tv_amount_ten.setOnClickListener(v -> et_tip_amount.setText(NUMBER_TEN));
        tv_amount_twenty.setOnClickListener(v -> et_tip_amount.setText(NUMBER_TWENTY));
        tv_amount_fifty.setOnClickListener(v -> et_tip_amount.setText(NUMBER_FIFTY));
        et_tip_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    double value = TCUtils.convertToDouble(s.toString());
                    double fee = TCConstant.LIKE_AND_TIP_FEE_AMOUNT;
                    if (tipModel == null)
                        tipModel = new TipModel();
                    tipModel.setAmount(s.toString());
                    tipModel.setFee_amount(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, fee));
                    if (BuildConfig.IS_APP_USER) {
                        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
                        double currencyExchange = value * accountModel.getRate();
                        tv_tip_exchange_to_currency.setText(String.format("≈ %s %s",
                                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, currencyExchange),
                                        TCConstant.EXCHANGE_CODE_USD));
                        tipModel.setExchangeCode(TCConstant.EXCHANGE_CODE_USD);
                        tipModel.setExchangeRate(accountModel.getRate());
                    } else {
                        TransactionConfigModel transactionConfigModel = RealmController.getInstance().getData(TransactionConfigModel.class);
                        double currencyExchange = value * transactionConfigModel.getCoinExchange();
                        tv_tip_exchange_to_currency.setText(
                                String.format("≈ %s %s",
                                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, currencyExchange),
                                        transactionConfigModel.getCode()));
                        tipModel.setExchangeCode(transactionConfigModel.getCode());
                        tipModel.setExchangeRate(transactionConfigModel.getCoinExchange());
                    }
                } else {
                    tipModel.setAmount("0");
                    tipModel.setFee_amount("0");
                    tv_tip_exchange_to_currency.setText("");
                }

            }
        });
    }

    private void next() {
        if (listener != null) {
            listener.onNext(tipModel);
        }
    }

    private void close() {
        if (listener != null) {
            listener.onClose();
        }
    }
}
