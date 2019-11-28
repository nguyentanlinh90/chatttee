package com.teecoin.feature.walletSystem.withdraw;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.withdrawSuccess.WithdrawSuccessScreen;
import com.teecoin.feature.walletSystem.withdrawUnSuccess.WithdrawUnSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.walletsystem.ConvertCurrenciesModel;
import com.teecoin.model.walletsystem.PaymentInfoModel;
import com.teecoin.model.walletsystem.WithdrawModel;
import com.teecoin.model.walletsystem.WithdrawPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.ConvertCurrenciesRequest;
import com.teecoin.myapi.apirequest.walletsystem.WithdrawRequest;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopPaymentInfoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.CustomRangeInputFilter;
import com.teecoin.ui.DecimalDigitsInputFilter;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.convertToDouble;

public class WithdrawScreen extends TCWalletBaseFragment {
    private static final String BALANCE_MODEL = "BALANCE_MODEL";

    @BindView(R.id.frg_sgd_wallet_tv_balance)
    TextView tvBalance;
    @BindView(R.id.frg_sgd_wallet_tv_currency)
    TextView tvCurrency;
    @BindView(R.id.frg_wallet_v_equivalent)
    View viewEquivalent;
    @BindView(R.id.frg_sgd_wallet_tv_quivalent)
    TextView tvEquivalent;
    @BindView(R.id.frg_withdraw_tv_note_minimum)
    TextView tvNoteMinimum;
    @BindView(R.id.frg_withdraw_tv_withdraw_title)
    TextView tvWithdrawTitle;
    @BindView(R.id.frg_withdraw_et_amount)
    EditText etAmount;
    @BindView(R.id.frg_withdraw_tv_currency)
    TextView tvCurrencyAmount;
    @BindView(R.id.frg_withdraw_tv_amount_review)
    TextView tvAmountReview;
    @BindView(R.id.frg_withdraw_tv_currency_review)
    TextView tvCurrencyAmountReview;
    @BindView(R.id.frg_withdraw_tv_error_amount)
    TextView tvErrorAmount;
    @BindView(R.id.frg_withdraw_v_fee)
    View vFee;
    @BindView(R.id.frg_withdraw_tv_fee_title)
    TextView tvFeeTitle;
    @BindView(R.id.frg_withdraw_tv_fee)
    TextView tvFee;
    @BindView(R.id.frg_withdraw_tv_fee_currency)
    TextView tvFeeCurrency;
    @BindView(R.id.frg_withdraw_v_fee_review)
    View vFeeReview;
    @BindView(R.id.frg_withdraw_tv_fee_review)
    TextView tvFeeReview;
    @BindView(R.id.frg_withdraw_tv_fee_currency_review)
    TextView tvFeeCurrencyReview;
    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView tvCancel;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView tvNext;
    @BindView(R.id.frg_withdraw_v_input)
    View vInput;
    @BindView(R.id.frg_withdraw_v_input_review)
    View vInputReview;
    @BindView(R.id.frg_withdraw_tv_you_will_earn)
    TextView tv_you_will_earn;


    private PaymentInfoModel paymentInfoModel;
    private BalanceModel balanceModel;
    private WithdrawPostModel withdrawPostModel;

    public static WithdrawScreen getInstance(BalanceModel balanceModel) {
        WithdrawScreen screen = new WithdrawScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BALANCE_MODEL, balanceModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_withdraw, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.withdraw));
        hideFooter();
        showTabMenuBottom();

    }

    @Override
    public void onBindView() {
        getInfoPayment();
        Bundle bundle = getArguments();
        if (null != bundle) {
            balanceModel = (BalanceModel) bundle.getSerializable(BALANCE_MODEL);
        }
        tvCancel.setText(TCUtils.getString(R.string.filter_reset));
        registerSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_cancel_next_bottom_tv_cancel:
                if (vInput.getVisibility() == View.GONE) {
                    //handleBackPressed();
                    openWalletScreen(true,false);
                } else {
                    vInput.setVisibility(View.VISIBLE);
                    etAmount.setText("");
                    etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
                    tvErrorAmount.setVisibility(View.INVISIBLE);
                    vInputReview.setVisibility(View.GONE);
                    vFee.setVisibility(View.VISIBLE);
                    vFeeReview.setVisibility(View.GONE);
                    updateTitleHeader(TCUtils.getString(R.string.withdraw));
                    tvWithdrawTitle.setTextColor(TCUtils.getColor(R.color.c_000000));
                    tvFeeTitle.setTextColor(TCUtils.getColor(R.color.c_000000));
                }
                break;

            case R.id.view_cancel_next_bottom_tv_next:
                if (vInput.getVisibility() == View.VISIBLE) {
                    checkInputData();
                    tvCancel.setText(TCUtils.getString(R.string.text_cancel));
                } else {
                    submitWithdraw();
                }
                break;
        }
    }

    private void getInfoPayment() {
        requestApi(new ShopPaymentInfoRequest(EnumMgr.FiatWalletInfo.TOP_UP.getValue(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                paymentInfoModel = (PaymentInfoModel) response.getResult();

                if (null != balanceModel && null != paymentInfoModel) {
                    initData();
                } else {
                    tvNext.setEnabled(false);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                tvNext.setEnabled(false);
            }
        }));
    }

    private void initData() {
        etAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, convertToDouble(balanceModel.getFiat_amount()))});
        tvBalance.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, balanceModel.getFiat_amount()));
        tvNoteMinimum.setText(String.format(TCUtils.getString(R.string.the_minimun_balance_to_maintained_in_your_s_wallet_we_will_process),
                paymentInfoModel.getCurrency(), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, paymentInfoModel.getMinBalance()), paymentInfoModel.getCurrency()));
        tvCurrency.setText(balanceModel.getCurrency_code());
        tvCurrencyAmount.setText(balanceModel.getCurrency_code());
        tvCurrencyAmountReview.setText(balanceModel.getCurrency_code());
        tvFee.setText(paymentInfoModel.getWithdrawalProcessingFee());
        tvFeeReview.setText(paymentInfoModel.getWithdrawalProcessingFee());
        tv_you_will_earn.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.you_will_earn_an_estimated_title_withdraw_screen), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getInterest_amount()))));
        requestApi(new ConvertCurrenciesRequest(balanceModel.getCurrency_code(), TCUtils.isProductionMode() ? EnumMgr.Currentcy.TEC.getValue()
                : EnumMgr.Currentcy.TEECOIN.getValue(), balanceModel.getFiat_amount(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ConvertCurrenciesModel convertCurrenciesModel = (ConvertCurrenciesModel) response.getResult();
                if (null != convertCurrenciesModel) {
                    viewEquivalent.setVisibility(View.VISIBLE);
                    tvEquivalent.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                            TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, convertCurrenciesModel.getConvertedAmount())));

                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                viewEquivalent.setVisibility(View.INVISIBLE);
            }
        }));
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
                    tvErrorAmount.setVisibility(View.INVISIBLE);
                } else {
                    showError(TCUtils.getString(R.string.please_enter_amount));
                }
            }
        });
    }

    private void showError(String error) {
        etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius));
        tvErrorAmount.setVisibility(View.VISIBLE);
        tvErrorAmount.setText(error);
    }

    private void checkInputData() {
        if (etAmount.getText().toString().length() > 0) {
            withdrawPostModel = new WithdrawPostModel(etAmount.getText().toString(), paymentInfoModel.getCurrency());

            double inputAmount = convertToDouble(withdrawPostModel.getCash_amount());

            if (paymentInfoModel.getCashAmount() <= 0  // current balance <= 0
                    || paymentInfoModel.getCashAmount() <= convertToDouble(paymentInfoModel.getMinBalance()) // current balance <= min balance
                    || convertToDouble(paymentInfoModel.getMinBalance()) + convertToDouble(paymentInfoModel.getWithdrawalProcessingFee()) > paymentInfoModel.getCashAmount() // min + fee > balance
            ) {

                String title = String.format(TCUtils.getString(R.string.s_insufficient), balanceModel.getCurrency_code());
                new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message),
                        title, TCUtils.getString(R.string.your_balance_is_not_sufficient_for_this_transaction), TCUtils.getString(R.string.text_ok)).show();
                return;
            }
            if (inputAmount < 1) {
                showError(TCUtils.getString(R.string.withdraw_amount_must_be_at_least_1_00_sgd));
                return;
            }
            String cashRemain = TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, convertToDouble(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT,
                    paymentInfoModel.getCashAmount()
                            - convertToDouble(paymentInfoModel.getMinBalance()) - convertToDouble(paymentInfoModel.getWithdrawalProcessingFee()))));

            if (convertToDouble(cashRemain) < inputAmount) {
                String title = String.format(TCUtils.getString(R.string.s_insufficient), balanceModel.getCurrency_code());
                String message = String.format(TCUtils.getString(R.string.note_withdraw_instead), cashRemain, balanceModel.getCurrency_code());
                new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message),
                        title, message, TCUtils.getString(R.string.text_ok)).show();
                if (convertToDouble(cashRemain) < 0) {
                    etAmount.setText("0");
                    etAmount.setSelection(0);
                } else {
                    etAmount.setText(cashRemain);
                    etAmount.setSelection(cashRemain.length());
                }
                return;
            }
        } else {
            showError(TCUtils.getString(R.string.please_enter_amount));
            return;
        }

        updateTitleHeader(TCUtils.getString(R.string.withdraw_review));

        tvAmountReview.setText(withdrawPostModel.getCash_amount());

        vInput.setVisibility(View.GONE);
        vInputReview.setVisibility(View.VISIBLE);
        vFee.setVisibility(View.GONE);
        vFeeReview.setVisibility(View.VISIBLE);
        tvWithdrawTitle.setTextColor(TCUtils.getColor(R.color.c_9698a2));
        tvFeeTitle.setTextColor(TCUtils.getColor(R.color.c_9698a2));

    }

    private void submitWithdraw() {
        requestApi(new WithdrawRequest(withdrawPostModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                WithdrawModel withdrawModel = (WithdrawModel) response.getResult();
                addFragment(WithdrawSuccessScreen.getInstance(withdrawModel));
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                addFragment(WithdrawUnSuccessScreen.getInstance(errorModel));
            }
        }));
    }

    public boolean isWithdrawScreen() {
        boolean isWithdraw = true;
        if (vInput.getVisibility() == View.GONE) {
            isWithdraw = false;
            updateTitleHeader(TCUtils.getString(R.string.withdraw));
            vInput.setVisibility(View.VISIBLE);
            vInputReview.setVisibility(View.GONE);
            vFee.setVisibility(View.VISIBLE);
            vFeeReview.setVisibility(View.GONE);
            tvCancel.setText(TCUtils.getString(R.string.filter_reset));
        }
        return isWithdraw;
    }
}
