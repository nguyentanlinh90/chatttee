package com.teecoin.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.teecoin.TCMainActivity;
import com.teecoin.feature.reviewSystem.listReviews.ListReviewScreen;
import com.teecoin.feature.reviewSystem.reviewforShop.ReviewForShopScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.feature.reviewSystem.vendorDetailTabPhotos.PhotoVendorDetailScreen;
import com.teecoin.feature.reviewSystem.vendorReview.ReviewDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.model.reviewsystem.LikeModel;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserLikeVendorRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public class TCReviewBaseFragment extends TCBaseFragment {

    @Override
    public void onBindView() {

    }


//    protected void getGoogleReviews(String placeId, GoogleApiSubscribe.GoogleResponseListener googleResponseListener) {
//        GoogleKeyConfigModel googleKeyConfigModel = RealmController.getInstance().getData(GoogleKeyConfigModel.class);
//        if (googleKeyConfigModel == null)
//            return;
//
//        GoogleReviewRequestModel googleReviewRequestModel =
//                new GoogleReviewRequestModel(placeId, googleKeyConfigModel.getGoogleKey());
//        GoogleAPIManager googleAPIManager = RetrofitGenerator.createService(GoogleAPIManager.class, URL.googleServer());
//        googleAPIManager.getGooglePlaceId(
//                String.format(GoogleRequestTarget.GET_GOOGLE_PLACE_ID.toString()
//                        , googleReviewRequestModel.getPlaceid(), googleReviewRequestModel.getKey()))
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new GoogleApiSubscribe<>(googleResponseListener, GoogleRequestTarget.GET_GOOGLE_PLACE_ID));
//    }

    public void likeVendor(ListReviewsModel listReviewsModel, ReviewVendorDetailModel reviewVendorDetailModel, int position) {
        requestApi(new ReviewUserLikeVendorRequest(
                new LikeModel(listReviewsModel == null ? reviewVendorDetailModel.getId() : listReviewsModel.getId()),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        Fragment fragment = getTopFragment();
                        if (fragment instanceof VendorScreen) {
                            VendorScreen vendorScreen = (VendorScreen) fragment;
                            vendorScreen.updateLikeItemList(position);
                        } else if (fragment instanceof ListReviewScreen) {
                            ListReviewScreen vendorReviewScreen = (ListReviewScreen) fragment;
                            vendorReviewScreen.updateItemList(position);
                        } else if (fragment instanceof ReviewForShopScreen) {
                            ReviewForShopScreen vendorReviewScreen = (ReviewForShopScreen) fragment;
                            vendorReviewScreen.updateLikeItemList(position);
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    }
                }));
    }
    public void likeImageReview(ImagesVendor imagesVendor, int position) {
        requestApi(new ReviewUserLikeVendorRequest(
                new LikeModel(imagesVendor.getReview().getId()),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        Fragment fragment = getTopFragment();
                        if (fragment instanceof PhotoVendorDetailScreen) {
                            PhotoVendorDetailScreen screen = (PhotoVendorDetailScreen) fragment;
                            screen.updateLikeAndTipPhoto(position);
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    }
                }));
    }
    public void likeReview(ReviewVendorDetailModel reviewVendorDetailModel) {
        requestApi(new ReviewUserLikeVendorRequest(
                new LikeModel(reviewVendorDetailModel.getId()),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        Fragment fragment = getTopFragment();
                        if (fragment instanceof ReviewDetailScreen) {
                            ReviewDetailScreen screen = (ReviewDetailScreen) fragment;
                            screen.updateLikeAndTipPhoto();
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    }
                }));
    }

    public void openTabCoupon() {
        Fragment fragment = ((TCMainActivity) getActiveActivity()).getTopFragment();
        if (fragment != null) {
            if (fragment instanceof VendorScreen) {
                VendorScreen vendorScreen = (VendorScreen) fragment;
                vendorScreen.openTabCoupon();
            }
        }

    }

    protected void deselectView(View... views) {
        for (View v : views) {
            v.setSelected(false);
        }
    }

    protected void deselectView(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setSelected(false);
            }
        }
    }
}
