package com.teecoin.feature.payment.shopInputPayment;

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
import com.teecoin.base.TCBaseAlertDialog;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.popup.DialogConfirmGeneral;
import com.teecoin.feature.payment.shopReviewPayment.ShopReviewPaymentScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.walletsystem.PaymentInfoModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopPaymentInfoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.CustomRangeInputFilter;
import com.teecoin.ui.DecimalDigitsInputFilter;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.convertToDouble;

public class ShopInputPaymentScreen extends TCWalletBaseFragment {
    @BindView(R.id.frg_shop_input_payment_view_parent)
    View view_parent;
    @BindView(R.id.frg_shop_input_payment_iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.frg_shop_input_payment_tv_name)
    TextView tv_name;
    @BindView(R.id.frg_shop_input_payment_tv_address)
    TextView tv_address;

    @BindView(R.id.frg_shop_input_payment_et_total_bill)
    EditText et_total_bill;
    @BindView(R.id.frg_shop_input_payment_tv_error_total_amount)
    TextView tv_error_total_amount;

    @BindView(R.id.frg_shop_input_payment_tv_min_amount_by_wallet)
    TextView tv_min_amount_by_wallet;

    @BindView(R.id.frg_shop_input_payment_et_amount_by_wallet)
    EditText et_amount_by_wallet;
    @BindView(R.id.frg_shop_input_payment_tv_error_amount_wallet)
    TextView tv_error_amount_wallet;
    @BindView(R.id.frg_shop_input_payment_tv_round)
    TextView tv_round;
    @BindView(R.id.frg_shop_input_payment_tv_converted)
    TextView tv_converted;

    @BindView(R.id.frg_shop_input_payment_et_amount_by_cash)
    EditText et_amount_by_cash;
    @BindView(R.id.frg_shop_input_payment_tv_error_amount_cash)
    TextView tv_error_amount_cash;

    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView tv_reset;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView tv_next;

    private PaymentInfoModel paymentInfoModel;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (et_total_bill.getText().hashCode() == editable.hashCode()) {

                removeTextChange(et_amount_by_wallet);
                removeTextChange(et_amount_by_cash);

                if (editable.length() > 0) {
                    showErrorTotal(false);
                    paymentInfoModel.setTotal(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, editable.toString()));
                    paymentInfoModel.setCoinBack(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                            convertToDouble(paymentInfoModel.getTotal()) * (paymentInfoModel.getCoinbackPercentage() / 100)));
                } else {
                    showErrorTotal(true);
                    paymentInfoModel.setTotal("0");
                }
                //reset amounts when change total bill
                et_amount_by_wallet.setText("");
                et_amount_by_cash.setText("");

                et_amount_by_wallet.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, convertToDouble(paymentInfoModel.getTotal()))});
                et_amount_by_cash.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, convertToDouble(paymentInfoModel.getTotal()))});

                addTextChange(et_amount_by_wallet);
                addTextChange(et_amount_by_cash);

            } else if (et_amount_by_wallet.getText().hashCode() == editable.hashCode()) {

                removeTextChange(et_amount_by_cash);

                String amount = "0";
                if (editable.length() > 0) {
                    showErrorAmount(false);
                    amount = editable.toString();

                }
                paymentInfoModel.setAmountWallet(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, amount));
                paymentInfoModel.setInputAmountCash(TCUtils.formatMoney(
                        TCConstant.TWO_DECIMAL_FORMAT, String.valueOf(convertToDouble(paymentInfoModel.getTotal()) - convertToDouble(paymentInfoModel.getAmountWallet()))));
                et_amount_by_cash.setText(paymentInfoModel.getInputAmountCash().replace(",", ""));
                paymentInfoModel.setConvertToTec(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                        calculateToTec(convertToDouble(paymentInfoModel.getAmountWallet()))));
                setTecConvert(paymentInfoModel.getConvertToTec());

                addTextChange(et_amount_by_cash);

            } else if (et_amount_by_cash.getText().hashCode() == editable.hashCode()) {

                removeTextChange(et_amount_by_wallet);
                String cash = "0";
                if (editable.length() > 0) {
                    cash = editable.toString();
                }
                paymentInfoModel.setInputAmountCash(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, cash));
                paymentInfoModel.setAmountWallet(TCUtils.formatMoney(
                        TCConstant.TWO_DECIMAL_FORMAT, convertToDouble(paymentInfoModel.getTotal()) - convertToDouble(paymentInfoModel.getInputAmountCash())));
                et_amount_by_wallet.setText(paymentInfoModel.getAmountWallet().replace(",", ""));

                paymentInfoModel.setConvertToTec(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT,
                        calculateToTec(convertToDouble(paymentInfoModel.getTotal()) - convertToDouble(paymentInfoModel.getInputAmountCash()))));

                setTecConvert(paymentInfoModel.getConvertToTec());

                addTextChange(et_amount_by_wallet);
            }
        }
    };

    public static ShopInputPaymentScreen getInstance() {
        return new ShopInputPaymentScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_input_payment, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.payment));
        hideButtonBackToolbar();
        showFooter();
        showTabMenuBottom();

    }

    @Override
    public void onBindView() {
        paymentInfoModel = new PaymentInfoModel();

        getInfoPayment();

        registerSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
        tv_reset.setText(TCUtils.getString(R.string.filter_reset));
        setUIProfile();

        handelEditText();

        tv_converted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), "0.00"));
        checkDeviceTokenExists();
        checkEdittexFocus();

    }

    private void getInfoPayment() {
        requestApi(new ShopPaymentInfoRequest(EnumMgr.FiatWalletInfo.PAYMENT.getValue(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                paymentInfoModel = (PaymentInfoModel) response.getResult();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                new TCBaseAlertDialog(getActiveActivity(), TCUtils.getString(R.string.text_message),
                        TCUtils.isEmpty(errorModel.getErrorMessage()) ? TCUtils.getString(R.string.something_wen_wrong_please_try_again) : errorModel.getErrorMessage(),
                        TCUtils.getString(R.string.text_ok), (id, onWhat) -> {
                    et_total_bill.setEnabled(false);
                    tv_next.setEnabled(false);
                }).show();
            }
        }));
    }

    private double calculateToTec(double amount) {
        return amount / (paymentInfoModel.getRate() * (100 - paymentInfoModel.getSpread()) * 0.01);
    }

    private void setTecConvert(String value) {
        if (convertToDouble(value) > 0) {
            tv_converted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), paymentInfoModel.getConvertToTec()));
        } else {
            tv_converted.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec), "0.00"));
        }
    }

    private void showErrorTotal(boolean isShow) {
        tv_error_total_amount.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        et_total_bill.setBackground(isShow ? TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius)
                : TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
    }

    private void showErrorAmount(boolean isShow) {
        tv_error_amount_wallet.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        et_amount_by_wallet.setBackground(isShow ? TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius)
                : TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
    }

    private void handelEditText() {
        et_total_bill.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, 999999999.99)});
        et_amount_by_wallet.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, convertToDouble(paymentInfoModel.getTotal()))});
        et_amount_by_cash.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, convertToDouble(paymentInfoModel.getTotal()))});

        et_total_bill.addTextChangedListener(textWatcher);
        et_amount_by_wallet.addTextChangedListener(textWatcher);
        et_amount_by_cash.addTextChangedListener(textWatcher);
    }

    private void removeTextChange(EditText editText) {
        editText.removeTextChangedListener(textWatcher);
    }

    private void addTextChange(EditText editText) {
        editText.addTextChangedListener(textWatcher);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_cancel_next_bottom_tv_next:
                if (TCUtils.isEmpty(et_total_bill.getText().toString())) {
                    showErrorTotal(true);
                    return;
                }
                if (convertToDouble(paymentInfoModel.getTotal()) != (convertToDouble(paymentInfoModel.getAmountWallet()) + convertToDouble(paymentInfoModel.getInputAmountCash()))) {
                    removeTextChange(et_amount_by_wallet);
                    removeTextChange(et_amount_by_cash);
                    showErrorAmount(true);
                    et_amount_by_wallet.setText("");
                    et_amount_by_cash.setText("");
                    tv_min_amount_by_wallet.setVisibility(View.GONE);
                    addTextChange(et_amount_by_wallet);
                    addTextChange(et_amount_by_cash);
                    return;
                }
                gotoPaymentReview();
                break;

            case R.id.view_cancel_next_bottom_tv_cancel:
                reset();
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
    }

    private void setUIProfile() {
        Vendor vendor = RealmController.getInstance().getData(Vendor.class);
        if (vendor != null) {
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(vendor.getLogo()) ? TCUtils.getDrawable(R.drawable.ic_shop_icon)
                    : vendor.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_avatar);
            tv_name.setText(vendor.getName());
            tv_address.setText(!TCUtils.isEmpty(vendor.getAddress()) ? vendor.getAddress() : "");
        }
    }

    private void gotoPaymentReview() {
        TCLog.d("linhnt", "total: " + paymentInfoModel.getTotal() + " : "
                + "amount wallet: " + paymentInfoModel.getAmountWallet() + " : "
                + "amount cash: " + paymentInfoModel.getInputAmountCash() + " : "
                + "coinback : " + paymentInfoModel.getCoinBack());

        if (convertToDouble(et_total_bill.getText().toString()) == 0) {
            showErrorTotal(true);

            return;
        }

        if (convertToDouble(et_amount_by_wallet.getText().toString()) == 0 && convertToDouble(paymentInfoModel.getCoinBack()) == 0) {
            new DialogConfirmGeneral(getActiveActivity(), TCUtils.getString(R.string.text_message),
                    TCUtils.getString(R.string.please_enable_coinback_if_you_want_to_provide_coinback_to_user_who_is_paying_full_amount_with_cash),
                    "", TCUtils.getString(R.string.text_ok), null).show();
            return;
        }

        //just check when coinBack value bigger with input value
        if (convertToDouble(paymentInfoModel.getAmountWallet()) - convertToDouble(paymentInfoModel.getCoinBack()) < 0
                && convertToDouble(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, Math.abs(convertToDouble(paymentInfoModel.getAmountWallet()) - convertToDouble(paymentInfoModel.getCoinBack()))))
                > paymentInfoModel.getMonthlyShopLimitAmount() + paymentInfoModel.getCashAmount()) {

            paymentInfoModel.setMinAmountWallet(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT,
                    convertToDouble(paymentInfoModel.getCoinBack()) - paymentInfoModel.getMonthlyShopLimitAmount() - paymentInfoModel.getCashAmount()));

            new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message), String.format(TCUtils.getString(R.string.s_insufficient), paymentInfoModel.getCurrency()),
                    String.format(TCUtils.getString(R.string.you_do_not_have_sufficient_s_balance_for_the_coinback_incurred_with_this_transaction),
                            paymentInfoModel.getCurrency()), TCUtils.getString(R.string.text_ok)).show();
            tv_min_amount_by_wallet.setText(String.format(TCUtils.getString(R.string.user_needs_to_pay_a_min_of_s_s_to_cover_the_coinback_fee),
                    paymentInfoModel.getMinAmountWallet(), paymentInfoModel.getCurrency()));
            tv_min_amount_by_wallet.setVisibility(View.VISIBLE);
            et_amount_by_wallet.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_red_border_5_radius));
            return;
        }
        tv_min_amount_by_wallet.setVisibility(View.GONE);
        et_amount_by_wallet.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
        addFragment(ShopReviewPaymentScreen.getInstance(paymentInfoModel));

    }

    private void reset() {
        et_total_bill.setText("");
        et_amount_by_wallet.setText("");
        et_amount_by_wallet.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
        et_amount_by_cash.setText("");
        et_amount_by_cash.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));
        showErrorTotal(false);
        tv_error_amount_wallet.setVisibility(View.INVISIBLE);
        tv_error_amount_cash.setVisibility(View.INVISIBLE);
        tv_min_amount_by_wallet.setVisibility(View.INVISIBLE);
        setTecConvert("0");

    }
    private void checkEdittexFocus(){
        TCUtils.checkEditTextHasFocus(et_total_bill);
        TCUtils.checkEditTextHasFocus(et_amount_by_wallet);
        TCUtils.checkEditTextHasFocus(et_amount_by_cash);

//        view_parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = view_parent.getRootView().getHeight() - view_parent.getHeight();
//
//                if (heightDiff > 100) {
//                    hideFooter();
//                } else {
//                    showFooter();
//                }
//            }
//        });
    }
}
