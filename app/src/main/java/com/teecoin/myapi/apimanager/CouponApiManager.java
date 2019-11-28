package com.teecoin.myapi.apimanager;

import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.couponsystem.CheckinNotificationResultModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseRequestModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponCoinBackRedemptionResponse;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.DailyRewardDayModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.couponsystem.ResultGetCouponModel;
import com.teecoin.model.couponsystem.ShopCouponDetailModel;
import com.teecoin.model.couponsystem.ShopCouponModel;
import com.teecoin.model.couponsystem.ShopInfoQrCodeModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.UserCouponModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.model.reviewsystem.VendorModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

public interface CouponApiManager {
    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueDataModel>>> getCouponCataloguesList(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueDataModel>>> getFilterCatalogueCoupon(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<CheckinNotificationResultModel>> checkinNotification(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueModel>>> getCouponCatalogues(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<RedeemCodeCouponResultModel>> userRedeemCoupon(@Url String url, @Header("Authorization") String token, @Body VendorCodeRedeemModel vendorCodeRedeemModel);

    @POST()
    Observable<BaseResponseModel<RedeemCodeCouponResultModel>> getListCouponCheckIn(@Url String url, @Header("Authorization") String token, @Body VendorCodeCheckInModel vendorCodeCheckInModel);

    @GET()
    Observable<BaseResponseModel<CouponDetailModel>> getDetailCouponCatalogue(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<CouponDetailModel>> getUserCouponDetail(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<CouponDetailModel>> getCouponDetailFromTransaction(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<ResultGetCouponModel>> getUserGetCoupon(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponModel>>> getUserCouponList(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<CouponCataloguePurchaseResponseModel>> userCataloguePurchase(@Url String url, @Header("Authorization") String token, @Body CouponCataloguePurchaseRequestModel purchaseRequestModel);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponModel>>> getFilterUserCoupon(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ShopCouponModel>>> getFilterShopCoupon(@Url String url, @Header("Authorization") String token);

    @Multipart
    @POST()
    Observable<BaseResponseModel<ShopCouponDetailModel>> shopAddCoupon(@Url String url, @Header("Authorization") String token,
                                                                       @Part("name") RequestBody name, @Part("discount_type") RequestBody discount_type,
                                                                       @Part("percentage") RequestBody percentage, @Part("start") RequestBody start,
                                                                       @Part("end") RequestBody end, @Part("description") RequestBody description,
                                                                       @Part("term") RequestBody term, @Part("hashtags") RequestBody hashtags,
                                                                       @Part("tz") RequestBody tz, @Part MultipartBody.Part banner,
                                                                       @Part("cash") RequestBody cash);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ShopCouponModel>>> getShopCouponList(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<ShopInfoQrCodeModel>> getShopInfoQrCode(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<ShopCouponDetailModel>> getShopCouponDetail(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getTopRatedExperience(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<CountryCodeModel>>> getListCountry(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getListOutlet(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<DailyRewardDayModel>>> getDailyDayReward(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<DailyRewardDayModel>> getDailyGiveAway(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<CouponCoinBackRedemptionResponse>> getCoinBackRedemption(@Url String url, @Header("Authorization") String token);
}
