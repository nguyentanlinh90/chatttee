package com.teecoin.myapi.apimanager;

import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.FiatWalletModel;
import com.teecoin.model.walletsystem.CheckSecretResponseModel;
import com.teecoin.model.walletsystem.CoinBackModel;
import com.teecoin.model.walletsystem.CoinBackPercentageModel;
import com.teecoin.model.walletsystem.CoinBackResultModel;
import com.teecoin.model.walletsystem.ConvertCurrenciesModel;
import com.teecoin.model.walletsystem.ConvertToTecModel;
import com.teecoin.model.walletsystem.ConvertToTecPostModel;
import com.teecoin.model.walletsystem.CreateAccountModel;
import com.teecoin.model.walletsystem.CryptoModel;
import com.teecoin.model.walletsystem.InvoiceModel;
import com.teecoin.model.walletsystem.LoginEmailModel;
import com.teecoin.model.walletsystem.PaymentDetailModel;
import com.teecoin.model.walletsystem.PaymentInfoModel;
import com.teecoin.model.walletsystem.PaymentInvoiceDetailModel;
import com.teecoin.model.walletsystem.PaymentInvoiceSubmitModel;
import com.teecoin.model.walletsystem.PaymentModel;
import com.teecoin.model.walletsystem.QRCodeInfoModel;
import com.teecoin.model.walletsystem.RewardModel;
import com.teecoin.model.walletsystem.RewardResultModel;
import com.teecoin.model.walletsystem.SendOTPModel;
import com.teecoin.model.walletsystem.TopUpPostModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.TransactionHistoryListResponseModel;
import com.teecoin.model.walletsystem.TransferMoneyModel;
import com.teecoin.model.walletsystem.UserCoinBackDetailModel;
import com.teecoin.model.walletsystem.VerifyOTPModel;
import com.teecoin.model.walletsystem.WithdrawModel;
import com.teecoin.model.walletsystem.WithdrawPostModel;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetQRCodeInfoRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface WalletApiManager {

    @POST()
    Observable<BaseResponseModel<AccountModel>> logInAccount(@Url String url, @Body AccountModel userAccountModel);

    @GET()
    Observable<BaseResponseModel<Object>> checkExistEmail(@Url String url);

    @GET()
    Observable<BaseResponseModel<Object>> checkExistPhone(@Url String url);

    @POST()
    Observable<BaseResponseModel<CreateAccountModel>> createUserAccount(@Url String url, @Body AccountModel userAccountModel);

//    @POST()
//    Observable<BaseResponseModel<AccountModel>> updateShopAccount(@Url String url, @Header("Authorization") String token, @Body AccountModel updateShopInfoModel);

    @POST()
    Observable<BaseResponseModel<AccountModel>> updateAccount(@Url String url, @Header("Authorization") String token, @Body AccountModel accountModel);

    @POST()
    Observable<BaseResponseModel<CreateAccountModel>> createShopAccount(@Url String url, @Body AccountModel shopModel);

//    @GET()
//    Observable<BaseResponseModel<TransactionHistoryListResponseModel>> getShopTransactionHistory(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<TransactionHistoryListResponseModel>> getAccountTransactionHistory(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<PaymentInvoiceDetailModel>> getPaymentDetail(@Url String url);

    @GET()
    Observable<BaseResponseModel<TransferMoneyModel>> getTransferMoneyDetail(@Url String url);

    @GET()
    Observable<BaseResponseModel<RewardResultModel>> getRewardDetail(@Url String url);

    @GET()
    Observable<BaseResponseModel<UserCoinBackDetailModel>> getCoinbackDetail(@Url String url);

    @GET()
    Observable<BaseResponseModel<CheckSecretResponseModel>> checkPublicKeyExist(@Url String url);

    @GET()
    Observable<BaseResponseModel<PaymentDetailModel>> checkPaymentStatusByInvoiceId(@Url String url);

    @POST()
    Observable<BaseResponseModel<QRCodeInfoModel>> getQRInfo(@Url String url, @Header("Authorization") String token, @Body WalletGetQRCodeInfoRequest.QRCodeInfoRequest qrCodeInfoRequest);

    @GET()
    Observable<BaseResponseModel<TransactionDetailModel>> getTransactionDetail(@Url String url);

    @POST()
    Observable<BaseResponseModel<RewardResultModel>> createReward(@Url String url, @Header("Authorization") String token, @Body RewardModel rewardModel);

    @POST()
    Observable<BaseResponseModel<PaymentDetailModel>> createPayment(@Url String url, @Header("Authorization") String token, @Body PaymentModel paymentModel);

    @POST()
    Observable<BaseResponseModel<CoinBackResultModel>> createCoinBack(@Url String url, @Header("Authorization") String token, @Body CoinBackModel coinBackModel);

    @POST()
    Observable<BaseResponseModel<TransferMoneyModel>> transferMoney(@Url String url, @Header("Authorization") String token, @Body TransferMoneyModel transferMoneyModel);

    @POST()
    Observable<BaseResponseModel<String>> sendPhoneOTP(@Url String url, @Body SendOTPModel sendOTPModel);

    @POST()
    Observable<BaseResponseModel<Boolean>> verifyOTP(@Url String url, @Body VerifyOTPModel verifyOTPModel);

    @POST()
    Observable<BaseResponseModel<AccountModel>> loginEmail(@Url String url, @Body LoginEmailModel loginEmailModel);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<CryptoModel>>> listCrypto(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<CryptoModel>> postCrypto(@Url String url, @Header("Authorization") String token, @Body TopUpPostModel topUpPostModel);

    @POST()
    Observable<BaseResponseModel<AccountModel>> updateCoinBack(@Url String url, @Body CoinBackPercentageModel coinBackPercentageModel);

    @GET()
    Observable<BaseResponseModel<PaymentInfoModel>> getPaymentInfo(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<InvoiceModel>> getPaymentInvoice(@Url String url, @Header("Authorization") String token, @Body PaymentInvoiceSubmitModel paymentInvoiceSubmitModel);

    @GET()
    Observable<BaseResponseModel<ConvertCurrenciesModel>> getConvertCurrencies(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<PaymentInvoiceDetailModel>> getPaymentInvoiceDetail(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<PaymentInvoiceDetailModel>> userCreatePayment(@Url String url, @Header("Authorization") String token, @Body PaymentModel paymentModel);

    @GET()
    Observable<BaseResponseModel<PaymentInvoiceSubmitModel>> checkPayment(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<FiatWalletModel>>> getSGDTransactionHistory(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<ConvertToTecModel>> convertToTec(@Url String url, @Body ConvertToTecPostModel convertToTecPostModel);

    @POST()
    Observable<BaseResponseModel<WithdrawModel>> withdraw(@Url String url, @Body WithdrawPostModel withdrawPostModel);

    @GET()
     Observable<BaseResponseModel<ConvertToTecModel>> getConvertToTecDetail(@Url String url) ;

    @GET()
    Observable<BaseResponseModel<WithdrawModel>> getWithdrawDetail(@Url String url) ;

}
