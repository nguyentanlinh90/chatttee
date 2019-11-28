package com.teecoin.stellar;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCApplication;
import com.teecoin.feature.reviewSystem.tip.TipModel;
import com.teecoin.javastellarsdk.stellar.StellarUtils;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseRequestModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.reviewsystem.ReviewShopModel;
import com.teecoin.model.walletsystem.CoinBackModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.model.walletsystem.PaymentInvoiceModel;
import com.teecoin.model.walletsystem.PaymentModel;
import com.teecoin.model.walletsystem.RewardModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.model.walletsystem.UserCreatePaymentRequest;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserPurchaseCatalogueRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopCreateTipRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserSubmitReviewShopRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCreateCoinBackRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCreatePaymentRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletCreateRewardRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletTransferMoneyRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.retrofit.StatusCode;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import org.stellar.sdk.Memo;
import org.stellar.sdk.Transaction;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StellarBusinessProcess {

    private static StellarBusinessProcess instance;
    private TCMainActivity mainActivity;

    private StellarBusinessProcess(TCMainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public synchronized static StellarBusinessProcess getInstance() {
        if (instance == null)
            instance = new StellarBusinessProcess((TCMainActivity) TCApplication.getActiveActivity());
        return instance;
    }

    //
//stellarGetTransaction(
//        SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
//            tipModel.getDestination(),
//            TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, tipModel.getAmount(), Locale.US),
//            TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, tipModel.getFee_amount(), Locale.US),
//    memo,
    private void stellarGetTransaction(
            String sourceSecretSeed,
            String destinationPublicKey,
            String teeCoinAmount,
            String teeCoinFee,
            Memo memo,
            boolean isLoading,
            StellarResponseListener listener) {
        if (!TCUtils.isNetworkConnectionAvailable()) {
            mainActivity.hideLoadingDialog();
            mainActivity.showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
            return;
        }
        if (isLoading) {
            mainActivity.showLoadingDialog();
        }
        StellarObserver.getInstance().getTransaction(
                sourceSecretSeed,
                destinationPublicKey,
                teeCoinAmount,
                teeCoinFee,
                memo)
                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StellarSubscriber<>(listener, isLoading));
    }

    // todo new
    private void stellarGetTransaction(
            String sourceSecretSeed,
            String paidAmount,
            String feeAmount,
            String basicFeeAmount,
            String paidAmountWallet,
            String feeAmountWallet,
            String basicFeeAmountWallet,
            boolean isLoading,
            StellarResponseListener listener) {
        if (!TCUtils.isNetworkConnectionAvailable()) {
            mainActivity.hideLoadingDialog();
            mainActivity.showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
            return;
        }
        if (isLoading) {
            mainActivity.showLoadingDialog();
        }
        StellarObserver.getInstance().getTransaction(
                sourceSecretSeed,
                paidAmount,
                feeAmount,
                basicFeeAmount,
                paidAmountWallet,
                feeAmountWallet,
                basicFeeAmountWallet)
                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StellarSubscriber<>(listener, isLoading));
    }

    public void startReward(RewardModel rewardModel, APIResponseListener listener) {
        AccountModel shopModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        Memo memo = Memo.text(String.format("%s,%s,%s",
                EnumMgr.TransactionType.Reward.getValue(),
                rewardModel.getInvoice(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getAmount(), Locale.US)));

        stellarGetTransaction(
                SecretKeyEncryption.decrypt(shopModel.getSecret_key()),
                rewardModel.getDestination(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getAmount(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getFee_amount(), Locale.US),
                memo,
                true,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o instanceof Transaction) {
                            submitReward(rewardModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitReward(RewardModel rewardModel, Transaction transaction, APIResponseListener listener) {
        rewardModel.setXdr(transaction.toEnvelopeXdrBase64());
        rewardModel.setTransactionHash(StellarUtils.convertToHexString(transaction.hash()));
        rewardModel.setAmount(
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getAmount(), Locale.US));
        rewardModel.setFee_amount(
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getFee_amount(), Locale.US));
        rewardModel.setInvoice_amount(
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, rewardModel.getInvoice_amount(), Locale.US));
        mainActivity.requestApi(new WalletCreateRewardRequest(rewardModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == WalletRequestTarget.CREATE_REWARD) {
                    listener.onSuccess(response, requestTarget);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                if (requestTarget == WalletRequestTarget.CREATE_REWARD) {
                    listener.onFail(errorModel, statusCode, requestTarget);
                }
            }
        }));
    }


    public void startPayment(PaymentInvoiceModel paymentInvoiceModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        Memo memo = Memo.text(String.format("%s,%s,%s",
                EnumMgr.TransactionType.Payment.getValue(),
                paymentInvoiceModel.getInvoice(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getPayment(), Locale.US)));
        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                paymentInvoiceModel.getShop().getPublic_key(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getPayment(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getTeeCoinFee(), Locale.US),
                memo,
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        TCLog.d("hung onStellarSuccess");
                        if (o instanceof Transaction) {
                            submitPayment(paymentInvoiceModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitPayment(PaymentInvoiceModel paymentInvoiceModel, Transaction transaction, APIResponseListener listener) {
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        paymentInvoiceModel.setTransactionHash(StellarUtils.convertToHexString(transaction.hash()));
        PaymentModel paymentModel = new PaymentModel(
                accountModel.getPublic_key(),
                paymentInvoiceModel.getShop().getPublic_key(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getPayment(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getTeeCoinFee(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getShop().getEx_tee(), Locale.US),
                paymentInvoiceModel.getInvoice(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getInvoice_amount(), Locale.US),
                TCUtils.formatMoney(TCConstant.THREE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getRemainAmount(), Locale.US),
                paymentInvoiceModel.getTransactionHash(),
                transaction.toEnvelopeXdrBase64(),
                TCUtils.formatMoney(TCConstant.FOUR_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceModel.getShop().getReturn_rate(), Locale.US)
        );

        mainActivity.requestApi(new WalletCreatePaymentRequest(paymentModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }

    // todo:new payment
    public void startPayment(PaymentInvoiceDetailModel paymentInvoiceDetailModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
//        Memo memo = Memo.text(String.format("%s,%s,%s",
//                EnumMgr.TransactionType.Payment.getValue(),
//                "190904",//CXDYD-190904-ZYGTRIDT
//                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceDetailModel.getPaid_amount(), Locale.US)));
        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceDetailModel.getPaid_amount(), Locale.US),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceDetailModel.getFee_amount(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, paymentInvoiceDetailModel.getBasic_fee_amount(), Locale.US),
                paymentInvoiceDetailModel.getPaid_amount_destination(),
                paymentInvoiceDetailModel.getFee_amount_destination(),
                paymentInvoiceDetailModel.getBasic_fee_amount_destination(),
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        //TCLog.e("tu paymnet onStellarSuccess");
                        if (o instanceof Transaction) {
                            submitPayment(paymentInvoiceDetailModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        // TCLog.e("tu paymnet onStellarFail "+e.getLocalizedMessage());
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitPayment(PaymentInvoiceDetailModel paymentInvoiceDetailModel, Transaction transaction, APIResponseListener listener) {
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        paymentInvoiceDetailModel.setTransactionHash(StellarUtils.convertToHexString(transaction.hash()));
        PaymentModel paymentModel = new PaymentModel(
                accountModel.getPublic_key(),
                paymentInvoiceDetailModel.getInvoice(), transaction.toEnvelopeXdrBase64(), paymentInvoiceDetailModel.getTransactionHash());
//
        //  TCLog.e("paymentModel "+TCUtils.convertToJson(paymentModel));
        mainActivity.requestApi(new UserCreatePaymentRequest(paymentModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }

    public void startCoinBack(CoinBackModel coinBackModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel shopModel = RealmController.getInstance().getData(AccountModel.class);
        Memo memo = Memo.text(String.format("%s,%s,%s",
                EnumMgr.TransactionType.CoinBack.getValue(),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getPayment(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getAmount(), Locale.US)));

        stellarGetTransaction(
                SecretKeyEncryption.decrypt(shopModel.getSecret_key()),
                coinBackModel.getDestination(),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getAmount(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getFee_amount(), Locale.US),
                memo,
                false, new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o instanceof Transaction) {
                            submitCoinBack(coinBackModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitCoinBack(CoinBackModel coinBackModel, Transaction transaction, APIResponseListener listener) {
        coinBackModel.setTransaction_hash(StellarUtils.convertToHexString(transaction.hash()));
        coinBackModel.setXdr(transaction.toEnvelopeXdrBase64());
        coinBackModel.setAmount(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getAmount(), Locale.US));
        coinBackModel.setFee_amount(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, coinBackModel.getFee_amount(), Locale.US));
        mainActivity.requestApi(new WalletCreateCoinBackRequest(coinBackModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }


    public void startTransferMoney(TransferMoneyModel transferMoneyModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        byte[] noteBytes = transferMoneyModel.getNote().getBytes();
        String note = transferMoneyModel.getNote();
        if (noteBytes.length > TCConstant.SEND_MONEY_NOTE_MAX_LENGTH_IN_BYTES) {
            note = new String(Arrays.copyOfRange(noteBytes, 0, TCConstant.SEND_MONEY_NOTE_MAX_LENGTH_IN_BYTES));
        }
        Memo memo = Memo.text(note);

        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                transferMoneyModel.getDestination(),
                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, transferMoneyModel.getAmount(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, transferMoneyModel.getTeeCoinFee(), Locale.US),
                memo,
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o instanceof Transaction) {
                            submitTransferMoney(transferMoneyModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitTransferMoney(
            TransferMoneyModel transferMoneyModel,
            Transaction transaction,
            APIResponseListener listener) {
        transferMoneyModel.setTransaction_hash(StellarUtils.convertToHexString((transaction.hash())));
        transferMoneyModel.setXdr(transaction.toEnvelopeXdrBase64());
        mainActivity.requestApi(new WalletTransferMoneyRequest(transferMoneyModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }

    public void startReview(ReviewShopModel reviewShopModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        byte[] commentBytes = TCUtils.hashString(reviewShopModel.getComment()).getBytes();
        String commentHash = new String(Arrays.copyOfRange(commentBytes, 0, 22));

        Memo memo = Memo.text(String.format("%s,%s,%s",
                EnumMgr.TransactionType.Review.getValue(),
                reviewShopModel.getRating(),
                commentHash));
        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                reviewShopModel.getDestination(),
                reviewShopModel.getAmount(),
                "0",
                memo,
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o instanceof Transaction) {
                            submitReview(reviewShopModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });
    }

    private void submitReview(
            ReviewShopModel reviewShopModel,
            Transaction transaction,
            APIResponseListener listener) {
        reviewShopModel.setTransaction_hash(StellarUtils.convertToHexString((transaction.hash())));
        reviewShopModel.setXdr(transaction.toEnvelopeXdrBase64());
        mainActivity.requestApi(new ReviewUserSubmitReviewShopRequest(reviewShopModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }

    public void startPurchase(CouponCataloguePurchaseRequestModel purchaseRequestModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                TCUtils.formatMoney(TCConstant.TWO_DECIMAL_NO_COMMAS_FORMAT, purchaseRequestModel.getAmount(), Locale.US),
                TCUtils.formatMoney(TCConstant.TWO_DECIMAL_NO_COMMAS_FORMAT, purchaseRequestModel.getFee_amount(), Locale.US),
                TCUtils.formatMoney(TCConstant.TWO_DECIMAL_NO_COMMAS_FORMAT, purchaseRequestModel.getBasic_fee_amount(), Locale.US),
                purchaseRequestModel.getDestination(),
                purchaseRequestModel.getFee_amount_destination(),
                purchaseRequestModel.getBasic_fee_amount_destination(),
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        TCLog.d("linhnt 1");
                        if (o instanceof Transaction) {
                            TCLog.d("linhnt 2");

                            submitPurchase(purchaseRequestModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        TCLog.d("linhnt 3");

                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), WalletRequestTarget.CREATE_PAYMENT);
                    }
                });

    }

    private void submitPurchase(
            CouponCataloguePurchaseRequestModel purchaseRequestModel,
            Transaction transaction,
            APIResponseListener listener) {
        purchaseRequestModel.setTransaction_hash(StellarUtils.convertToHexString((transaction.hash())));
        purchaseRequestModel.setXdr(transaction.toEnvelopeXdrBase64());
        mainActivity.requestApi(new CouponUserPurchaseCatalogueRequest(purchaseRequestModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                TCLog.d("linhnt 4");

                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                TCLog.d("linhnt 5" , errorModel.getErrorMessage());
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }


    public void startTip(TipModel tipModel, APIResponseListener listener) {
        mainActivity.showLoadingDialog();
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        Memo memo = Memo.text(EnumMgr.TransactionType.Tip.getValue());
        tipModel.setSource(accountModel.getPublic_key());
        stellarGetTransaction(
                SecretKeyEncryption.decrypt(accountModel.getSecret_key()),
                tipModel.getDestination(),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, tipModel.getAmount(), Locale.US),
                // TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT, tipModel.getAmount(), Locale.US),
                TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, tipModel.getFee_amount(), Locale.US),
                memo,
                false,
                new StellarResponseListener() {
                    @Override
                    public void onStellarSuccess(Object o) {
                        if (o instanceof Transaction) {
                            submitTip(tipModel, (Transaction) o, listener);
                        }
                    }

                    @Override
                    public void onStellarFail(Throwable e) {
                        listener.onFail(new ErrorModel(
                                        StatusCode.BAD_REQUEST.getValue(), e.getMessage()),
                                StatusCode.BAD_REQUEST.getValue(), ReviewRequestTarget.CREATE_TIP);
                    }
                });
    }

    private void submitTip(TipModel tipModel, Transaction transaction, APIResponseListener listener) {
        tipModel.setTransaction_hash(StellarUtils.convertToHexString((transaction.hash())));
        tipModel.setXdr(transaction.toEnvelopeXdrBase64());
        mainActivity.requestApi(new ReviewShopCreateTipRequest(tipModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                listener.onSuccess(response, requestTarget);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                listener.onFail(errorModel, statusCode, requestTarget);
            }
        }));
    }


//    public void getStellarPayment(
//            String accountId,
//            String lastToken,
//            boolean isLoading,
//            StellarResponseListener listener) {
//
//        if (!TCUtils.isNetworkConnectionAvailable()) {
//            mainActivity.hideLoadingDialog();
//            mainActivity.showAlertDialog(View.NO_ID,
//                    TCUtils.getString(R.string.alert),
//                    TCUtils.getString(R.string.error_no_internet_connection),
//                    TCUtils.getString(R.string.text_ok), null, null);
//            return;
//        }
//        if (isLoading) {
//            mainActivity.showLoadingDialog();
//        }
//        StellarObserver.getInstance().getStellarPayment(accountId, lastToken)
//                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new StellarSubscriber<>(listener, isLoading));
//    }

    public void getStellarPayment(
            String transactionHash,
            boolean isLoading,
            StellarResponseListener listener) {

        if (!TCUtils.isNetworkConnectionAvailable()) {
            mainActivity.hideLoadingDialog();
            mainActivity.showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
            return;
        }
        if (isLoading) {
            mainActivity.showLoadingDialog();
        }
        StellarObserver.getInstance().getStellarPayment(transactionHash)
                .timeout(1, TimeUnit.MINUTES, Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StellarSubscriber<>(listener, isLoading));
    }
}
