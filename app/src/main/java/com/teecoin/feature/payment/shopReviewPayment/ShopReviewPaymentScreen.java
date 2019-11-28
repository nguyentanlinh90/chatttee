package com.teecoin.feature.payment.shopReviewPayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.payment.shopInputPayment.ShopInputPaymentScreen;
import com.teecoin.feature.payment.shopPaymentQRCode.ShopPaymentQRCodeScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.walletsystem.InvoiceModel;
import com.teecoin.model.walletsystem.PaymentInfoModel;
import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopPaymentInvoiceRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ShopReviewPaymentScreen extends TCWalletBaseFragment {

    private static final String PAYMENT_INFO_MODEL = "PaymentInfoModel";

    @BindView(R.id.frg_shop_input_payment_iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.frg_shop_input_payment_tv_name)
    TextView tv_name;
    @BindView(R.id.frg_shop_input_payment_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_shop_review_payment_tv_total_bill)
    TextView tv_total_bill;
    @BindView(R.id.frg_shop_review_payment_tv_amount_wallet)
    TextView tv_amount_wallet;
    @BindView(R.id.frg_shop_review_payment_tv_amount_cash)
    TextView tv_amount_cash;
    @BindView(R.id.frg_shop_review_payment_tv_converd_to_tec)
    TextView tv_converd_to_tec;

    @BindView(R.id.view_cancel_next_bottom_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.view_cancel_next_bottom_tv_next)
    TextView tv_get_code;
    private Vendor vendor;
    private PaymentInfoModel paymentInfoModel;

    public static ShopReviewPaymentScreen getInstance(PaymentInfoModel paymentInfoModel) {
        ShopReviewPaymentScreen screen = new ShopReviewPaymentScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_INFO_MODEL, paymentInfoModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_review_payment, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.payment_review));
        hideFooter();
        showTabMenuBottom();
        showButtonBackToolbar();
        hideViewQrCode();
        hideButtonAddCoupon();

    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            paymentInfoModel = (PaymentInfoModel) bundle.getSerializable(PAYMENT_INFO_MODEL);
            fillData();
        }
        registerSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
        tv_cancel.setText(TCUtils.getString(R.string.text_cancel));
        tv_get_code.setText(TCUtils.getString(R.string.get_code));
        setUIProfile();

    }

    private void fillData() {
        if (paymentInfoModel == null)
            return;
        tv_total_bill.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getTotal()), TCUtils.getString(R.string.text_sgd)));
        tv_amount_wallet.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getAmountWallet()), TCUtils.getString(R.string.text_sgd)));
        tv_converd_to_tec.setText(String.format("%s %s",
                TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getConvertToTec()), TCUtils.getString(R.string.tee_coin_symbol)));
        tv_amount_cash.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, paymentInfoModel.getInputAmountCash()), TCUtils.getString(R.string.text_sgd)));
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.view_cancel_next_bottom_tv_cancel, R.id.view_cancel_next_bottom_tv_next);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_cancel_next_bottom_tv_next:
                getInvoice();
                break;
            case R.id.view_cancel_next_bottom_tv_cancel:
                replaceFragment(ShopInputPaymentScreen.getInstance(), true);
                break;

        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue() && finishedResultCode == RESULT_OK) {
            handleBackPressed();
        }
    }

    private void setUIProfile() {
        vendor = RealmController.getInstance().getData(Vendor.class);
        if (vendor != null) {
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(vendor.getLogo()) ? TCUtils.getDrawable(R.drawable.ic_shop_icon)
                    : vendor.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_avatar);
            tv_name.setText(vendor.getName());
            tv_address.setText(!TCUtils.isEmpty(vendor.getAddress()) ? vendor.getAddress() : "");
        }
    }

    private void getInvoice() {
        PaymentInvoiceSubmitModel paymentInvoiceSubmitModel = new PaymentInvoiceSubmitModel(paymentInfoModel);
        requestApi(new ShopPaymentInvoiceRequest(paymentInvoiceSubmitModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                InvoiceModel invoiceModel = (InvoiceModel) response.getResult();
                //  addFragment(ShopPaymentQRCodeScreen.getInstance(invoiceModel, paymentInvoiceSubmitModel));
                addFragmentForResult(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue(), ShopPaymentQRCodeScreen.getInstance(invoiceModel, paymentInvoiceSubmitModel));

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok)).show();

            }
        }));

    }
}
