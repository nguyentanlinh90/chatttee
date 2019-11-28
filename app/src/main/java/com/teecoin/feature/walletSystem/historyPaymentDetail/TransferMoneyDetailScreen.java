package com.teecoin.feature.walletSystem.historyPaymentDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.reviewsystem.ReviewFromNotificationModel;
import com.teecoin.model.walletsystem.CoinBackDetailModel;
import com.teecoin.model.walletsystem.PaymentDetailModel;
import com.teecoin.model.walletsystem.RewardResultModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetCoinBackDetailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetRewardDetailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetTransferMoneyDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

import static com.teecoin.utils.TCConstant.THREE_DECIMAL_FORMAT;

public class TransferMoneyDetailScreen extends TCWalletBaseFragment implements APIResponseListener {

    private static final String TRANSACTION_DETAIL_MODEL = "TransactionDetailRealmModel";
    private static final String PUSH_NOTIFICATION_DETAIL_MODEL = "PushNotificationModel";
    private TransactionDetailModel transModel;
    private PushNotificationModel pushNotificationModel;

    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_payment_id;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_history_detail_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_history_detail_invoice_id)
    TextView tv_invoice_id;
    @BindView(R.id.frg_history_detail_tv_amount_title)
    TextView tv_amount_title;
    @BindView(R.id.frg_history_detail_tv_amount)
    TextView tv_amount;
    @BindView(R.id.frg_history_detail_tv_currency_amount)
    TextView tv_currency_amount;
    @BindView(R.id.frg_history_detail_coin_back_tv_convert_payment)
    TextView tv_convert_payment;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;
    @BindView(R.id.frg_history_detail_tv_payment_title)
    TextView tv_payment_title;
    @BindView(R.id.frg_history_detail_tv_payment)
    TextView tv_payment;
    @BindView(R.id.frg_history_detail_tv_currency_payment)
    TextView tv_currency_payment;
    @BindView(R.id.frg_history_detail_ll_fee)
    View ll_fee;
    @BindView(R.id.frg_history_detail_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frg_history_detail_tv_currency_fee)
    TextView tv_currency_fee;
    @BindView(R.id.frg_history_detail_tv_total_tee)
    TextView tv_total_tee;
    @BindView(R.id.frg_history_detail_tv_remain)
    TextView tv_remain;
    @BindView(R.id.frg_history_detail_tv_payment_coin_back)
    TextView tv_coin_back;
    @BindView(R.id.frg_history_detail_tv_reward)
    TextView tv_reward;
    @BindView(R.id.frg_history_detail_rl_reward)
    View rl_reward;
    @BindView(R.id.frg_history_detail_rl_payment_coin_back)
    View rl_payment_coin_back;
    @BindView(R.id.frg_history_detail_rl_remain)
    View rl_remain;
    @BindView(R.id.frg_history_detail_rl_total)
    View rl_total;
    @BindView(R.id.frg_history_detail_rl_value_for_shop)
    View frg_history_detail_rl_value_for_shop;
    @BindView(R.id.frg_history_detail_rl_coin_back)
    View rl_coin_back;
    @BindView(R.id.frg_history_detail_tv_coin_back_amount)
    TextView tv_coin_back_amount;
    @BindView(R.id.frg_history_detail_ll_root_view)
    View ll_root_view;
    @BindView(R.id.frg_history_detail_ll_invoice_and_amount_layout)
    View ll_invoice_and_amount_layout;
    @BindView(R.id.frg_history_detail_ll_transfer_money_note)
    View ll_transfer_money_note;
    @BindView(R.id.frg_history_detail_tv_transfer_money_note)
    TextView tv_transfer_money_note;
    @BindView(R.id.frg_history_detail_rl_transfer_money_amount)
    View rl_transfer_money_amount;
    @BindView(R.id.frg_history_detail_tv_transfer_money_amount)
    TextView tv_transfer_money_amount;

    // add review
    @BindView(R.id.ll_review)
    View ll_review;
    @BindView(R.id.ll_go_review)
    View ll_go_review;
    @BindView(R.id.view_review_tv_get_tee)
    TextView tv_get_tee;

    private ReviewFromNotificationModel reviewModel;

    public static TransferMoneyDetailScreen getInstance(TransactionDetailModel model) {
        TransferMoneyDetailScreen screen = new TransferMoneyDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_DETAIL_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    public static TransferMoneyDetailScreen getInstance(PushNotificationModel model) {
        TransferMoneyDetailScreen screen = new TransferMoneyDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PUSH_NOTIFICATION_DETAIL_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.text_details).toUpperCase());
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
        onClick();

    }

    private void onClick() {
        ll_go_review.setOnClickListener(v -> gotoReviewShop());
        ll_review.setVisibility(View.GONE);
        if (isAppUser()) {
            if (transModel != null) {
                boolean isShowReview = (transModel.getType().equals(EnumMgr.TransactionType.Reward.getValue())
                        || transModel.getType().equals(EnumMgr.TransactionType.Payment.getValue())
                        || transModel.getType().equals(EnumMgr.TransactionType.CoinBack.getValue()))
                        && !transModel.isIs_review_success();
               // ll_review.setVisibility(isShowReview ? View.VISIBLE : View.GONE);
            }

            if (pushNotificationModel != null) {
                boolean isShowReview = pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.Reward.getValue())
                        || pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.Payment.getValue())
                        || pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.CoinBack.getValue());

              //  ll_review.setVisibility(isShowReview ? View.VISIBLE : View.GONE);
            }
        }

        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        if (feeConfigModel != null) {
            tv_get_tee.setText(String.format(TCUtils.getString(R.string.user_review_tee_get), feeConfigModel.getReview_reward_amount()));
        }
    }

    @Override
    public void displayView() {
    }

    private void gotoReviewShop() {
        if (reviewModel != null) {
            addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue(), UserReviewShopScreen.getInstance(transModel));
        } else {
            addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue(), UserReviewShopScreen.getInstance(reviewModel));
        }
    }

    private void getDetail() {
        ll_root_view.setVisibility(View.GONE);
        if (transModel != null) {
            if (transModel.getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
                getPaymentDetail(transModel.getTransaction_id());
            } else if (transModel.getType().equals(EnumMgr.TransactionType.Reward.getValue())) {// Reward
                getRewardDetail(transModel.getTransaction_id());// change model
            } else if (transModel.getType().equals(EnumMgr.TransactionType.CoinBack.getValue())) {// CoinBack
                getCoinBackDetail(transModel.getTransaction_id());
            } else {
                getTransferMoneyDetail(transModel.getTransaction_id());
            }
        } else if (pushNotificationModel != null) {
            if (pushNotificationModel.getData().getType() != null) {
                if (pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
                    getPaymentDetail(pushNotificationModel.getData().getTransaction_id());
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.Reward.getValue())) {
                    getRewardDetail(pushNotificationModel.getData().getTransaction_id());
                } else if (pushNotificationModel.getData().getType().equals(EnumMgr.TransactionType.CoinBack.getValue())) {
                    getCoinBackDetail(pushNotificationModel.getData().getTransaction_id());
                } else {
                    getTransferMoneyDetail(pushNotificationModel.getData().getTransaction_id());
                }
            }
        }
    }

    private void getTransferMoneyDetail(String transaction_id) {
        requestApi(new WalletGetTransferMoneyDetailRequest(transaction_id, this));
    }

    private void getRewardDetail(String transaction_id) {
        requestApi(new WalletGetRewardDetailRequest(transaction_id, this));
    }

    private void getCoinBackDetail(String transaction_id) {
        requestApi(new WalletGetCoinBackDetailRequest(transaction_id, this));
    }


    private void fillPaymentData(PaymentDetailModel paymentDetailModel) {
        /*  HEADER INFO */
        if (transModel == null) {
            tv_payment_id.setText(isAppUser() ? paymentDetailModel.getShopName() :
                    String.format(TCUtils.getString(R.string.string_format_1),
                            TCUtils.getString(R.string.history_detail_payment_id),
                            paymentDetailModel.getTransactionId()));
        } else {
            tv_payment_id.setText(isAppUser() ? transModel.getShop_name() :
                    String.format(TCUtils.getString(R.string.string_format_1),
                            TCUtils.getString(R.string.history_detail_payment_id),
                            paymentDetailModel.getTransactionId()));
        }
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(paymentDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentDetailModel.getRate()),
                paymentDetailModel.getCurrencyCode()));
//        tv_address.setText(String.format(
//                TCUtils.getString(R.string.string_format_1),
//                TCUtils.getString(R.string.coin_back_address1),
//                paymentDetailModel.getDestination()));
        tv_address.setText(String.format(
                TCUtils.getString(R.string.string_format_1),
                TCUtils.getString(R.string.coin_back_address1),
                TCUtils.setTextAddress(paymentDetailModel.getSource(), paymentDetailModel.getDestination())));
        tv_address.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        /*  HEADER INFO */

        /*  INVOICE_ID, INVOICE_AMOUNT,PAYMENT & FEE */
        tv_invoice_id.setText(paymentDetailModel.getInvoice());

        tv_amount_title.setText(TCUtils.getString(R.string.general_amount));
        tv_amount.setText(TCUtils.formatMoney(TCConstant.THREE_DECIMAL_FORMAT, paymentDetailModel.getInvoiceAmount()));
        tv_currency_amount.setText(paymentDetailModel.getCurrencyCode());

        tv_payment_title.setText(TCUtils.getString(R.string.input_payment_payment));
        tv_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, paymentDetailModel.getAmount()));
        tv_currency_payment.setText(TCUtils.getString(R.string.tee_coin_symbol));

        if (isAppUser()) {
            ll_fee.setVisibility(View.VISIBLE);
            tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentDetailModel.getFeeAmount()));
            tv_currency_fee.setText(TCUtils.getString(R.string.tee_coin_symbol));
        } else {
            ll_fee.setVisibility(View.GONE);
        }
        /*  INVOICE_ID, INVOICE_AMOUNT,PAYMENT & FEE */

        /*  TOTAL PAYMENT & REMAIN */
        // tv_total_tee.setText(String.format(TCUtils.getString(isAppUser() ? R.string.string_format_2 : R.string.string_format_3),
        tv_total_tee.setText(String.format(TCUtils.getString(R.string.string_format_1),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentDetailModel.getTotalAmount(isAppUser())), TCUtils.getString(R.string.tee_coin_symbol)));

        tv_remain.setText(String.format(TCUtils.getString(R.string.string_format_1),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, paymentDetailModel.getRemainAmount()),
                paymentDetailModel.getCurrencyCode()));

        if (paymentDetailModel.getCoinbackSuccess()) {
            rl_payment_coin_back.setVisibility(View.VISIBLE);
            tv_coin_back.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentDetailModel.getCoinbackAmount()),
                    TCUtils.getString(R.string.tee_coin_symbol)));
            double coinback = TCUtils.convertToDouble(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, paymentDetailModel.getCoinbackAmount()));
            if (coinback == 0) {
                rl_payment_coin_back.setVisibility(View.GONE);
            }
        } else {
            rl_payment_coin_back.setVisibility(View.GONE);
        }
        /*  TOTAL PAYMENT & REMAIN */

        rl_reward.setVisibility(View.GONE);
        rl_coin_back.setVisibility(View.GONE);
        ll_transfer_money_note.setVisibility(View.GONE);
        rl_transfer_money_amount.setVisibility(View.GONE);
        ll_root_view.setVisibility(View.VISIBLE);

        reviewModel = new ReviewFromNotificationModel(paymentDetailModel.getTransactionId(), paymentDetailModel.getDestination());
    }

    private void fillCoinBackData(CoinBackDetailModel coinBackDetailModel) {
        /*  HEADER INFO */
        if (transModel != null) {
            tv_payment_id.setText(isAppUser() ? transModel.getShop_name() :
                    String.format(TCUtils.getString(R.string.string_format_1),
                            TCUtils.getString(R.string.history_detail_coin_back_id),
                            coinBackDetailModel.getTransactionId()));
        } else {
            tv_payment_id.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.getString(R.string.history_detail_coin_back_id),
                    coinBackDetailModel.getTransactionId()));
        }

        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(coinBackDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(getString(R.string.history_detail_coin_back_rate), coinBackDetailModel.getReturnRate()));
        tv_address.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        tv_address.setText(String.format(TCUtils.getString(R.string.string_format_1), TCUtils.getString(R.string.coin_back_address1),
                isAppUser() ? coinBackDetailModel.getSource() : coinBackDetailModel.getDestination()));
        /*  HEADER INFO */
        // GAOE2IKG72QCGZUKVES2PHLYB4PGLXCWL4YMNCOBOFTX63JBUESNN7JR

        /*  INVOICE_ID, PAYMENT & FEE */
        tv_invoice_id.setText(coinBackDetailModel.getInvoice());
        tv_amount_title.setText(TCUtils.getString(R.string.text_remaining_payment));

        double convertToTEC = TCUtils.convertToDouble(coinBackDetailModel.getRemainAmount()) / TCUtils.convertToDouble(coinBackDetailModel.getRate());
        tv_amount.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, coinBackDetailModel.getRemainAmount()));
        tv_convert_payment.setVisibility(View.VISIBLE);
        tv_convert_payment.setText(String.format(TCUtils.getString(R.string.string_format_5),
                TCUtils.formatMoney(THREE_DECIMAL_FORMAT, TCUtils.roundFloor(convertToTEC, TCConstant.ROUND_THREE_DECIMAL))));

        tv_currency_amount.setText(coinBackDetailModel.getCurrencyCode());

        if (isAppUser()) {
            frg_history_detail_rl_value_for_shop.setVisibility(View.GONE);
            rl_total.setVisibility(View.GONE);
            rl_coin_back.setVisibility(View.VISIBLE);
            tv_coin_back_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, coinBackDetailModel.getTotalCoinBack(isAppUser())));
        } else {
            rl_coin_back.setVisibility(View.GONE);
            tv_payment_title.setText(TCUtils.getString(R.string.coin_back_coinback));
            tv_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, coinBackDetailModel.getAmount()));
            tv_currency_payment.setText(TCUtils.getString(R.string.tee_coin_symbol));
            tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, coinBackDetailModel.getFeeAmount()));
            tv_currency_fee.setText(TCUtils.getString(R.string.tee_coin_symbol));
            tv_total_tee.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, coinBackDetailModel.getTotalCoinBack(isAppUser())),
                    TCUtils.getString(R.string.tee_coin_symbol)));

        }
        rl_remain.setVisibility(View.GONE);
        rl_payment_coin_back.setVisibility(View.GONE);
        rl_reward.setVisibility(View.GONE);
        ll_transfer_money_note.setVisibility(View.GONE);
        rl_transfer_money_amount.setVisibility(View.GONE);
        ll_root_view.setVisibility(View.VISIBLE);

        reviewModel = new ReviewFromNotificationModel(coinBackDetailModel.getTransactionId(), coinBackDetailModel.getSource());
    }

    private void fillRewardData(RewardResultModel rewardDetailModel) {
        /*  HEADER INFO */
        if (transModel == null) {
            tv_payment_id.setText(isAppUser() ? rewardDetailModel.getShop_name() :
                    String.format(TCUtils.getString(R.string.string_format_1),
                            TCUtils.getString(R.string.history_detail_reward_id),
                            rewardDetailModel.getTransactionId()));
        } else {
            tv_payment_id.setText(isAppUser() ? transModel.getShop_name() :
                    String.format(TCUtils.getString(R.string.string_format_1),
                            TCUtils.getString(R.string.history_detail_reward_id),
                            rewardDetailModel.getTransactionId()));
        }

        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(rewardDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(getString(R.string.history_detail_reward_rate), rewardDetailModel.getReturn_rate()));
//        tv_address.setText(String.format(
//                TCUtils.getString(R.string.string_format_1),
//                TCUtils.getString(R.string.coin_back_address1),
//                rewardDetailModel.getSource()));
        tv_address.setText(String.format(
                TCUtils.getString(R.string.string_format_1),
                TCUtils.getString(R.string.coin_back_address1),
                TCUtils.setTextAddress(rewardDetailModel.getSource(), rewardDetailModel.getDestination())));
        tv_address.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        /*  HEADER INFO */

        tv_invoice_id.setText(rewardDetailModel.getInvoice());
        tv_amount.setText(TCUtils.formatMoney(TCConstant.THREE_DECIMAL_FORMAT, rewardDetailModel.getInvoiceAmount()));
        tv_currency_amount.setText(rewardDetailModel.getCurrencyCode());

        if (isAppUser()) {
            frg_history_detail_rl_value_for_shop.setVisibility(View.GONE);
            rl_total.setVisibility(View.GONE);
            rl_reward.setVisibility(View.VISIBLE);
            tv_reward.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, rewardDetailModel.getTotalReward(isAppUser())));
        } else {
            rl_total.setVisibility(View.VISIBLE);
            rl_reward.setVisibility(View.GONE);
            tv_payment_title.setText(TCUtils.getString(R.string.text_reward));
            tv_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, rewardDetailModel.getAmount()));
            tv_currency_payment.setText(TCUtils.getString(R.string.tee_coin_symbol));
            tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, rewardDetailModel.getFeeAmount()));
            tv_currency_fee.setText(TCUtils.getString(R.string.tee_coin_symbol));
            tv_total_tee.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, rewardDetailModel.getTotalReward(isAppUser())),
                    TCUtils.getString(R.string.tee_coin_symbol)));
        }
        rl_coin_back.setVisibility(View.GONE);
        rl_payment_coin_back.setVisibility(View.GONE);
        rl_remain.setVisibility(View.GONE);
        ll_transfer_money_note.setVisibility(View.GONE);
        rl_transfer_money_amount.setVisibility(View.GONE);
        ll_root_view.setVisibility(View.VISIBLE);

        reviewModel = new ReviewFromNotificationModel(rewardDetailModel.getTransactionId(), rewardDetailModel.getSource());// 1

    }

    private void fillTransferMoneyData(TransferMoneyModel transferMoneyModel) {
        /*  HEADER INFO */
        tv_payment_id.setText(
                TCUtils.isYourselfASenderMoney(transferMoneyModel.getSource()) ?
                        transferMoneyModel.getReceiver() : transferMoneyModel.getSender());
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(transferMoneyModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getExchangeRate()),
                transferMoneyModel.getCurrency_code()));
//        tv_address.setText(String.format(
//                TCUtils.getString(R.string.string_format_1),
//                TCUtils.getString(R.string.coin_back_address1),
//                transferMoneyModel.getDestination()));
        tv_address.setText(String.format(
                TCUtils.getString(R.string.string_format_1),
                TCUtils.getString(R.string.coin_back_address1),
                TCUtils.setTextAddress(transferMoneyModel.getSource(), transferMoneyModel.getDestination())));
        /*  HEADER INFO */

        /*  BODY  */
        ll_invoice_and_amount_layout.setVisibility(View.GONE);
        ll_transfer_money_note.setVisibility(View.VISIBLE);
        tv_transfer_money_note.setText(transferMoneyModel.getNote());
        if (TCUtils.isYourselfASenderMoney(transferMoneyModel.getSource())) {
            frg_history_detail_rl_value_for_shop.setVisibility(View.VISIBLE);
            rl_transfer_money_amount.setVisibility(View.GONE);
            tv_payment_title.setText(TCUtils.getString(R.string.general_amount));
            tv_payment.setText(TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, transferMoneyModel.getAmount()));
            tv_currency_payment.setText(TCUtils.getString(R.string.tee_coin_symbol));
            tv_fee.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getTeeCoinFee()));
            tv_currency_fee.setText(TCUtils.getString(R.string.tee_coin_symbol));

            /*  TOTAL PAYMENT */
            rl_total.setVisibility(View.VISIBLE);
            transferMoneyModel.calculateTotalTeeCoin();
            tv_total_tee.setText(String.format(TCUtils.getString(R.string.string_format_1),
                    TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getTotalTeeCoin()),
                    TCUtils.getString(R.string.tee_coin_symbol)));
            /*  TOTAL PAYMENT */
        } else {
            frg_history_detail_rl_value_for_shop.setVisibility(View.GONE);
            rl_total.setVisibility(View.GONE);
            rl_transfer_money_amount.setVisibility(View.VISIBLE);
            tv_transfer_money_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transferMoneyModel.getAmount()));
        }
        /*  BODY  */


        rl_reward.setVisibility(View.GONE);
        rl_coin_back.setVisibility(View.GONE);
        rl_remain.setVisibility(View.GONE);
        rl_payment_coin_back.setVisibility(View.GONE);
        ll_root_view.setVisibility(View.VISIBLE);

        reviewModel = new ReviewFromNotificationModel(transferMoneyModel.getTransactionId(), transferMoneyModel.getDestination());
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            fillPaymentData((PaymentDetailModel) response.getResult());
        } else if (requestTarget == WalletRequestTarget.GET_REWARD_DETAIL) {
            fillRewardData((RewardResultModel) response.getResult());
        } else if (requestTarget == WalletRequestTarget.GET_COIN_BACK_DETAIL) {
            fillCoinBackData((CoinBackDetailModel) response.getResult());
        } else if (requestTarget == WalletRequestTarget.GET_TRANSFER_MONEY_DETAIL) {
            fillTransferMoneyData((TransferMoneyModel) response.getResult());
        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            Toast.makeText(getActiveActivity(), errorModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue() && finishedResultCode == RESULT_OK) {
            ll_review.setVisibility(View.GONE);
            ((TCMainActivity) getActiveActivity()).hideMenuNextBottom();
        }
    }

}
