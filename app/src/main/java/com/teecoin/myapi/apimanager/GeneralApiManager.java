package com.teecoin.myapi.apimanager;

import com.teecoin.feature.general.locationtracking.CurrentLocationModel;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.AvatarResponseModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.general.ChangeRecoveryPasswordModel;
import com.teecoin.model.general.CheckVerificationCodeModel;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.model.general.ProfileModel;
import com.teecoin.model.general.ReadNotificationRequestModel;
import com.teecoin.model.general.RegisterNotifyModel;
import com.teecoin.model.general.ResetPasswordByEmailRequestModel;
import com.teecoin.model.general.ResetPasswordByPublicKeyRequestModel;
import com.teecoin.model.general.ResetPasswordResponseModel;
import com.teecoin.model.general.SetNewRecoveryPasswordModel;
import com.teecoin.model.general.SocialInfoModel;
import com.teecoin.model.reviewsystem.ReferralEventModel;
import com.teecoin.myapi.apirequest.general.GeneralInitSocialPasswordRequest;
import com.teecoin.myapi.apirequest.general.GeneralStoreSecretKeyRequest;
import com.teecoin.myapi.apirequest.general.GeneralVerifyPasswordRequest;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

public interface GeneralApiManager {
    @Multipart
    @POST()
    Observable<BaseResponseModel<AvatarResponseModel>> updateAvatar(@Url String url, @Header("Authorization") String token, @Part MultipartBody.Part avatar);

    @POST()
    Observable<BaseResponseModel> accountLogout(@Url String url, @Body RegisterNotifyModel registerNotifyModel);

    @POST()
    Observable<BaseResponseModel<ResetPasswordResponseModel>> resetPasswordByPublicKey(@Url String url, @Body ResetPasswordByPublicKeyRequestModel resetPasswordByPublicKeyRequestModel);

    @POST()
    Observable<BaseResponseModel<ResetPasswordResponseModel>> resetPasswordByEmail(@Url String url, @Body ResetPasswordByEmailRequestModel resetPasswordByEmailRequestModel);

    @POST()
    Observable<BaseResponseModel> changeRecoveryPassword(@Url String url, @Header("Authorization") String token, @Body ChangeRecoveryPasswordModel changeRecoveryPasswordModel);

    @POST()
    Observable<BaseResponseModel<BaseResultsResponseModel<DataPushNotificationModel>>> readNotifications(@Url String url, @Header("Authorization") String token, @Body ReadNotificationRequestModel readNotificationRequestModel);

    @GET()
    Observable<BaseResponseModel<ReferralEventModel>> getReferralEventModel(@Url String url);

    @POST()
    Observable<BaseResponseModel<Object>> locationTrack(@Url String url, @Header("Authorization") String token, @Body CurrentLocationModel currentLocation);

    @POST()
    Observable<BaseResponseModel> accountNotificationRegistration(@Url String url, @Header("Authorization") String token, @Body RegisterNotifyModel registerNotifyModel);

    @GET()
    Observable<BaseResponseModel<String>> getVerificationCode(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<CheckVerificationCodeModel>> checkVerificationCode(@Url String url, @Header("Authorization") String token, @Body CheckVerificationCodeModel checkVerificationCodeModel);

    @POST()
    Observable<BaseResponseModel<String>> setNewRecoveryPassword(@Url String url, @Header("Authorization") String token, @Body SetNewRecoveryPasswordModel setNewRecoveryPasswordModel);

    @GET()
    Observable<BaseResponseModel<ProfileModel>> getProfileAccount(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<CouponDetailModel>> getData(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<AccountModel>> signUpSocial(@Url String url, @Header("Authorization") String token, @Body SocialInfoModel signUpSocialModel);

    @GET()
    Observable<BaseResponseModel<Boolean>> checkRewardStatus(@Url String url);

    @POST()
    Observable<BaseResponseModel<Boolean>> initSocialPassword(@Url String url, @Body GeneralInitSocialPasswordRequest.InitSocialPasswordModel initSocialPasswordModel);

    @POST()
    Observable<BaseResponseModel<Boolean>> verifyPassword(@Url String url, @Body GeneralVerifyPasswordRequest.VerifyPasswordModel initSocialPasswordModel);

    @GET()
    Observable<BaseResponseModel<Boolean>> checkSecretKey(@Url String url);

    @POST()
    Observable<BaseResponseModel<Boolean>> storeSecretKey(@Url String url, @Body GeneralStoreSecretKeyRequest.StoreSecretKey storeSecretKey);

    @GET()
    Observable<BaseResponseModel<BalanceModel>> getBalanceShop(@Url String url, @Header("Authorization") String token);
}
