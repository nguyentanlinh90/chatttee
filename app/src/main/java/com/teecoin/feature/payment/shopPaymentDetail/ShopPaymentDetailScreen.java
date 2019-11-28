package com.teecoin.feature.payment.shopPaymentDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ShopPaymentDetailScreen extends TCWalletBaseFragment implements APIResponseListener {

    private static final String TRANSACTION_ID = "TRANSACTION_ID";


    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_payment_id;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;
    @BindView(R.id.frg_shop_payment_detail_tv_customer)
    TextView tv_customer;
    @BindView(R.id.frg_shop_payment_detail_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frg_shop_payment_detail_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frg_shop_payment_detail_ll_coinback)
    View ll_coinback;
    @BindView(R.id.frg_shop_payment_detail_tv_coinback)
    TextView tv_coinback;
    @BindView(R.id.frg_shop_payment_detail_tv_amount_cash)
    TextView tv_amount_cash;
    @BindView(R.id.frg_user_payment_coinback_detail_tv_receivable)
    TextView tv_receivable;

    private String transaction_id = "";

    public static ShopPaymentDetailScreen getInstance(String transaction_id) {
        ShopPaymentDetailScreen screen = new ShopPaymentDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_ID, transaction_id);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_payment_detail, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.payment_detail).toUpperCase());
        showReadAllNotification(false);
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            transaction_id = bundle.getString(TRANSACTION_ID);
            getDetail();
        }

    }

    private void getDetail() {
        tv_rate.setVisibility(View.GONE);
        if (!TCUtils.isEmpty(transaction_id))
            getPaymentDetail(transaction_id);

    }

    private void fillPaymentData(PaymentInvoiceDetailModel paymentDetailModel) {
        tv_payment_id.setText(paymentDetailModel.getCustomer());
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(paymentDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));

        tv_customer.setText(paymentDetailModel.getCustomer());
        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getInvoice_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getPaid_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_coinback.setText(String.format("-%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getCoinback_cash_amount()), paymentDetailModel.getCurrency_code()));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentDetailModel.getRemain_cash_amount()), paymentDetailModel.getCurrency_code()));

        tv_receivable.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(paymentDetailModel.getPaid_cash_amount()) - TCUtils.convertToDouble(paymentDetailModel.getCoinback_cash_amount())), paymentDetailModel.getCurrency_code()));
        ll_coinback.setVisibility(TCUtils.convertToDouble(paymentDetailModel.getCoinback_cash_amount()) > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            fillPaymentData((PaymentInvoiceDetailModel) response.getResult());
        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok)).show();
    }
}
