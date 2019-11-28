package com.teecoin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.catalogues.UserCatalogueListScreen;
import com.teecoin.feature.couponSystem.user.checkinNotification.CheckInNotificationScreen;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCheckInResultScreen;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponResultScreen;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserRedeemResultScreen;
import com.teecoin.feature.couponSystem.user.dailyReward.DailyRewardScreen;
import com.teecoin.feature.couponSystem.user.discover.DiscoverScreen;
import com.teecoin.feature.couponSystem.user.discover.FavouriteAndShareListener;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.couponSystem.user.recommendCoupons.RecommendCouponsScreen;
import com.teecoin.feature.general.firebase.MyFireBase;
import com.teecoin.feature.general.popup.PopupEvents;
import com.teecoin.feature.payment.userPaymentSuccess.UserPaymentSuccessScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.feature.reviewSystem.vendorcoupon.VendorCouponScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.EventsModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.model.reviewsystem.PostVendorFavoriteModel;
import com.teecoin.model.reviewsystem.ShareVendorModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetEventListRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetLinkShareVendorRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorDetailRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserSubmitFavoriteRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class TCCouponBaseFragment extends TCBaseFragment {
    //    private LatLng latLng =TCConstant.LOCATION_DEFAULT;
//    LocationManager locationManager;

    @Override
    public void onBindView() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getLocationListener();
        ((TCMainActivity) getActiveActivity()).getListFilterCategory();

        streamExchangeRate();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void submitFavourite(int position, VendorModel vendorModel, FavouriteAndShareListener postFavouriteListenner) {
        PostVendorFavoriteModel postVendorFavoriteModel
                = new PostVendorFavoriteModel(vendorModel.getId(), !vendorModel.isFavorite());
        requestApi(new ReviewUserSubmitFavoriteRequest(postVendorFavoriteModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                postFavouriteListenner.submitFavoriteSuccess(position, vendorModel);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    public void getShareVendor(VendorModel vendorModel) {
        if (!TCUtils.isEmpty(vendorModel.getUrlShare())) {
            TCUtils.shareLinkVendor(vendorModel.getUrlShare());
        } else {
            requestApi(new ReviewGetLinkShareVendorRequest(vendorModel.getId(), new APIResponseListener() {
                @Override
                public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                    ShareVendorModel shareVendorModel = (ShareVendorModel) response.getResult();
                    TCUtils.shareLinkVendor(shareVendorModel.getUrlShare());
                }

                @Override
                public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    showBaseMessage(errorModel.getErrorMessage());
                }
            }));
        }
        // todo
    }

    public void writeReview(VendorModel vendorModel) {
        requestApi(new ReviewShopGetVendorDetailRequest(vendorModel.getId(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                VendorDetailModel detailModel = (VendorDetailModel) response.getResult();
                addFragment(UserReviewShopScreen.getInstance(detailModel));
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
            }
        }));
    }

    public void getEvents() {
        requestApi(new ReviewGetEventListRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<EventsModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                if (list != null && list.size() > 0) {
                    ArrayList<EventsModel> listData = RealmController.getInstance().getListEvent();
                    if (listData != null && listData.size() > 0) {
                        checkEventExist(list, listData);
                    } else {
                        for (EventsModel event : list) {
                            RealmController.getInstance().insertData(event);
                        }
                        showEvent(list);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    private void checkEventExist(ArrayList<EventsModel> list, ArrayList<EventsModel> listData) {
        for (int i = 0; i < listData.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (listData.get(i).getId().equals(list.get(j).getId())) {
                    list.remove(j);
                }
            }
        }
        if (list.size() > 0) {
            //showEvent(list.get(0));
            showEvent(list);
            for (int i = 0; i < listData.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (!listData.get(i).getId().equals(list.get(j).getId())) {
                        RealmController.getInstance().insertData(listData.get(i));
                    }
                }
            }

        }
    }

    private void showEvent(ArrayList<EventsModel> eventsModel) {
//        PopupNotification notification = new PopupNotification(getActiveActivity(), eventsModel, (id, onWhat) -> addFragment(WebViewScreen.getInstance(eventsModel.getEvent_url(), EnumMgr.PushNotification.Referral.getValue())));
//        notification.show();
//        new PopupEvents(getActiveActivity(), eventsModel, new TCConfirmListener() {
//            @Override
//            public void onConfirmed(int id, Object onWhat) {
//                EventsModel model = (EventsModel) onWhat;
//                addFragment(WebViewScreen.getInstance(model.getEvent_url(), EnumMgr.PushNotification.Event.getValue()));
//            }
//        }).show();

        showSingleDialog(new PopupEvents(getActiveActivity(), eventsModel, new TCConfirmListener() {
            @Override
            public void onConfirmed(int id, Object onWhat) {
                EventsModel model = (EventsModel) onWhat;
                addFragment(WebViewScreen.getInstance(model.getEvent_url(), TCUtils.getString(R.string.events)));
            }
        }));
    }

    public void setListCategoryToSort(ArrayList<VendorCategoryModel> listCategorySort) {
        ((TCMainActivity) getActiveActivity()).setListCategoryToSort(listCategorySort);
    }

    public VendorCategoryModel getVendorCategorySelected() {
        return ((TCMainActivity) getActiveActivity()).getVendorCategorySelected();
    }

    public void openHowtoEarnTECScreen() {
        // new TermsOfServiceScreen(getActiveActivity(), TCUtils.getString(R.string.text_how_to_earn_tec), TCConstant.URL_EARN_TEC).show();
        addFragment(WebViewScreen.getInstance(TCConstant.URL_EARN_TEC, TCUtils.getString(R.string.text_how_to_earn_tec)));
    }

    public void openAgodaWebsiteScreen() {
        // new TermsOfServiceScreen(getActiveActivity(), "Agoda", TCConstant.URL_AGODA_WEBSITE).show();
        addFragment(WebViewScreen.getInstance(TCConstant.URL_AGODA_WEBSITE, TCUtils.getString(R.string.text_agoda)));
    }

    public void handelScrollingCategory(AppBarLayout appBarLayout, int verticalOffset, TCRecyclerView rcvCategoryImage, View vcategoryName) {
        float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());

        rcvCategoryImage.setAlpha(1 - (offsetAlpha * -1));

        if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            // Collapsed
            vcategoryName.setAlpha(1);

        } else if (verticalOffset == 0) {
            // Expanded
            // TCLog.e("Expanded " + verticalOffset);
            vcategoryName.setAlpha(0);
        } else {
            // TCLog.e("Somewhere in between " + verticalOffset);
            // Somewhere in between
            if (verticalOffset < -160)
                vcategoryName.setAlpha(1 + (offsetAlpha * -1));
            else if (verticalOffset > -160 && verticalOffset < -100) {
                vcategoryName.setAlpha(0);
            }
        }
    }

    public void gotoVendorDetailScreen(String vendorID, boolean couponList) {
        addFragment(VendorScreen.getInstance(vendorID,
                couponList));
    }

    public void streamExchangeRate() {
        MyFireBase myFireBase = ((TCMainActivity) getActiveActivity()).getMyFireBase();
        myFireBase.exchangeRateStreaming(TransactionConfigModel.DEFAULT_CODE, (rate, fee, spread, basic_fee_amount) -> {
            setExChangeRates(rate, fee, spread, basic_fee_amount);
            if (getTopFragment() instanceof DiscoverScreen) {
                DiscoverScreen screen = (DiscoverScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserCatalogueListScreen) {
                UserCatalogueListScreen screen = (UserCatalogueListScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof DailyRewardScreen) {
                DailyRewardScreen screen = (DailyRewardScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof RecommendCouponsScreen) {
                RecommendCouponsScreen screen = (RecommendCouponsScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserCouponScreen) {
                UserCouponScreen screen = (UserCouponScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserPaymentSuccessScreen) {
                UserPaymentSuccessScreen screen = (UserPaymentSuccessScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof VendorCouponScreen) {
                VendorCouponScreen screen = (VendorCouponScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserMyCouponDetailScreen) {
                UserMyCouponDetailScreen screen = (UserMyCouponDetailScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserCouponResultScreen) {
                UserCouponResultScreen screen = (UserCouponResultScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof CheckInNotificationScreen) {
                CheckInNotificationScreen screen = (CheckInNotificationScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserCheckInResultScreen) {
                UserCheckInResultScreen screen = (UserCheckInResultScreen) getTopFragment();
                screen.reloadPriceCoupon();
            } else if (getTopFragment() instanceof UserRedeemResultScreen) {
                UserRedeemResultScreen screen = (UserRedeemResultScreen) getTopFragment();
                screen.reloadPriceCoupon();
            }
        });
    }

    public void setExChangeRates(double rate, double fee, double spread, double basic_fee_amount) {
        ((TCMainActivity) getActiveActivity()).setExChangeRates(rate, fee, spread, basic_fee_amount);
    }

    public FeeConfigModel getFeeConfig() {
        return RealmController.getInstance().getData(FeeConfigModel.class);
    }
}
