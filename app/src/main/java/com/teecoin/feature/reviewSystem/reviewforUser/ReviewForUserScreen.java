package com.teecoin.feature.reviewSystem.reviewforUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.general.account.AccountScreen;
import com.teecoin.feature.general.notification.NotificationScreen;
import com.teecoin.feature.general.popup.RequestLocationPermissionDialog;
import com.teecoin.feature.reviewSystem.listReviews.ListReviewScreen;
import com.teecoin.feature.reviewSystem.location.LocationScreen;
import com.teecoin.feature.reviewSystem.promotion.PromotionAdapter;
import com.teecoin.feature.reviewSystem.promotion.PromotionScreen;
import com.teecoin.feature.reviewSystem.restaurant.RestaurantScreen;
import com.teecoin.feature.reviewSystem.spa.SpaScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.AppBannerModel;
import com.teecoin.model.reviewsystem.HighlightModel;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.model.reviewsystem.PromotionModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetHighLightListRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNewMerchantRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNotificationRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetPromotionRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetTopMostReviewRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.ui.TCAutoScrollViewPager;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.BindView;

public class ReviewForUserScreen extends TCReviewBaseFragment implements APIResponseListener, TCDecisionListener, StellarResponseListener {
    private static final int MAX_REQUEST_LOCATION_TIMES = 3;
    private static final String USER_REVIEW_VENDOR_ID = "USER_REVIEW_VENDOR_ID";
    private static final String USER_REVIEW_GOTO_VENDOR_COUPON_LIST = "USER_REVIEW_GOTO_VENDOR_COUPON_LIST";
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_review_rl_search)
    View rl_search;
    @BindView(R.id.rl_search_box)
    View rl_search_box;
    @BindView(R.id.toolbar_chattee_main_review_iv_logo)
    ImageView iv_logo;
    @BindView(R.id.toolbar_review_iv_search)
    ImageView iv_search;
    @BindView(R.id.iv_search_box)
    ImageView iv_search_box;
    @BindView(R.id.toolbar_review_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.ed_search)
    EditText ed_search;
    @BindView(R.id.iv_remove)
    ImageView iv_remove;
    @BindView(R.id.left_menu_tv_number_notofication)
    TextView tv_number_notification;
    @BindView(R.id.left_menu_iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.left_menu_iv_avatar_border)
    ImageView left_menu_iv_avatar_border;
    @BindView(R.id.left_menu_tv_name)
    TextView tv_name;
    @BindView(R.id.left_menu_rl_notification)
    View rl_notification;
    @BindView(R.id.left_menu_rl_location)
    View rl_location;
    @BindView(R.id.left_menu_rl_category)
    View rl_category;
    @BindView(R.id.left_menu_group_category)
    View group_category;
    @BindView(R.id.left_menu_iv_catalogue_arrow)
    ImageView iv_catalogue_arrow;
    @BindView(R.id.left_menu_tv_restaurant)
    TextView tv_restaurant;
    @BindView(R.id.left_menu_tv_cuisine)
    TextView tv_cuisine;
    @BindView(R.id.left_menu_tv_spa)
    TextView tv_spa;
    @BindView(R.id.left_menu_rl_reviews)
    View rl_reviews;
    //------------------
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refresh_view;
    @BindView(R.id.ll_container)
    View ll_container;
    @BindView(R.id.ll_view_ll_banner)
    View ll_view_ll_banner;
    @BindView(R.id.frag_main_review_page_slide)
    TCAutoScrollViewPager images_slide;
    @BindView(R.id.frag_main_review_circle_page)
    CircleIndicatorPager circle_page;
    //--- highlight
    @BindView(R.id.ll_view_highlight)
    View ll_view_highlight;
    @BindView(R.id.frg_review_rcv_highlight)
    TCRecyclerView rcv_highlight;
    // --- place near
    @BindView(R.id.ll_view_ll_promotion)
    View ll_view_ll_promotion;
    @BindView(R.id.frag_review_ll_view_all_promotion)
    View ll_view_all_promotion;
    @BindView(R.id.frag_review_rcv_promotion)
    TCRecyclerView rcv_promotion;
    // --- place near
    @BindView(R.id.ll_view_ll_place_near_you)
    View ll_place_near_you;
    @BindView(R.id.frag_review_ll_near_you_view_all)
    View ll_near_you_view_all;
    @BindView(R.id.frag_review_rcv_place_near)
    TCRecyclerView rcv_place_near;
    // --- top 10
    @BindView(R.id.ll_view_ll_top_ten)
    View ll_top_ten;
    @BindView(R.id.frg_review_ll_view_all_top)
    View ll_view_all_top;
    @BindView(R.id.frg_review_rcv_top_review)
    TCRecyclerView rcv_top_review;
    //----- New merchants
    @BindView(R.id.ll_view_all_merchants)
    View ll_view_all_merchants;
    @BindView(R.id.ll_new_merchants)
    View ll_new_merchants;
    @BindView(R.id.frag_review_rcv_new_merchants)
    TCRecyclerView rcv_new_merchants;
    @BindView(R.id.nested_scroll)
    NestedScrollView nested_scroll;
    private ArrayList<HighlightModel> listPageSlide;
    private ArrayList<HighlightModel> listHighLight;
    private ArrayList<PromotionModel> listPromotion;
    private ArrayList<VendorModel> listPlaceNear;
    private ArrayList<ListReviewsModel> listTopMostReview;
    private ArrayList<VendorModel> listNewMerchants;
    private HighLightAdapter highLightAdapter;
    private PromotionAdapter promotionAdapter;
    private PlacesNearAdapter placesNearAdapter;
    private TopMostReviewAdapter topMostReviewAdapter;
    private NewMerchantsAdapter newMerchantsAdapter;
    private LatLng currentLatLng;
    private AccountModel accountModel;
    private int currentPageBanner = 0;
    private Timer timer;

    public static ReviewForUserScreen getInstance() {
        return new ReviewForUserScreen();
    }

    public static ReviewForUserScreen getInstance(String vendorId, boolean isGotoVendorCouponList) {
        ReviewForUserScreen screen = new ReviewForUserScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_REVIEW_VENDOR_ID, vendorId);
        bundle.putSerializable(USER_REVIEW_GOTO_VENDOR_COUPON_LIST, isGotoVendorCouponList);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_review, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        showFooter();
        showTabMenuBottom();
        getNewNotification();
    }

    @Override
    public void onBindView() {
        initSetupToolbar();
        handleSearchBox(false);
        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        stellarGetBalance(accountModel.getPublic_key(), true, this);
        getProfile();
        initView();
        requestData();
        refreshData();
        checkDeviceTokenExists();
        ((TCMainActivity) getActiveActivity()).handleDeepLinkIntent(getActiveActivity().getIntent());
        gotoVendorScreen();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initSetupToolbar() {
        getActiveActivity().setSupportActionBar(toolbar);
        getActiveActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActiveActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu_nav);
        handleClickMenuLeft();
        onClick();
    }


    private void handleSearchBox(boolean isShow) {
        rl_search_box.setVisibility(isShow ? View.VISIBLE : View.GONE);
        iv_logo.setVisibility(isShow ? View.GONE : View.VISIBLE);
        iv_search.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tv_cancel.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void onClick() {
        iv_search.setOnClickListener(v -> {
            handleSearchBox(true);
            showKeyboard(ed_search);
        });
        tv_cancel.setOnClickListener(v -> {
            handleSearchBox(false);
        });
        iv_remove.setOnClickListener(v -> {
            ed_search.setText("");
            iv_remove.setVisibility(View.GONE);

        });
        iv_search_box.setOnClickListener(v -> {
            if (!TCUtils.isEmpty(ed_search.getText().toString())) {
                gotoRestaurantScreen(false, ed_search.getText().toString());
            }
        });

        ed_search.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!TCUtils.isEmpty(ed_search.getText().toString())) {
                    gotoRestaurantScreen(false, ed_search.getText().toString());
                }
                return true;
            }
            return false;
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iv_remove.setVisibility(!TCUtils.isEmpty(s.toString()) ? View.VISIBLE : View.GONE);

            }
        });

        ll_view_all_promotion.setOnClickListener(v -> addFragment(PromotionScreen.getInstance()));
        ll_view_all_top.setOnClickListener(v -> gotoListReviewScreen());
        ll_near_you_view_all.setOnClickListener(v -> gotoRestaurantScreen(true, ""));
        ll_view_all_merchants.setOnClickListener(v -> addFragment(RestaurantScreen.getInstance(false, currentLatLng, EnumMgr.TypeRestaurants.NewMerchants.getValue(), "")));
    }

    private void handleClickMenuLeft() {
        group_category.setVisibility(View.GONE);
        rl_notification.setOnClickListener(v -> gotoNotificationScreen());
        rl_location.setOnClickListener(v -> gotoLocationScreen());
        rl_category.setOnClickListener(v -> groupCatalogue());
        tv_restaurant.setOnClickListener(v -> gotoRestaurantScreen(false, ""));
//        tv_cuisine.setOnClickListener(v -> gotoCuisineScreen());
        tv_spa.setOnClickListener(v -> gotoSpaScreen());
        rl_reviews.setOnClickListener(v -> gotoListReviewScreen());
        tv_name.setOnClickListener(v -> gotoAccountScreen());
        iv_avatar.setOnClickListener(v -> gotoAccountScreen());
    }

    private void gotoAccountScreen() {
        addFragment(AccountScreen.getInstance());
        closeDrawer();
    }

    private void gotoNotificationScreen() {
        addFragment(NotificationScreen.getInstance());
        closeDrawer();
    }

    private void gotoLocationScreen() {
        addFragmentForResult(EnumMgr.RequestCode.GOTO_MAP_FROM_HOME_REVIEW.getValue(), LocationScreen.getInstance());
        closeDrawer();
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_MAP_FROM_HOME_REVIEW.getValue()) {
            if (finishedResult != null && finishedResult.getExtras() != null) {
                Bundle bundle = finishedResult.getExtras();
                listPlaceNear.clear();
                currentLatLng = bundle.getParcelable(TCConstant.LAT_LNG_CURRENT);
                listNewMerchants.clear();
                requestPlaceNearYouAndNewMerchants();
            }
        }
    }

    private void groupCatalogue() {
        if (group_category.getVisibility() == View.VISIBLE) {
            group_category.setVisibility(View.GONE);
            iv_catalogue_arrow.setImageResource(R.drawable.ic_next_grey);
        } else {
            group_category.setVisibility(View.VISIBLE);
            iv_catalogue_arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
    }

    private void gotoRestaurantScreen(boolean isNearYou, String keyword) {
        addFragment(RestaurantScreen.getInstance(false, currentLatLng, isNearYou ? EnumMgr.TypeRestaurants.NearYou.getValue() : EnumMgr.TypeRestaurants.Restaurants.getValue(), keyword));
        closeDrawer();
    }

    private void gotoSpaScreen() {
        addFragment(SpaScreen.getInstance());
        closeDrawer();
    }

    private void gotoListReviewScreen() {
        addFragment(ListReviewScreen.getInstance());
        closeDrawer();
    }

    private void closeDrawer() {
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressLint("CheckResult")
    private void getProfile() {
        if (accountModel == null) {
            accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        }
        tv_name.setText(String.format(TCUtils.getString(R.string.string_format_1), accountModel.getFirst_name(), accountModel.getLast_name()));
        Glide.with(getActiveActivity()).load(TCUtils.getDrawable(R.drawable.bg_circle_image)).apply(RequestOptions.circleCropTransform()).into(left_menu_iv_avatar_border);
        Glide.with(getActiveActivity()).load((accountModel.getAvatar() != null && !TCUtils.isEmpty(accountModel.getAvatar())) ?
                accountModel.getAvatar() : TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)).apply(RequestOptions.circleCropTransform()).into(iv_avatar);
    }

    private void initView() {
        initList();
        images_slide.startAutoScroll();
        images_slide.setInterval(3000);
        images_slide.setCycle(true);
        images_slide.setStopScrollWhenTouch(true);
        //1. top image
//        pageSlideAdapter = new SlideBannerAdapter(getActiveActivity(), listPageSlide);
//        images_slide.setAdapter(pageSlideAdapter);

        //2. highlight
        highLightAdapter = new HighLightAdapter(LayoutInflater.from(getActiveActivity()), listHighLight, (view, item, position, clickType) -> addFragment(VendorScreen.getInstance(item.getId())));
        rcv_highlight.setAdapter(highLightAdapter);

        //3. near place
        placesNearAdapter = new PlacesNearAdapter(LayoutInflater.from(getActiveActivity()), listPlaceNear, (view, item, position, clickType) -> addFragment(VendorScreen.getInstance(item.getId())));
        rcv_place_near.setAdapter(placesNearAdapter);

        //4. promotion
        promotionAdapter = new PromotionAdapter(LayoutInflater.from(getActiveActivity()), listPromotion, (view, item, position, clickType) -> addFragment(WebViewScreen.getInstance(item.getDetail_url(), TCUtils.getString(R.string.review_promotion_detail_title))), false);
        rcv_promotion.setAdapter(promotionAdapter);

        //5. top most review
//        listTopMostReview = new ArrayList<>();
//        topMostReviewAdapter = new TopMostReviewAdapter(LayoutInflater.from(getActiveActivity()), listTopMostReview,
//                (view, item, position, clickType) -> VendorScreen.getInstance(item.getVendor().getId())));
        topMostReviewAdapter = new TopMostReviewAdapter(LayoutInflater.from(getActiveActivity()), listTopMostReview, (view, item, position, clickType) -> {
            /*VendorDetailModel vendorDetailModel = new VendorDetailModel(item.getVendor().getId(), item.getVendor().getName(), item.getVendor().getPlace_id());
            addFragment(VendorReviewScreen.newInstance(vendorDetailModel));*/
        });
        rcv_top_review.setAdapter(topMostReviewAdapter);

        //6. new merchant
        newMerchantsAdapter = new NewMerchantsAdapter(LayoutInflater.from(getActiveActivity()), listNewMerchants,
                (view, item, position, clickType) -> addFragment(VendorScreen.getInstance(item.getId())));
        rcv_new_merchants.setAdapter(newMerchantsAdapter);
    }

    private void initList() {
        refresh_view.setEnabled(false);
        listPageSlide = new ArrayList<>();
        listHighLight = new ArrayList<>();
        listPromotion = new ArrayList<>();
        listPlaceNear = new ArrayList<>();
        listTopMostReview = new ArrayList<>();
        listNewMerchants = new ArrayList<>();
    }

    private void requestData() {
        //1. banner
        getListBanner();

        //2. highlight;
        requestApi(new ReviewUserGetHighLightListRequest(this));

        //3. near place
        checkCurrentLocation();

        //4. promotion
        requestApi(new ReviewUserGetPromotionRequest(false, this));

        //5. top most review
        requestApi(new ReviewUserGetTopMostReviewRequest(this));

        //6. new merchant
        newMerchantsAdapter = new NewMerchantsAdapter(LayoutInflater.from(getActiveActivity()), listNewMerchants,
                (view, item, position, clickType) -> addFragment(VendorScreen.getInstance(item.getId())));
        rcv_new_merchants.setAdapter(newMerchantsAdapter);
    }

    private void getListBanner() {

        ArrayList<AppBannerModel> appBannerModel = RealmController.getInstance().getListAppBanner();
        if (appBannerModel != null && appBannerModel.size() > 0) {
            SlideBannerAdapter slideBannerAdapter = new SlideBannerAdapter(getActiveActivity(), appBannerModel);
            images_slide.setAdapter(slideBannerAdapter);
            circle_page.setViewPager(images_slide);
            ll_view_ll_banner.setVisibility(View.VISIBLE);
            ll_view_ll_banner.getLayoutParams().width = TCScreenSize.getWidth(getActiveActivity());
            ll_view_ll_banner.getLayoutParams().height = TCScreenSize.getBannerChatteApp(TCScreenSize.getWidth(getActiveActivity()));
        } else {
            ll_view_ll_banner.setVisibility(View.GONE);
        }
    }

    private void requestPlaceNearYouAndNewMerchants() {
        if (currentLatLng != null) {
            requestApi(new ReviewUserGetNewMerchantRequest(1,
                    TCConstant.KM_DEFAULT, "1",
                    TCDateUtility.getTimeZone(), currentLatLng,
                    false, this));
//            requestApi(new UserDiscoverGetRecommendForYouRequest(false,
//                    new PlaceAutoCompleteModel(1, currentLatLng), this));
        }
    }

    private void checkCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getActiveActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            boolean alreadyDenied = shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (alreadyDenied) {
                int count = TCSharePreferenceManager.getInstance().getInt(DataKey.CountRequestLocationTime) + 1;
                if (count <= MAX_REQUEST_LOCATION_TIMES) {
                    new RequestLocationPermissionDialog(getActiveActivity(), this, count).show();
                } else {
                    getGPS();
                }
            } else {
                requestPermissions(TCConstant.LOCATION_PERMISSIONS, EnumMgr.RequestCode.LOCATION.getValue());
            }
        } else {
            getGPS();
        }
    }

    private void getGPS() {
        if (ContextCompat.checkSelfPermission(getActiveActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            currentLatLng = TCConstant.LOCATION_DEFAULT;
        } else {
            if (TCUtils.isEnableGPS()) {
                currentLatLng = TCUtils.getGPS(getActiveActivity());
            } else {
                currentLatLng = TCConstant.LOCATION_DEFAULT;
            }
            ((TCMainActivity) getActiveActivity()).startLocationTracking();
        }
        requestPlaceNearYouAndNewMerchants();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.LOCATION.getValue()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGPS();
            } else {
                currentLatLng = TCConstant.LOCATION_DEFAULT;
                requestPlaceNearYouAndNewMerchants();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE.getValue()) {
            getGPS();
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        ll_container.setVisibility(View.VISIBLE);
        if (refresh_view.isRefreshing()) {
            refresh_view.setRefreshing(false);
        }
        refresh_view.setEnabled(true);
        if (requestTarget == ReviewRequestTarget.GET_LIST_HIGHLIGHT) {
            ArrayList<HighlightModel> list = ((BaseResultsResponseModel<HighlightModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listHighLight.addAll(list.size() > TCConstant.LIST_DEFAULT_5 ? list.subList(0, TCConstant.LIST_DEFAULT_5) : list);
                ll_view_highlight.setVisibility(View.VISIBLE);
                highLightAdapter.notifyDataSetChanged();
            } else {
                ll_view_highlight.setVisibility(View.GONE);
            }

        } else if (requestTarget == ReviewRequestTarget.GET_PROMOTION) {
            ArrayList<PromotionModel> list = ((BaseResultsResponseModel<PromotionModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listPromotion.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                ll_view_ll_promotion.setVisibility(View.VISIBLE);
                promotionAdapter.notifyDataSetChanged();
            } else {
                ll_view_ll_promotion.setVisibility(View.GONE);
            }

        } else if (requestTarget == ReviewRequestTarget.GET_RECOMMEND_FOR_YOU) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listPlaceNear.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                ll_place_near_you.setVisibility(View.VISIBLE);
                placesNearAdapter.notifyDataSetChanged();
            } else {
                ll_place_near_you.setVisibility(View.GONE);
            }
        } else if (requestTarget == ReviewRequestTarget.GET_TOP_MOST_REVIEWS) {
            ArrayList<ListReviewsModel> list = ((BaseResultsResponseModel<ListReviewsModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listTopMostReview.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                ll_top_ten.setVisibility(View.VISIBLE);
                topMostReviewAdapter.notifyDataSetChanged();
            } else {
                ll_top_ten.setVisibility(View.GONE);
            }

        } else if (requestTarget == ReviewRequestTarget.GET_NEW_MERCHANTS) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listNewMerchants.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                ll_new_merchants.setVisibility(View.VISIBLE);
                newMerchantsAdapter.notifyDataSetChanged();
            } else {
                ll_new_merchants.setVisibility(View.GONE);
            }
        } else if (requestTarget == ReviewRequestTarget.USER_GET_NOTIFY) {
//            ArrayList<DataPushNotificationModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            String unread = ((BaseResultsResponseModel) response.getResult()).getUnread();
            if (!TCUtils.isEmpty(unread) && !unread.equals("0")) {
                tv_number_notification.setText(unread);
            } else {
                tv_number_notification.setText("");
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_LIST_HIGHLIGHT) {
            ll_view_highlight.setVisibility(View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_PROMOTION) {
            ll_view_ll_promotion.setVisibility(View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_RECOMMEND_FOR_YOU) {
            ll_place_near_you.setVisibility(View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_TOP_MOST_REVIEWS) {
            ll_top_ten.setVisibility(View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_NEW_MERCHANTS) {
            ll_new_merchants.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositiveButtonClicked(int id, Object onWhat) {
        //open
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActiveActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE.getValue());
    }

    @Override
    public void onNegativeButtonClicked(int id, Object onWhat) {
        TCSharePreferenceManager.getInstance().setInt(DataKey.CountRequestLocationTime, (Integer) onWhat);
        getGPS();
    }

    @Override
    public void onNeutralButtonClicked(int id, Object onWhat) {

    }

    private void refreshData() {
        refresh_view.setOnRefreshListener(() -> {
            ll_container.setVisibility(View.GONE);
            initView();
            requestData();
        });
    }

    private void getNewNotification() {
        requestApi(new ReviewUserGetNotificationRequest(1, this));
    }

    @Override
    public void onStellarSuccess(Object object) {
        if (object instanceof String) {
            String teeCoinAmount = (String) object;
            accountModel.setBalance(TCUtils.isEmpty(teeCoinAmount) ? "0" : teeCoinAmount);
            RealmController.getInstance().updateBalanceAccount(accountModel);
        }
    }

    @Override
    public void onStellarFail(Throwable t) {

    }


    private void gotoVendorScreen() {
        if (getArguments() != null) {
            if (!TCUtils.isEmpty(getArguments().getString(USER_REVIEW_VENDOR_ID))) {
                addFragment(VendorScreen.getInstance(getArguments().getString(USER_REVIEW_VENDOR_ID),
                        getArguments().getBoolean(USER_REVIEW_GOTO_VENDOR_COUPON_LIST)));
            }
        }
    }
}

