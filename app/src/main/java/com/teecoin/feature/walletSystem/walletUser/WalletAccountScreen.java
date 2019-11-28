package com.teecoin.feature.walletSystem.walletUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.BuildConfig;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCheckInResultScreen;
import com.teecoin.feature.general.popup.MessageBaseScreen;
import com.teecoin.feature.payment.userReviewPayment.UserReviewPaymentScreen;
import com.teecoin.feature.walletSystem.coinback.CoinBackScreen;
import com.teecoin.feature.walletSystem.history.HistoryScreen;
import com.teecoin.feature.walletSystem.myqrcode.MyQRCodeScreen;
import com.teecoin.feature.walletSystem.scanqrCode.InputWalletScreen;
import com.teecoin.feature.walletSystem.sendmoney.SendMoneyScreen;
import com.teecoin.feature.walletSystem.topup.TopUpScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.QrCheckInModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.walletsystem.ConvertCurrenciesModel;
import com.teecoin.model.walletsystem.InvoiceModel;
import com.teecoin.model.walletsystem.PaymentDetailModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.model.walletsystem.PaymentInvoiceModel;
import com.teecoin.model.walletsystem.QRCodeInfoModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.TransactionHistoryListResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNotificationRequest;
import com.teecoin.myapi.apirequest.walletsystem.ConvertCurrenciesRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetQRCodeInfoRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetTransactionHistoryListRequest;
import com.teecoin.myapi.apirequest.walletsystem.user.UserGetPaymentInvoiceDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_NAME;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_CURRENCY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_SELECTED;

public class WalletAccountScreen extends TCWalletBaseFragment implements RecycleListener<TransactionDetailModel>, StellarResponseListener, APIResponseListener {
    private static final String ACTION_TYPE_WALLET = "ACTION_TYPE_WALLET";

    @BindView(R.id.frg_wallet_account_refresh_view)
    TCSwipeRefreshLayout refreshView;

    @BindView(R.id.frg_wallet_account_nsv_container)
    NestedScrollView nsv_container;
    @BindView(R.id.frg_wallet_account_tv_coin_number)
    TextView tv_coin_number;
    @BindView(R.id.frg_wallet_account_tv_market)
    TextView tv_market;
    @BindView(R.id.frg_wallet_account_v_market_value)
    View view_market_value;
    @BindView(R.id.frg_wallet_account_tv_market_value)
    TextView tv_market_value;
    @BindView(R.id.frg_wallet_account_tv_tec_name)
    TextView tv_tec_name;
    @BindView(R.id.view_latest_transaction_rcv_transaction_list)
    TCRecyclerView rcv_transaction_list;
    @BindView(R.id.frg_wallet_account_rl_balance_background)
    View rl_balance;
    @BindView(R.id.frg_wallet_account_iv_billionaire_crown)
    ImageView iv_billionaire_diamond;

    @BindView(R.id.frg_wallet_account_rl_account_balance)
    RelativeLayout rl_account_balance;

    @BindView(R.id.frg_wallet_account_tv_balance_text)
    TextView tv_balance_text;

    @BindView(R.id.frg_home_wallet_ll_top_up)
    View vTopUp;
    @BindView(R.id.frg_home_wallet_ll_send)
    View vSend;
    @BindView(R.id.frg_home_wallet_ll_receive)
    View vReceive;
    @BindView(R.id.frg_home_wallet_ll_pay)
    View vPay;

    private TransactionDetailListAdapter adapterUser;


    private AccountModel accountModel;

    private CountryCodeModel currentCountryCode;

    public static WalletAccountScreen getInstance() {
        return new WalletAccountScreen();
    }

    public static WalletAccountScreen getInstance(String actionType) {
        WalletAccountScreen screen = new WalletAccountScreen();
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_TYPE_WALLET, actionType);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_account, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.my_wallet));
        showButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
        getBalance();
//        getTransactionData();
        requestApi(new WalletGetTransactionHistoryListRequest(1, this));
        checkNewNotification();
        //startStreamData();
        stellarGetBalance(accountModel.getPublic_key(), false, this);

    }

    @Override
    public void onBindView() {

        String topUpCountry = TCSharePreferenceManager.getInstance().getString(DataKey.TopUpCountry);

        if (isAppUser() && topUpCountry.contains(((TCMainActivity) getActiveActivity()).getCountryCodeModel().getCountry_code().toLowerCase())) {
            vTopUp.setVisibility(View.VISIBLE);
        }
        vPay.setVisibility(!isAppUser() ? View.GONE : View.VISIBLE);

        currentCountryCode = ((TCMainActivity) getActiveActivity()).getCountryCodeModel();
        if (currentCountryCode == null) {
            currentCountryCode = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, DEFAULT_SELECTED);
        }
        init();

        Bundle bundle = getArguments();
        if (null != bundle) {
            String typeAction = bundle.getString(ACTION_TYPE_WALLET);
            if (TCConstant.INPUT_WALLET.equals(typeAction)) {
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_INVOICE_FOR_SEND_MONEY.getValue());
            }else if (TCConstant.USER_PAYMENT_REVIEW.equals(typeAction)){
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue());
            }
        }
    }

    private void init() {
        rcv_transaction_list.setFocusable(false);
        nsv_container.requestFocus();
        initClickEvent();
        setupRecycler();
        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        //stellarGetBalance(accountModel.getPublic_key(), true, this);
        checkDeviceTokenExists();
        refreshData();
    }

    private void initClickEvent() {
        registerSingleClick(R.id.frg_home_wallet_ll_send, R.id.frg_home_wallet_ll_receive,
                R.id.frg_home_wallet_ll_top_up, R.id.view_latest_transaction_ll_view_all_transaction, R.id.frg_home_wallet_ll_pay);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {
            case R.id.frg_home_wallet_ll_send:
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_INVOICE_FOR_SEND_MONEY.getValue());
                break;

            case R.id.frg_home_wallet_ll_receive:
                addFragment(MyQRCodeScreen.getInstance());
                break;

            case R.id.frg_home_wallet_ll_top_up:
                addFragment(TopUpScreen.getInstance());
                break;

            case R.id.view_latest_transaction_ll_view_all_transaction:
                gotoHistoryList();
                break;
            case R.id.frg_home_wallet_ll_pay:
                gotoScanQRCode(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue());
                break;
        }
    }

    private void gotoHistoryList() {
        addFragment(HistoryScreen.getInstance());
    }

    @Override
    public void onItemClick(View view, TransactionDetailModel item, int position, EnumMgr.ClickType clickType) {
        if (!adapterUser.isClickedItem()) {
            adapterUser.setClickedItem(true);
            itemClickTransaction(item, clickType);
        }

    }

    @Override
    public void onStellarSuccess(Object object) {
        if (object instanceof String) {
            String teeCoinAmount = (String) object;
            accountModel.setBalance(TCUtils.isEmpty(teeCoinAmount) ? "0" : teeCoinAmount);
            RealmController.getInstance().updateBalanceAccount(accountModel);
            tv_coin_number.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, accountModel.getBalance()));
            updateBalanceUI(accountModel.getBalance());
            ((TCMainActivity) getActiveActivity()).updateGoogleAnalyticUserProperty(accountModel);
        }
        refreshView.setRefreshing(false);
    }

    private void updateBalanceUI(String balance) {
        if (TCUtils.convertToDouble(balance) >= TCConstant.MILLIONAIRE_AMOUNT) {
            if (getTopFragment() instanceof WalletAccountScreen) {
                updateTitleHeader(TCUtils.getString(R.string.million_club));
            }
            rl_balance.setBackground(TCUtils.getDrawable(R.drawable.bg_million_club));
            iv_billionaire_diamond.setVisibility(View.VISIBLE);
            tv_balance_text.setVisibility(View.GONE);
            tv_coin_number.setTextColor(TCUtils.getColor(R.color.c_ffffff));
            tv_tec_name.setTextColor(TCUtils.getColor(R.color.c_ffffff));
            tv_market.setTextColor(TCUtils.getColor(R.color.c_ffffff));
            tv_market_value.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        } else {
            iv_billionaire_diamond.setVisibility(View.GONE);
            tv_balance_text.setVisibility(View.VISIBLE);
            tv_coin_number.setTextColor(TCUtils.getColor(R.color.c_baa35c));
            tv_tec_name.setTextColor(TCUtils.getColor(R.color.c_000000));
            tv_market.setTextColor(TCUtils.getColor(R.color.c_848484));
            tv_market_value.setTextColor(TCUtils.getColor(R.color.c_000000));
        }
        if (null != currentCountryCode)
            setMarketValue(balance);
    }

    @Override
    public void onStellarFail(Throwable o) {
        tv_coin_number.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, 0));
        refreshView.setRefreshing(false);
    }

    public void getTransactionData() {
        ArrayList<TransactionDetailModel> transactionList =
                BuildConfig.IS_CHATEE_APP ?
                        RealmController.getInstance().getTransactionList(false) :
                        RealmController.getInstance().getTransactionListWithoutTipAndReview(false);
        if (transactionList != null && transactionList.size() > 0) {
            adapterUser = new TransactionDetailListAdapter(LayoutInflater.from(getActiveActivity()), transactionList, this);
            rcv_transaction_list.setAdapter(adapterUser);
            TCSharePreferenceManager.getInstance().setString(DataKey.PagingToken, transactionList.get(0).getPaging_token());
        } else {
            requestApi(new WalletGetTransactionHistoryListRequest(1, this));
        }
    }

    private void setupRecycler() {
        rcv_transaction_list.setHasFixedSize(true);
        rcv_transaction_list.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActiveActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_transaction_list.setLayoutManager(layoutManager);
    }

    private void checkNewNotification() {
        requestApi(new ReviewUserGetNotificationRequest(1, this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                TCLog.d("Cancelled from fragment");
            } else {
                // TCLog.e("qr code resultCode "+resultCode);
                try {
                    PaymentInvoiceModel paymentInvoiceModel = new PaymentInvoiceModel();
                    QrCheckInModel qrCheckInModel = new QrCheckInModel();
                    InvoiceModel invoiceModel = new InvoiceModel();
                    if (qrCheckInModel.validate(result.getContents())) {
                        if (!isAppUser()) {
                            new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message), TCUtils.getString(R.string.text_warning), TCUtils.getString(R.string.account_does_not_exists), TCUtils.getString(R.string.text_ok)).show();
                            return;
                        }
                        qrCheckInModel = TCUtils.convertToModel(result.getContents(), QrCheckInModel.class);
                        VendorCodeCheckInModel vendorCodeCheckInModel = new VendorCodeCheckInModel(qrCheckInModel.getVendor_code(),
                                String.valueOf(getLocation().latitude), String.valueOf(getLocation().longitude));
                        vendorCodeCheckInModel.setFromHomeCheckIn(true);
                        addFragment(UserCheckInResultScreen.getInstance(vendorCodeCheckInModel, true));
                    } else if (invoiceModel.validate(result.getContents())) {
                        invoiceModel = TCUtils.convertToModel(result.getContents(), InvoiceModel.class);
                        if (isAppUser()) {
                            checkPaymentSattus(invoiceModel);
                            //addFragment(UserReviewPaymentScreen.getInstance(invoiceModel));
                        } else {
                            new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message), TCUtils.getString(R.string.text_alert), TCUtils.getString(R.string.account_does_not_exists), TCUtils.getString(R.string.text_ok)).show();
                        }

                    } else {
                        requestApi(new WalletGetQRCodeInfoRequest(result.getContents(), this));
                    }
                } catch (Exception e) {
                    ScanQRCodeInvalid();
                    //showScanFailDialog(requestCode);
                }
            }
        }

        //go to input wallet screen
        if (data != null && TCConstant.KEY_PUT_TO_IMPORT_WALLET.equals(data.getStringExtra(TCConstant.KEY_PUT_TO_IMPORT_WALLET))) {
            addFragment(InputWalletScreen.getInstance());
        }
    }

    public void getBalance() {
        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        tv_coin_number.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, accountModel.getBalance() != null ? accountModel.getBalance() : "0"));
        updateBalanceUI(accountModel.getBalance());
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_USER_TRANSACTION_HISTORY_LIST
                || requestTarget == WalletRequestTarget.GET_SHOP_TRANSACTION_HISTORY_LIST) {

            TransactionHistoryListResponseModel responseModel = (TransactionHistoryListResponseModel) response.getResult();
            TCSharePreferenceManager.getInstance().setInt(DataKey.TransactionHistoryPageIndex, responseModel.getNextPageIndex());
            if (responseModel.getTransactionDetailModelList() != null && responseModel.getTransactionDetailModelList().size() > 0) {
                for (TransactionDetailModel model : responseModel.getTransactionDetailModelList()) {
                    RealmController.getInstance().insertData(model, TransactionDetailModel.PRIMARY_KEY, model.getTransaction_hash());
                }
                getTransactionData();
            }
        } else if (requestTarget == ReviewRequestTarget.USER_GET_NOTIFY || requestTarget == ReviewRequestTarget.SHOP_GET_NOTIFY) {
            String unread = ((BaseResultsResponseModel) response.getResult()).getUnread();
//            iv_notification.setImageDrawable(TCUtils.getDrawable(
//                    !TCUtils.isEmpty(unread) && !unread.equals("0") ?
//                            R.drawable.ic_notification_toolbar_dot_red : R.drawable.ic_notification_toolbar));
        } else if (requestTarget == WalletRequestTarget.GET_QR_CODE_INFO) {
            QRCodeInfoModel qrCodeInfoModel = (QRCodeInfoModel) response.getResult();
            addFragment(SendMoneyScreen.getInstance(true, qrCodeInfoModel));
        } else if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            PaymentDetailModel detailModel = (PaymentDetailModel) response.getResult();
            if (!isTransferCoinToYourself(detailModel.getSource())) {
                addFragment(CoinBackScreen.getInstance((PaymentDetailModel) response.getResult()));
            }
        } else if (requestTarget == WalletRequestTarget.USER_GET_PAYMENT_INVOICE_DETAIL) {
            PaymentInvoiceDetailModel paymentInvoiceDetailModel = (PaymentInvoiceDetailModel) response.getResult();
            if (paymentInvoiceDetailModel != null && paymentInvoiceDetailModel.getStatus().equals(TCConstant.TAG_PAYMENT_UNKNOWN)) {
                addFragmentForResult(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue(), UserReviewPaymentScreen.getInstance(paymentInvoiceDetailModel));
            } else {
                ScanQRCodeInvalid();
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_QR_CODE_INFO) {
            ScanQRCodeInvalid();
            //  showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
        } else if (requestTarget == WalletRequestTarget.USER_GET_PAYMENT_INVOICE_DETAIL) {
            new MessageBaseScreen(getActiveActivity(), errorModel.getErrorMessage()).show();
        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue() && finishedResultCode == RESULT_OK) {
            gotoScanQRCode(EnumMgr.RequestCode.SCAN_INVOICE_FOR_PAYMENT.getValue());
        }
    }

    private void setMarketValue(String balance) {
        requestApi(new ConvertCurrenciesRequest(TCUtils.isProductionMode() ? EnumMgr.Currentcy.TEC.getValue()
                : EnumMgr.Currentcy.TEECOIN.getValue(), currentCountryCode.getCurrency_code(), balance, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ConvertCurrenciesModel convertCurrenciesModel = (ConvertCurrenciesModel) response.getResult();
                if (null != convertCurrenciesModel) {
                    view_market_value.setVisibility(View.VISIBLE);
                    String amount = TCUtils.getString(R.string.integrating_with_exchanger);
                    if (!TCUtils.isEmpty(convertCurrenciesModel.getConvertedAmount())) {
                        amount = String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT,
                                convertCurrenciesModel.getConvertedAmount()), convertCurrenciesModel.getTo());
                    }
                    tv_market_value.setText(amount);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                view_market_value.setVisibility(View.INVISIBLE);
            }
        }));
//        if (accountModel == null) {
//            accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
//        }
//        return TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT,
//                TCUtils.convertToDouble(balance) * accountModel.getRate());
    }

    private void refreshData() {
        refreshView.setOnRefreshListener(() -> stellarGetBalance(accountModel.getPublic_key(), true, this));
    }

    private void checkPaymentSattus(InvoiceModel invoiceModel) {
        if (invoiceModel == null)
            return;
        requestApi(new UserGetPaymentInvoiceDetailRequest(invoiceModel.getInvoice(), this));
    }
}


