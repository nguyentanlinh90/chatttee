package com.teecoin.feature.walletSystem.convertToTec;

import android.content.Intent;
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
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.convertToTecSuccess.ConvertToTecSuccessScreen;
import com.teecoin.feature.walletSystem.convertToTecUnSuccess.ConvertToTecUnSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.walletsystem.ConvertCurrenciesModel;
import com.teecoin.model.walletsystem.ConvertToTecModel;
import com.teecoin.model.walletsystem.ConvertToTecPostModel;
import com.teecoin.model.walletsystem.PaymentInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralGetShopBalanceRequest;
import com.teecoin.myapi.apirequest.walletsystem.ConvertCurrenciesRequest;
import com.teecoin.myapi.apirequest.walletsystem.ConvertToTecRequest;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopPaymentInfoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.ui.CustomRangeInputFilter;
import com.teecoin.ui.DecimalDigitsInputFilter;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.convertToDouble;

public class ConvertToTecScreen extends TCWalletBaseFragment {
    @BindView(R.id.frg_sgd_wallet_tv_balance)
    TextView tvBalance;
    @BindView(R.id.frg_shop_input_payment_tv_text_sgd)
    TextView tvCurrency;
    @BindView(R.id.frg_wallet_v_equivalent)
    View viewEquivalent;
    @BindView(R.id.frg_sgd_wallet_tv_quivalent)
    TextView tvEquivalent;
    @BindView(R.id.frg_convert_to_tec_tv_note)
    TextView tvNote;
    @BindView(R.id.frg_convert_to_tec_et_amount)
    EditText etAmount;
    @BindView(R.id.frg_convert_to_tec_tv_error_amount)
    TextView tvErrorAmount;
    @BindView(R.id.frg_convert_to_tec_tv_converted)
    TextView tvConverted;
    @BindView(R.id.frg_convert_to_tec_tv_review_amount)
    TextView tvReviewAmount;
    @BindView(R.id.frg_convert_to_tec_tv_review_converted)
    TextView tvReviewConverted;
    @BindView(R.id.frg_convert_to_tec_tv_fee)
    TextView tvFee;
    @BindView(R.id.frg_convert_to_tec_tv_total)
    TextView tvTotal;
    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView tvCancel;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView tvNext;
    @BindView(R.id.frg_convert_to_tec_v_input)
    View vInput;
    @BindView(R.id.frg_convert_to_tec_v_input_review)
    View vInputReview;

    private PaymentInfoModel paymentInfoModel;
    private ConvertToTecPostModel convertToTecPostModel;
    private BalanceModel balanceModel;

    private String NUM_ZERO = "0.00";

    public static ConvertToTecScreen getInstance() {
        ConvertToTecScreen screen = new ConvertToTecScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_convert_to_tec, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.convert_to_tec));
        hideFooter();
        showTabMenuBottom();
        getBalance();
    }

    @Override
    public void onBindView() {
       // getInfoPayment();
        registerSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
        tvCancel.setText(TCUtils.getString(R.string.filter_reset));
    }

    private void getInfoPayment() {
        requestApi(new ShopPaymentInfoRequest(EnumMgr.FiatWalletInfo.TOP_UP.getValue(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                paymentInfoModel = (PaymentInfoModel) response.getResult();
                if (null != balanceModel) {
                    convertToTecPostModel = new ConvertToTecPostModel();
                    initData();
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
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
                   // handleBackPressed();
                    openWalletScreen(true,false);
                } else {

                    vInput.setVisibility(View.VISIBLE);
                    etAmount.setText("");
                    etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
                    tvErrorAmount.setVisibility(View.INVISIBLE);
                    vInputReview.setVisibility(View.GONE);
                    updateTitleHeader(TCUtils.getString(R.string.convert_to_tec));
                }
                break;

            case R.id.view_cancel_next_bottom_tv_next:
                if (vInput.getVisibility() == View.VISIBLE) {
                    checkInputData();
                    tvCancel.setText(TCUtils.getString(R.string.text_cancel));
                } else {
                    submitConvert();
                }
                break;
        }
    }

    private void checkInputData() {
        if (convertToDouble(convertToTecPostModel.getConvert_cash_amount()) > 0) {

            if (paymentInfoModel.getCashAmount() <= 0 || // current balance <= 0
                    paymentInfoModel.getCashAmount() <= convertToDouble(paymentInfoModel.getMinBalance())) // current balance < min balance
            {
                showDialogInsufficient(TCUtils.getString(R.string.your_balance_is_not_sufficient_for_this_transaction));
                return;
            }
            String cashRemain = TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, convertToDouble(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT,
                    paymentInfoModel.getCashAmount() - convertToDouble(paymentInfoModel.getMinBalance()))));
            if (convertToDouble(cashRemain) < convertToDouble(convertToTecPostModel.getConvert_cash_amount())) {
                showDialogInsufficient(String.format(TCUtils.getString(R.string.note_convert_instead), cashRemain, paymentInfoModel.getCurrency()));
                etAmount.setText(cashRemain);
                etAmount.setSelection(cashRemain.length());
                return;
            }
        } else {
            showError(TCUtils.getString(R.string.please_enter_amount));
            return;
        }
        updateTitleHeader(TCUtils.getString(R.string.convert_to_tec_review));
        vInput.setVisibility(View.GONE);
        vInputReview.setVisibility(View.VISIBLE);
    }

    private void showDialogInsufficient(String message) {
        String title = String.format(TCUtils.getString(R.string.s_insufficient), paymentInfoModel.getCurrency());
        new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message),
                title, message, TCUtils.getString(R.string.text_ok)).show();
    }

    private void showError(String error) {
        etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius));
        tvErrorAmount.setVisibility(View.VISIBLE);
        tvErrorAmount.setText(error);
    }

    private void initData() {
        tvBalance.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, balanceModel.getFiat_amount()));
        tvCurrency.setText(balanceModel.getCurrency_code());
        tvNote.setText(String.format(TCUtils.getString(R.string.the_minimun_balance_to_maintained_in_your_s_wallet),
                paymentInfoModel.getCurrency(), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getMinBalance()), paymentInfoModel.getCurrency()));
        tvConverted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), NUM_ZERO));
        tvFee.setText(NUM_ZERO);
        tvTotal.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), NUM_ZERO));

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

        etAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, TCUtils.convertToDouble(balanceModel.getFiat_amount()))});
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && convertToDouble(etAmount.getText().toString()) > 0) {
                    etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
                    tvErrorAmount.setVisibility(View.INVISIBLE);

                    convertToTecPostModel.setConvert_cash_amount(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, etAmount.getText().toString()));

                    paymentInfoModel.setConvertToTec(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                            calculateToTec(convertToDouble(convertToTecPostModel.getConvert_cash_amount()))));

                    paymentInfoModel.setFeeCalculator(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                            (convertToDouble(paymentInfoModel.getConvertToTec()) * convertToDouble(paymentInfoModel.getFee()) * 0.01)
                                    + convertToDouble(paymentInfoModel.getBasicFee())));

                    paymentInfoModel.setTotal(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                            convertToDouble(paymentInfoModel.getConvertToTec()) - convertToDouble(paymentInfoModel.getFeeCalculator())));

                } else {
                    showError(TCUtils.getString(R.string.please_enter_amount));

                    convertToTecPostModel.setConvert_cash_amount(NUM_ZERO);
                    paymentInfoModel.setConvertToTec(NUM_ZERO);
                    paymentInfoModel.setFeeCalculator(NUM_ZERO);
                    paymentInfoModel.setTotal(NUM_ZERO);
                }
                tvConverted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), paymentInfoModel.getConvertToTec()));
                tvReviewAmount.setText(String.format("%s %s", convertToTecPostModel.getConvert_cash_amount(), paymentInfoModel.getCurrency()));
                tvReviewConverted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), paymentInfoModel.getConvertToTec()));
                tvFee.setText(paymentInfoModel.getFeeCalculator());
                tvTotal.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), paymentInfoModel.getTotal()));
            }
        });
    }

    private double calculateToTec(double amount) {
        return (amount / paymentInfoModel.getRate()) * (100 - paymentInfoModel.getSpread()) * 0.01;
    }

    private void submitConvert() {
        convertToTecPostModel.setCurrency_code(paymentInfoModel.getCurrency());
        requestApi(new ConvertToTecRequest(convertToTecPostModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ConvertToTecModel convertToTecModel = (ConvertToTecModel) response.getResult();
                addFragmentForResult(EnumMgr.RequestCode.CONVERT_TO_TEC.getValue(), ConvertToTecSuccessScreen.getInstance(convertToTecModel));

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                //addFragmentForResult(EnumMgr.RequestCode.CONVERT_TO_TEC.getValue(), ConvertToTecUnSuccessScreen.getInstance(convertToTecPostModel, errorModel));
                addFragment(ConvertToTecUnSuccessScreen.getInstance(errorModel));

            }
        }));
    }

    public boolean isConvertScreen() {
        boolean isConvert = true;
        if (vInput.getVisibility() == View.GONE) {
            isConvert = false;
            updateTitleHeader(TCUtils.getString(R.string.convert_to_tec));
            vInput.setVisibility(View.VISIBLE);
            vInputReview.setVisibility(View.GONE);
            tvCancel.setText(TCUtils.getString(R.string.filter_reset));
        }
        return isConvert;
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.CONVERT_TO_TEC.getValue() && finishedResultCode == RESULT_OK) {
            isConvertScreen();
            resetUI();
        }

    }

    private void resetUI() {
        etAmount.setText("");
        tvConverted.setText(TCUtils.getString(R.string.converted_to_tec));
        tvFee.setText(String.format("%s   %s", TCUtils.getString(R.string.text_zero), TCUtils.getString(R.string.tee_coin_symbol)));
        tvTotal.setText(String.format("%s %s", TCUtils.getString(R.string.text_zero), TCUtils.getString(R.string.tee_coin_symbol)));
        etAmount.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
        tvErrorAmount.setVisibility(View.INVISIBLE);
    }
    private void getBalance() {
        requestApi(new GeneralGetShopBalanceRequest(GeneralRequestTarget.SHOP_GET_BALANCE, this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        super.onSuccess(response, requestTarget);
        if (requestTarget == GeneralRequestTarget.SHOP_GET_BALANCE) {
            balanceModel = (BalanceModel) response.getResult();
            getInfoPayment();
           // initData();
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        super.onFail(errorModel, statusCode, requestTarget);
    }
}
