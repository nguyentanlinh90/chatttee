package com.teecoin.myapi.apimanager;

import com.teecoin.feature.reviewSystem.tip.TipModel;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.couponsystem.FeatureExperienceModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.model.general.EventsModel;
import com.teecoin.model.reviewsystem.CommentDetailModel;
import com.teecoin.model.reviewsystem.CommentImageModel;
import com.teecoin.model.reviewsystem.CuisineModel;
import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.model.reviewsystem.GetStatusReviewModel;
import com.teecoin.model.reviewsystem.GetVendorIdForShop;
import com.teecoin.model.reviewsystem.GoogleReviewResponseModel;
import com.teecoin.model.reviewsystem.HighlightModel;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.model.reviewsystem.LikeModel;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.model.reviewsystem.PostVendorFavoriteModel;
import com.teecoin.model.reviewsystem.PromotionModel;
import com.teecoin.model.reviewsystem.ReferralInformationModel;
import com.teecoin.model.reviewsystem.ReviewShopModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.model.reviewsystem.ShareVendorModel;
import com.teecoin.model.reviewsystem.SuggestModel;
import com.teecoin.model.reviewsystem.TipDetailModel;
import com.teecoin.model.reviewsystem.TipResponseModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.model.reviewsystem.VendorGetWalletResponseModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.model.reviewsystem.VisitTypeModel;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserCheckReferralCodeRequest;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

public interface ReviewApiManager {

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<CuisineModel>>> getCuisines(@Url String url);

    @GET()
    Observable<BaseResponseModel<TipDetailModel>> getTipHistoryDetail(@Url String url);

    @GET()
    Observable<BaseResponseModel<ReferralInformationModel>> getReferralInformation(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ListReviewsModel>>> getFilterReview(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ListReviewsModel>>> getListReviews(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getListPlace(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<PromotionModel>>> getPromotion(@Url String url);

    @POST()
    Observable<BaseResponseModel<Object>> checkReferralCode(@Url String url, @Body ReviewUserCheckReferralCodeRequest.CheckReferralCodeModel checkReferralCodeModel);

    @GET()
    Observable<BaseResponseModel<GetVendorIdForShop>> shopGetVendorID(@Url String url);

    @GET()
    Observable<BaseResponseModel<VendorDetailModel>> getVendorDetail(@Url String url,@Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ListReviewsModel>>> getTopMostReviews(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<HighlightModel>>> getListHighLight(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getNewMerchants(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<DataPushNotificationModel>>> getLisNotifications(@Url String url, @Header("Authorization") String token);

    @Multipart
    @POST()
    Observable<BaseResponseModel<BaseResultsResponseModel<CommentImageModel>>> uploadCommentImage(@Url String url, @Part MultipartBody.Part[] files);

    @GET()
    Observable<BaseResponseModel<VendorGetWalletResponseModel>> getWalletVendor(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<VendorGetWalletResponseModel>> getLogoReview(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ReviewVendorDetailModel>>> getVendorReviews(@Url String url);

    @GET()
    Observable<BaseResponseModel<GetVendorIdForShop>> getVendorID(@Url String url);

    @Multipart
    @POST()
    Observable<BaseResponseModel<Object>> uploadCommentVideo(@Url String url, @Part MultipartBody.Part[] files, @Part("uuid_video") String uuId);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getVendors(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getCuisineListByID(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getFilterRestaurant(@Url String url);

    @POST()
    Observable<BaseResponseModel> userLike(@Url String url, @Header("Authorization") String token, @Body LikeModel likeModel);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<EventsModel>>> getListEvent(@Url String url);

    @POST()
    Observable<BaseResponseModel> submitUserReviewShop(@Url String url, @Header("Authorization") String token, @Body ReviewShopModel reviewShopModel);

    @POST()
    Observable<BaseResponseModel<TipResponseModel>> createTip(@Url String url, @Header("Authorization") String token, @Body TipModel tipModel);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueDataModel>>> getVendorCoupon(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorCategoryModel>>> getVendorCategories(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueDataModel>>> getCouponRecommendations(@Url String url, @Header("Authorization") String token);

//    @GET
//    Observable<BaseResponseModel<BaseResultsResponseModel<UserCouponCatalogueModel>>> getCouponRecommendCategories(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel> submitFavoriteVendor(@Url String url, @Header("Authorization") String token, @Body PostVendorFavoriteModel postVendorFavoriteModel);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getListFavorite(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getVendor(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<com.teecoin.model.reviewsystem.VendorCategoryModel>>> getVendorByCategory(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<ShareVendorModel>> getLinkShareVendor(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<GoogleReviewResponseModel>> getGoogleReview(@Url String url);

    @GET()
    Observable<BaseResponseModel<GetStatusReviewModel>> getReviewStatus(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<SuggestModel>>> getListSuggest(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<String>>> getListKeySearchRecent(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<ImagesVendor>>> getListImageVendor(@Url String url);

    @GET()
    Observable<BaseResponseModel<CommentDetailModel>> getCommentDetail(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VisitTypeModel>>> getVisitType(@Url String url);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<VendorModel>>> getListCheckinRewards(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<com.teecoin.model.reviewsystem.VendorCategoryModel>>> getListCheckinRewardsByCategory(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<FeatureExperienceModel>>> getListArticles(@Url String url, @Header("Authorization") String token);

    @GET()
    Observable<BaseResponseModel<BaseResultsResponseModel<DiscoverBannerModel>>> getListDiscoverBanner(@Url String url, @Header("Authorization") String token);

    @POST()
    Observable<BaseResponseModel<DiscoverBannerModel>> submitBannerClick(@Url String url, @Header("Authorization") String token, @Body DiscoverBannerModel discoverBannerModel);
}
