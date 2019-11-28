package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.reviewsystem.TipResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckTipScreen extends LinearLayout {

    @BindView(R.id.view_check_tip_tv_balance)
    TextView tv_balance;
    @BindView(R.id.view_check_tip_tv_exchange_rate)
    TextView tv_exchange_rate;
    @BindView(R.id.view_check_tip_tv_tip_amount)
    TextView tv_tip_amount;
    @BindView(R.id.view_check_tip_tv_summary_tip_amount)
    TextView tv_summary_tip_amount;
    @BindView(R.id.view_check_tip_tv_summary_fee)
    TextView tv_summary_fee;
    @BindView(R.id.view_check_tip_tv_summary_total)
    TextView tv_summary_total;
    @BindView(R.id.view_check_tip_tv_tip_exchange_to_currency)
    TextView tv_tip_exchange_to_currency;
    @BindView(R.id.view_check_tip_tv_back)
    View tv_back;
    @BindView(R.id.view_check_tip_tv_ok)
    View tv_ok;

    private TipEventListener listener;
    private TipModel tipModel;

    public CheckTipScreen(Context context, TipModel tipModel, TipEventListener listener) {
        super(context);
        this.listener = listener;
        this.tipModel = tipModel;
        init();
    }

    private CheckTipScreen(Context context) {
        super(context);
        init();
    }

    private CheckTipScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private CheckTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private CheckTipScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        View view = inflate(getContext(), R.layout.view_check_tip, this);
        ButterKnife.bind(CheckTipScreen.this, view);
        initView();
        initClickEvent();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null) {
            tv_balance.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, accountModel.getBalance())));
        }

        tv_exchange_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                tipModel.getExchangeRate(), tipModel.getExchangeCode()));
        double currencyExchange = TCUtils.convertToDouble(tipModel.getAmount()) * tipModel.getExchangeRate();
        tv_tip_exchange_to_currency.setText(
                String.format("≈ %s %s",
                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, currencyExchange),
                        tipModel.getExchangeCode()));

        tv_tip_amount.setText(tipModel.getAmount());
        tv_summary_tip_amount.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, tipModel.getAmount())));
        tv_summary_fee.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, tipModel.getFee_amount())));
        tv_summary_total.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, tipModel.getTotalAmount())));
    }

    private void initClickEvent() {
        tv_back.setOnClickListener(v -> back());
        tv_ok.setOnClickListener(v -> {
            if (checkTip()) {
                startTip();
            }
        });
    }

    private boolean checkTip() {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null) {
            if (TCUtils.convertToDouble(accountModel.getBalance()) <
                    TCUtils.convertToDouble(tipModel.getTotalAmount())) {
                Toast.makeText(getContext(), TCUtils.getString(R.string.balance_not_enough), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void back() {
        if (listener != null)
            listener.onBack();
    }

    private void tipSuccess(TipResponseModel tipResponseModel) {
        if (listener != null) {
            listener.tipSuccess(tipResponseModel);
        }
    }

    private void tipFail(String errorMessage) {
        if (listener != null) {
            listener.tipFail(errorMessage);
        }
    }

    public void startTip() {
        StellarBusinessProcess.getInstance().startTip(tipModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                TipResponseModel tipResponseModel = (TipResponseModel) response.getResult();
                tipResponseModel.setTotalAmount(tipModel.getTotalAmount());
                tipResponseModel.setAmount(tipModel.getAmount());
                tipSuccess(tipResponseModel);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                tipFail(errorModel.getErrorMessage());
            }
        });
    }
}
