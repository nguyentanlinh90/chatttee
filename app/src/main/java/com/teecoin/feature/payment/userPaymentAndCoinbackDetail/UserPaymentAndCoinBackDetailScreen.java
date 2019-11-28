package com.teecoin.feature.payment.userPaymentAndCoinbackDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.UserCoinBackDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetCoinBackDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class UserPaymentAndCoinBackDetailScreen extends TCWalletBaseFragment implements APIResponseListener {
    private static final String TRANSACTION_DETAIL_MODEL = "TransactionDetailRealmModel";
    private static final String PUSH_NOTIFICATION_DETAIL_MODEL = "PushNotificationModel";

    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_vendor;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_address)
    TextView tv_address;

    @BindView(R.id.frg_user_payment_coinback_detail_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_converd)
    TextView tv_converd;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_amount_cash)
    TextView tv_amount_cash;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_amount)
    TextView tv_amount;

    @BindView(R.id.frg_user_payment_coinback_detail_tv_amount_coinback)
    TextView tv_amount_coinback;

    @BindView(R.id.frg_user_payment_coinback_detail_tv_coinback)
    TextView tv_coinback;

    @BindView(R.id.frg_user_payment_coinback_detail_view_amount_coinback)
    View view_amount_coinback;
    @BindView(R.id.frg_user_payment_coinback_detail_view_coinback)
    View view_coinback;
    @BindView(R.id.frg_user_payment_coinback_detail_view_amount_payment)
    View view_amount_payment;


    private TransactionDetailModel transModel;
    private PushNotificationModel pushNotificationModel;
    private Vendor vendor;
    private String title = "";

    public static UserPaymentAndCoinBackDetailScreen getInstance(TransactionDetailModel model) {
        UserPaymentAndCoinBackDetailScreen screen = new UserPaymentAndCoinBackDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_DETAIL_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserPaymentAndCoinBackDetailScreen getInstance(PushNotificationModel model) {
        UserPaymentAndCoinBackDetailScreen screen = new UserPaymentAndCoinBackDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PUSH_NOTIFICATION_DETAIL_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_payment_coinback_detail, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(title.toUpperCase());
        showReadAllNotification(false);
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            transModel = (TransactionDetailModel) bundle.getSerializable(TRANSACTION_DETAIL_MODEL);
            pushNotificationModel = (PushNotificationModel) bundle.getSerializable(PUSH_NOTIFICATION_DETAIL_MODEL);
            getDetail();
        }
        registerSingleClick(R.id.frg_history_detail_tv_payment_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frg_history_detail_tv_payment_id);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_history_detail_tv_payment_id:
                gotoVendorDetail();
                break;
        }
    }

    private void gotoVendorDetail() {
        if (vendor != null)
            addFragment(VendorScreen.getInstance(vendor.getId()));
    }

    private void getDetail() {
        tv_vendor.setTextColor(TCUtils.getColor(R.color.c_b2973f));
        if (transModel != null) {
            if (transModel.getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
                getPaymentDetail(transModel.getTransaction_id());
            } else if (transModel.getType().equals(EnumMgr.TransactionType.CoinBack.getValue())) {// CoinBack
                getCoinBackDetail(transModel.getTransaction_id());
            }
        } else if (pushNotificationModel != null) {
            if (pushNotificationModel.getData().getType() != null) {
                if (pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
                    getPaymentDetail(pushNotificationModel.getData().getTransaction_id());
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.CoinBack.getValue())) {
                    getCoinBackDetail(pushNotificationModel.getData().getTransaction_id());
                }
            }
        }
    }

    private void fillPaymentData(PaymentInvoiceDetailModel paymentDetailModel) {
        title = TCUtils.getString(R.string.payment_detail);
        updateTitleHeader(title);
        vendor = paymentDetailModel.getVendor();
        if (vendor != null) {
            tv_vendor.setText(Html.fromHtml("<u>" + vendor.getName() + "</u>"));
        }
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(paymentDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FULL_FORMAT, paymentDetailModel.getRate()),
                paymentDetailModel.getCurrency_code()));
        tv_address.setText(String.format(
                TCUtils.getString(R.string.string_format_1),
                TCUtils.getString(R.string.coin_back_address1),
                paymentDetailModel.getPaid_amount_destination()));


        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getInvoice_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getPaid_amount()), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_converd.setText(String.format("%s %s %s", TCUtils.getString(R.string.text_character), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getPaid_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentDetailModel.getFee_amount()) + TCUtils.convertToDouble(paymentDetailModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getRemain_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentDetailModel.getPaid_amount()) + TCUtils.convertToDouble(paymentDetailModel.getFee_amount()) + TCUtils.convertToDouble(paymentDetailModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));

        view_amount_coinback.setVisibility(View.GONE);
        view_coinback.setVisibility(View.GONE);
    }

    private void fillCoinBackData(UserCoinBackDetailModel coinBackDetailModel) {
        title = TCUtils.getString(R.string.coinback_detail);
        updateTitleHeader(title);
        vendor = coinBackDetailModel.getVendor();
        if (vendor != null) {
            tv_vendor.setText(Html.fromHtml("<u>" + vendor.getName() + "</u>"));
        }
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(coinBackDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FULL_FORMAT, coinBackDetailModel.getRate()),
                coinBackDetailModel.getCurrency_code()));
        tv_address.setText(String.format(
                TCUtils.getString(R.string.string_format_1),
                TCUtils.getString(R.string.coin_back_address1),
                coinBackDetailModel.getSource()));

        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, coinBackDetailModel.getInvoice_cash_amount()), coinBackDetailModel.getCurrency_code()));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, coinBackDetailModel.getPaid_amount()), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_converd.setText(String.format("%s %s %s", TCUtils.getString(R.string.text_character), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, coinBackDetailModel.getPaid_cash_amount()), coinBackDetailModel.getCurrency_code()));
        tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(coinBackDetailModel.getFee_amount()) + TCUtils.convertToDouble(coinBackDetailModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, coinBackDetailModel.getRemain_cash_amount()), coinBackDetailModel.getCurrency_code()));
        tv_amount_coinback.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(coinBackDetailModel.getPaid_amount()) + TCUtils.convertToDouble(coinBackDetailModel.getFee_amount()) + TCUtils.convertToDouble(coinBackDetailModel.getBasic_fee_amount())), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_coinback.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, coinBackDetailModel.getCoinback_amount()), TCUtils.getString(R.string.tee_coin_symbol)));

        view_amount_payment.setVisibility(View.GONE);


    }

    private void getCoinBackDetail(String transaction_id) {
        requestApi(new WalletGetCoinBackDetailRequest(transaction_id, this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            fillPaymentData((PaymentInvoiceDetailModel) response.getResult());
        } else if (requestTarget == WalletRequestTarget.GET_COIN_BACK_DETAIL) {
            fillCoinBackData((UserCoinBackDetailModel) response.getResult());
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok)).show();
    }
}
