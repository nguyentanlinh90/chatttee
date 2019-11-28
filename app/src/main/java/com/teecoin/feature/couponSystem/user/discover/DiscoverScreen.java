package com.teecoin.feature.couponSystem.user.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.feature.couponSystem.user.couponCatalogue.RecommendCouponsHorizontalAdapter;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCheckInResultScreen;
import com.teecoin.feature.couponSystem.user.dailyReward.DailyRewardScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.DetailVendorCategoryScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.VendorCategoryScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.couponSystem.user.recommendCoupons.RecommendCouponsScreen;
import com.teecoin.feature.general.popup.RequestLocationCheckInDialog;
import com.teecoin.feature.general.popup.RequestLocationPermissionDialog;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.reviewSystem.search.SearchVendorScreen;
import com.teecoin.feature.reviewSystem.searchLocation.DownloadImagesTask;
import com.teecoin.feature.reviewSystem.searchLocation.OnBitmapDownloadedListener;
import com.teecoin.feature.reviewSystem.searchLocation.SearchLocationScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.feature.walletSystem.paymentThreshold.YesNoListener;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.BitMapSelectModel;
import com.teecoin.model.couponsystem.BitMapUnSelectModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.FeatureExperienceModel;
import com.teecoin.model.couponsystem.QrCheckInModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.DiscoverUserGetCountryRequest;
import com.teecoin.myapi.apirequest.couponsystem.UserDiscoverGetRecommendCouponsRequest;
import com.teecoin.myapi.apirequest.couponsystem.UserDiscoverGetTopRatedExperienceRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetListCheckinRewardsRequest;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetArticlesRequest;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetListBannerRequest;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetRecommendForYouRequest;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetVendorCategoriesRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.ui.TCAutoScrollViewPager;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_NAME;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_CURRENCY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_SELECTED;
import static com.teecoin.utils.TCConstant.MAX_REQUEST_LOCATION_TIMES;

public class DiscoverScreen extends TCCouponBaseFragment implements StellarResponseListener, APIResponseListener, TCDecisionListener, OnBitmapDownloadedListener {
    private static final String USER_REVIEW_VENDOR_ID = "USER_REVIEW_VENDOR_ID";
    private static final String USER_REVIEW_GOTO_VENDOR_COUPON_LIST = "USER_REVIEW_GOTO_VENDOR_COUPON_LIST";
    @BindView(R.id.frag_discover_refresh_view)
    TCSwipeRefreshLayout refreshView;

    @BindView(R.id.frag_discover_nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.frag_discover_tv_country)
    TextView tv_country;

    @BindView(R.id.frag_discover_tv_balance)
    TextView tvBalance;

    @BindView(R.id.v_back_top_top)
    FrameLayout vBackToTop;

    @BindView(R.id.frag_discover_view_more_wallet)
    View vOpenWallet;

//    @BindView(R.id.frag_discover_iv_earn_tec)
//    ImageView iv_earn_tec;

    @BindView(R.id.frag_discover_v_daily_tec_give_away)
    View v_daily_tec_give_away;
    @BindView(R.id.view_circle_daily_tec_away_iv_gift)
    ImageView iv_gift_daily_tec;
    @BindView(R.id.view_circle_daily_tec_away_iv_close)
    ImageView iv_close_daily_tec;


    // 1. Discover
    @BindView(R.id.fragment_coupon_home_v_discover)
    View vDiscover;
    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcvDiscover;

    @BindView(R.id.frag_discover_v_checkin)
    View vCheckin;
    @BindView(R.id.frag_discover_tv_count_all_check_in)
    TextView tvCountAllCheckIn;
    @BindView(R.id.frag_discover_rcv_checkin)
    TCRecyclerView rcvCheckin;

    // banner
    @BindView(R.id.frag_discover_v_banner)
    View v_banner;
    @BindView(R.id.frag_discover_view_pager_banner)
    TCAutoScrollViewPager pagerBanner;
    @BindView(R.id.frag_discover_indicator_pager)
    CircleIndicatorPager indicator_pager;


    // feature experience
    @BindView(R.id.frag_discover_v_feature_experience)
    View v_feature_experience;

    @BindView(R.id.frag_discover_rcv_feature_experience)
    TCRecyclerView rcv_feature_experience;

    // 2. Recommend for you
    @BindView(R.id.frag_discover_v_recommend_for_you)
    View vRecommendForYou;
    @BindView(R.id.frag_discover_tv_count_all_recommended_for_you)
    TextView tvCountAllRecommendedForYou;
    @BindView(R.id.frag_discover_rcv_recommend_for_you)
    TCRecyclerView rcvRecommendForYou;

    // 3. Recommend coupon
    @BindView(R.id.frag_discover_v_recommend_coupons)
    View vRecommendCoupons;
    @BindView(R.id.frag_discover_tv_count_all_recommended_coupon)
    TextView tvCountAllRecommendedCoupon;
    @BindView(R.id.frag_discover_rcv_recommend_coupons)
    TCRecyclerView rcvRecommendCoupons;

    // 4. Top Rate
    @BindView(R.id.frag_discover_home_v_top_rate)
    View vTopRate;
    @BindView(R.id.frag_discover_tv_count_all_top_rate)
    TextView tvCountAllTopRate;
    @BindView(R.id.fragment_coupon_home_rcv_top_rate)
    TCRecyclerView rcvTopRate;

    @BindView(R.id.frag_discover_ll_check_in)
    View frag_discover_ll_check_in;


    private ArrayList<CountryCodeModel> listCountry;
    private CountryCodeModel currentCountryCode;
    private DiscoverVendorCatalogueImageAdapter categoryAdapter;
    private ArrayList<VendorCategoryModel> listCatalogueToSort;
    private ArrayList<VendorModel> listRecommendForYou;
    private ArrayList<VendorModel> listCheckin;
    private ArrayList<UserCouponCatalogueDataModel> listCouponAndCashVoucher;
    private ArrayList<VendorModel> listTopRate;
    private ArrayList<FeatureExperienceModel> featureExperienList;
    private ArrayList<DiscoverBannerModel> bannerList;

    private AccountModel accountModel;

    private DiscoverVendorAdapter checkinAdapter;
    private DiscoverVendorAdapter discoverVendorAdapter;
    private DiscoverVendorAdapter topRatedExperienceAdapter;
    private RecommendCouponsHorizontalAdapter recommendCouponsHorizontalAdapter;

    private boolean isStartCheckIn = false;

    public static DiscoverScreen getInstance() {
        return new DiscoverScreen();
    }

    public static DiscoverScreen getInstance(String vendorId, boolean isGotoVendorCouponList) {
        DiscoverScreen screen = new DiscoverScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_REVIEW_VENDOR_ID, vendorId);
        bundle.putSerializable(USER_REVIEW_GOTO_VENDOR_COUPON_LIST, isGotoVendorCouponList);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        showFooter();
        showTabMenuBottom();
        hideCancelNextBottomView();
        getLocationListener();

        //update when change to home screen
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        stellarGetBalance(accountModel.getPublic_key(), true, this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindView() {

        v_daily_tec_give_away.setVisibility(TCSharePreferenceManager.getInstance().getBoolean(DataKey.ShowDailyTecAway) ? View.VISIBLE : View.GONE);
        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));

        if (!TCUtils.isEmpty(accountModel.getBalance())) {
            setBalance(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, accountModel.getBalance()));
        }
        stellarGetBalance(accountModel.getPublic_key(), true, this);

        vDiscover.setVisibility(View.INVISIBLE);
        v_feature_experience.setVisibility(View.INVISIBLE);
        vCheckin.setVisibility(View.INVISIBLE);
        vRecommendForYou.setVisibility(View.INVISIBLE);
        vRecommendCoupons.setVisibility(View.INVISIBLE);
        vTopRate.setVisibility(View.INVISIBLE);
        vBackToTop.setVisibility(View.GONE);

        onViewClick();

        gotoVendorScreen();

        setupRecyclerView();

        refreshData();

        //update when change to home screen
        getEvents();
        ((TCMainActivity) getActiveActivity()).handleDeepLinkIntent(getActiveActivity().getIntent());
        checkDeviceTokenExists();

    }

    @Override
    public void onStellarSuccess(Object o) {
        if (o instanceof String) {
            String teeCoinAmount = (String) o;
            accountModel.setBalance(TCUtils.isEmpty(teeCoinAmount) ? "0" : teeCoinAmount);
            RealmController.getInstance().updateBalanceAccount(accountModel);
            setBalance(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, accountModel.getBalance()));
            ((TCMainActivity) getActiveActivity()).updateGoogleAnalyticUserProperty(accountModel);
        }
    }

    @Override
    public void onStellarFail(Throwable t) {
        setBalance(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, "0"));
    }

    public void setBalance(String balance) {
        tvBalance.setText(TCUtils.isEmpty(balance) ? "0" : balance + " " + TCUtils.getString(R.string.tee_coin_symbol));
    }

    private void setupRecyclerView() {

        pagerBanner.startAutoScroll();
        pagerBanner.setInterval(2000);
        pagerBanner.setCycle(true);
        pagerBanner.setStopScrollWhenTouch(true);

        listCountry = new ArrayList<>();

        listCatalogueToSort = new ArrayList<>();

        listCheckin = new ArrayList<>();

        listRecommendForYou = new ArrayList<>();

        listCouponAndCashVoucher = new ArrayList<>();

        listTopRate = new ArrayList<>();

        featureExperienList = new ArrayList<>();

        bannerList = new ArrayList<>();

        //1. discover/ categories
        categoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), listCatalogueToSort, (view, item, position, clickType) -> {
            categoryAdapter.unSelectedAllItem();
            categoryAdapter.setSelectedItem(position);
            addFragmentForResult(EnumMgr.RequestCode.GOTO_DETAIL_VENDOR_CATEGORY.getValue(), DetailVendorCategoryScreen.getInstance(item, EnumMgr.ParamToSortCondition.Discover.getValue(), item.getName(), null, false));
        });
        categoryAdapter.setAllSelector(true);
        rcvDiscover.setAdapter(categoryAdapter);

        //2.check in
        this.checkinAdapter = new DiscoverVendorAdapter(LayoutInflater.from(getActiveActivity()), listCheckin, true,
                (view, item, position, clickType) -> {
                    if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                        ImageView ivFavorite = (ImageView) view;
                        submitFavourite(position, item, (position12, vendorModel) ->
                                checkinAdapter.setStateIconFavorite(position12, ivFavorite, !vendorModel.isFavorite()));
                    } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                        getShareVendor(item);
                    } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                        writeReview(item);
                    } else {
                        addFragment(VendorScreen.getInstance(item.getId()));
                    }
                });
        rcvCheckin.setAdapter(checkinAdapter);

        FeatureExperienceAdapter featureExperience = new FeatureExperienceAdapter(LayoutInflater.from(getActiveActivity()), featureExperienList, new RecycleListener<FeatureExperienceModel>() {
            @Override
            public void onItemClick(View view, FeatureExperienceModel item, int position, EnumMgr.ClickType clickType) {
                addFragment(WebViewScreen.getInstance(item.getLink_post(), TCUtils.getString(R.string.featured_experience)));
            }
        });
        rcv_feature_experience.setAdapter(featureExperience);
        rcv_feature_experience.setNextPageIndex(1);
        rcv_feature_experience.addOnScrollListener(new TCRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (rcv_feature_experience.isCanLoadMore()) {
                        rcv_feature_experience.setNextPageIndex(rcv_feature_experience.getNextPageIndex() + 1);
                        getArticle();
                    }
                }
            }
        });
        //3.recommended for you
        this.discoverVendorAdapter = new DiscoverVendorAdapter(LayoutInflater.from(getActiveActivity()), listRecommendForYou, false,
                (view, item, position, clickType) -> {
                    if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                        ImageView ivFavorite = (ImageView) view;
                        submitFavourite(position, item, (position12, vendorModel) ->
                                discoverVendorAdapter.setStateIconFavorite(position12, ivFavorite, !vendorModel.isFavorite()));
                    } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                        getShareVendor(item);
                    } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                        writeReview(item);
                    } else {
                        addFragment(VendorScreen.getInstance(item.getId()));
                    }
                });
        rcvRecommendForYou.setAdapter(discoverVendorAdapter);

        //4. RecommendedCouponAndVouchers same coupon catalogue list
        recommendCouponsHorizontalAdapter =
                new RecommendCouponsHorizontalAdapter(LayoutInflater.from(getActiveActivity()),
                        listCouponAndCashVoucher,
                        true,
                        (view, item, position, clickType) -> {
                            addFragment(UserMyCouponDetailScreen.newInstance(item));
                        });
        rcvRecommendCoupons.setAdapter(recommendCouponsHorizontalAdapter);

        // 5. top rated experiences
        this.topRatedExperienceAdapter =
                new DiscoverVendorAdapter(LayoutInflater.from(getActiveActivity()),
                        listTopRate, false,
                        (view, item, position, clickType) ->
                        {
                            if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                                ImageView ivFavorite = (ImageView) view;
                                submitFavourite(position, item, (position1, vendorModel) -> topRatedExperienceAdapter.setStateIconFavorite(position1, ivFavorite, !vendorModel.isFavorite()));
                            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                                getShareVendor(item);
                            } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                                writeReview(item);
                            } else {
                                addFragment(VendorScreen.getInstance(item.getId()));
                            }
                        });
        rcvTopRate.setAdapter(topRatedExperienceAdapter);
        initData();
    }

    private void onViewClick() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        vBackToTop.setVisibility(View.VISIBLE);
                    } else {
                        vBackToTop.setVisibility(View.GONE);
                    }

                });

        registerSingleClick(R.id.frag_discover_rl_location, R.id.frag_discover_ll_balance,
                R.id.frag_discover_v_search, R.id.v_back_top_top,
                R.id.frag_discover_view_all_discover, R.id.frag_discover_view_all_recommend_for_you,
                R.id.frag_discover_v_all_recommend_coupons,
                R.id.frag_discover_v_all_rate, R.id.frag_discover_iv_earn_tec,
                R.id.frag_discover_ll_check_in, R.id.frag_discover_view_all_checkin, R.id.view_circle_daily_tec_away_iv_gift, R.id.view_circle_daily_tec_away_iv_close);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_discover_rl_location:
                openDialogCountry();
                break;
            case R.id.frag_discover_ll_balance:
                addFragment(WalletAccountScreen.getInstance());
                break;
            case R.id.frag_discover_v_search:
                addFragment(SearchVendorScreen.getInstance(EnumMgr.SearchType.SearchVendor.getValue()));
                break;
            case R.id.v_back_top_top:
                nestedScrollToTop(nestedScrollView);
                break;
            case R.id.frag_discover_view_all_discover:
                categoryAdapter.setAllSelector(false);
                addFragment(VendorCategoryScreen.getInstance(EnumMgr.ParamToSortCondition.Discover.getValue(),
                        TCUtils.getString(R.string.discover_experiences), "", false));
                break;
            case R.id.frag_discover_view_all_checkin:
                categoryAdapter.setAllSelector(false);
                addFragment(VendorCategoryScreen.getInstance(EnumMgr.ParamToSortCondition.Discover.getValue(),
                        TCUtils.getString(R.string.checkin_rewards), "", true));
                break;
            case R.id.frag_discover_view_all_recommend_for_you:
                categoryAdapter.setAllSelector(false);
                addFragment(VendorCategoryScreen.getInstance(EnumMgr.ParamToSortCondition.RecommendedForYou.getValue(),
                        TCUtils.getString(R.string.recommended_for_you), "", false));
                break;
            case R.id.frag_discover_v_all_recommend_coupons:
                categoryAdapter.setAllSelector(false);
                addFragment(RecommendCouponsScreen.getInstance());
                break;
            case R.id.frag_discover_v_all_rate:
                categoryAdapter.setAllSelector(false);
                addFragment(VendorCategoryScreen.getInstance(EnumMgr.ParamToSortCondition.TopRate.getValue(),
                        TCUtils.getString(R.string.top_rated_experiences), "", false));
                break;
            case R.id.floating_action_button:
                addFragment(SearchLocationScreen.getInstance());
                break;
            case R.id.frag_discover_iv_earn_tec:
                openHowtoEarnTECScreen();
                break;
            case R.id.frag_discover_ll_check_in:
                startCheckIn();
                break;
            case R.id.view_circle_daily_tec_away_iv_close:
                v_daily_tec_give_away.setVisibility(View.GONE);
                TCSharePreferenceManager.getInstance().setBoolean(DataKey.ShowDailyTecAway, false);
                break;
            case R.id.view_circle_daily_tec_away_iv_gift:
                addFragmentForResult(EnumMgr.RequestCode.GOTO_DAILY_REWARD.getValue(), DailyRewardScreen.getInstance());
                break;

        }
    }

    private void startCheckIn() {

        isStartCheckIn = true;

        if (ContextCompat.checkSelfPermission(getActiveActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            boolean alreadyDenied = shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (alreadyDenied) {
                int count = TCSharePreferenceManager.getInstance().getInt(DataKey.CountRequestLocationTime) + 1;
                if (count > MAX_REQUEST_LOCATION_TIMES) {
                    new RequestLocationCheckInDialog(getActiveActivity(), new YesNoListener() {
                        @Override
                        public void onSubmit() {
                            openSetting(true);
                        }

                        @Override
                        public void onCancel() {
                            //do nothing
                        }
                    }).show();
                } else {
                    requestPermissions(TCConstant.LOCATION_PERMISSIONS, EnumMgr.RequestCode.LOCATION.getValue());
                }
            } else {
                requestPermissions(TCConstant.LOCATION_PERMISSIONS, EnumMgr.RequestCode.LOCATION.getValue());
            }
        } else {
            gotoScanQRCode(EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue());
        }
    }

    public void initData() {
        requestApi(new UserDiscoverGetVendorCategoriesRequest(this));
        checkCurrentLocation();
    }

    public void getCountryCode(boolean reload) {
        if (reload) {
            requestApi(new DiscoverUserGetCountryRequest(this));
        } else {
            if (((TCMainActivity) getActiveActivity()).getListCountry() != null
                    && ((TCMainActivity) getActiveActivity()).getListCountry().size() > 0) {

                listCountry.addAll(((TCMainActivity) getActiveActivity()).getListCountry());

                tv_country.setText(getCountryCodeModel().getCountry_code());

                getData();

            } else {
                requestApi(new DiscoverUserGetCountryRequest(this));
            }
        }
    }

    private void getData() {

        nestedScrollView.setVisibility(View.GONE);
        if (featureExperienList.size() > 0) {

            v_feature_experience.setVisibility(View.GONE);

            featureExperienList.clear();
        }
        if (bannerList.size() > 0) {

            v_banner.setVisibility(View.GONE);

            bannerList.clear();
        }
        if (listCheckin.size() > 0) {

            vCheckin.setVisibility(View.GONE);

            listCheckin.clear();
        }
        if (listRecommendForYou.size() > 0) {

            vRecommendForYou.setVisibility(View.GONE);

            listRecommendForYou.clear();
        }

        if (listCouponAndCashVoucher.size() > 0) {

            vRecommendCoupons.setVisibility(View.GONE);

            listCouponAndCashVoucher.clear();
        }

        if (listTopRate.size() > 0) {

            listTopRate.clear();

            vTopRate.setVisibility(View.GONE);
        }

        getArticle();

        if (getCountryCodeModel() == null || TCUtils.isEmpty(getCountryCodeModel().getCountry_code())) {

            showBaseMessage(TCUtils.getString(R.string.error_country_code));

            return;
        }

        requestApi(new UserDiscoverGetListBannerRequest(getCountryCodeModel().getCountry_code(), this));

        requestApi(new GetListCheckinRewardsRequest(getLocation(), null, getCountryCodeModel().getCountry_code(), "", this));

        requestApi(new UserDiscoverGetRecommendForYouRequest(getLocation(), null, getCountryCodeModel().getCountry_code(), "", this));

        requestApi(new UserDiscoverGetRecommendCouponsRequest(getLocation(), getCountryCodeModel().getCountry_code(), this));

        requestApi(new UserDiscoverGetTopRatedExperienceRequest(getLocation(), getCountryCodeModel().getCountry_code(), this));

    }

    private void getArticle() {
        requestApi(new UserDiscoverGetArticlesRequest(rcv_feature_experience.getNextPageIndex(), getCountryCodeModel().getCountry_code(), this));
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
                    getLocationListener();
                    getCountryCode(false);
                }
            } else {
                requestPermissions(TCConstant.LOCATION_PERMISSIONS, EnumMgr.RequestCode.LOCATION.getValue());
                getCountryCode(false);
            }
        } else {
            //  getLocationListener();
            getCountryCode(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {

            if (isStartCheckIn && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startCheckIn();

                ((TCMainActivity) getActiveActivity()).setReloadDiscoverScreen();
                getLocationListener();
            }

            if (!isStartCheckIn) {
                ((TCMainActivity) getActiveActivity()).setReloadDiscoverScreen();
                getLocationListener();
            }
        }
        hideKeyBoard();
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.DISCOVER_BANNERS) {
            ArrayList<DiscoverBannerModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                bannerList.addAll(list);
                SlideBannerAdapter bannerAdapter = new SlideBannerAdapter(getActiveActivity(), bannerList);
                pagerBanner.setAdapter(bannerAdapter);
                indicator_pager.setViewPager(pagerBanner);
                v_banner.setVisibility(View.VISIBLE);
            } else {
                v_banner.setVisibility(View.GONE);
            }

//
        } else if (requestTarget == ReviewRequestTarget.GET_LIST_ARTICLES) {
            ArrayList<FeatureExperienceModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                featureExperienList.addAll(list);
                rcv_feature_experience.onLoadMoreComplete();
                rcv_feature_experience.setCanLoadMore(!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext()));
                v_feature_experience.setVisibility(View.VISIBLE);
            } else {
                v_feature_experience.setVisibility(View.GONE);
            }
        } else if (requestTarget == CouponRequestTarget.GET_LIST_COUNTRY) {
            ArrayList<CountryCodeModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            if (list != null && list.size() > 0) {
                listCountry.clear();
                listCountry.addAll(list);

                //currentCountryCode = TCUtils.checkCountryCode(listCountry, getLatLngCurrent());
                currentCountryCode = TCUtils.checkCountryCode(listCountry, getLocation());
                ((TCMainActivity) getActiveActivity()).setCountryCodeModel(currentCountryCode);

                for (int i = 0; i < listCountry.size(); i++) {

                    if (currentCountryCode.getCountry_code().toUpperCase().equals(listCountry.get(i).getCountry_code().toUpperCase())) {

                        listCountry.get(i).setSelected(true);
                    }
                }
            } else {

                //set default is SG - Singapore
                currentCountryCode = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, DEFAULT_SELECTED);

                if (listCountry.size() == 0) {

                    listCountry.add(currentCountryCode);
                }
            }

            ((TCMainActivity) getActiveActivity()).setListCountry(listCountry);

            ((TCMainActivity) getActiveActivity()).setCountryCodeModel();

            tv_country.setText(currentCountryCode.getCountry_code());

            //get data after get country code success
            getData();

        } else if (requestTarget == ReviewRequestTarget.VENDOR_CATEGORIES) {
            ArrayList<VendorCategoryModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listCatalogueToSort.addAll(list);
                rcvDiscover.onLoadMoreComplete();

                ((TCMainActivity) getActiveActivity()).setListCategoryToSort(listCatalogueToSort);

                //set list bitmap marker for map search

                ArrayList<BitMapSelectModel> listSelect = new ArrayList<>();

                for (int i = 0; i < listCatalogueToSort.size(); i++) {
                    int finalI = i;
                    new DownloadImagesTask(bmImage -> {
                        BitMapSelectModel model = new BitMapSelectModel(listCatalogueToSort.get(finalI).getId(), bmImage);
                        listSelect.add(model);
                        if (finalI == listCatalogueToSort.size() - 1) {
                            ((TCMainActivity) getActiveActivity()).setBitMapSelect(listSelect);
                        }
                    }).execute(listCatalogueToSort.get(i).getMap_marker_selected());
                }

                ArrayList<BitMapUnSelectModel> listUnSelect = new ArrayList<>();

                for (int i = 0; i < listCatalogueToSort.size(); i++) {
                    int finalI = i;
                    new DownloadImagesTask(bmImage -> {
                        BitMapUnSelectModel model = new BitMapUnSelectModel(listCatalogueToSort.get(finalI).getId(), bmImage);
                        listUnSelect.add(model);
                        if (finalI == listCatalogueToSort.size() - 1) {
                            ((TCMainActivity) getActiveActivity()).setBitMapUnSelect(listUnSelect);
                        }
                    }).execute(listCatalogueToSort.get(i).getMap_marker_unselected());
                }
            }
            vDiscover.setVisibility(listCatalogueToSort.size() > 0 ? View.VISIBLE : View.GONE);

        } else if (requestTarget == ReviewRequestTarget.GET_LIST_CHECKIN_REWARDS) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listCheckin.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                rcvCheckin.onLoadMoreComplete();
                vCheckin.setVisibility(View.VISIBLE);

                int count = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getCount();
                tvCountAllCheckIn.setText(String.format(TCUtils.getString(R.string.count_all_discover), count));
            } else {
                vCheckin.setVisibility(View.GONE);
            }

        } else if (requestTarget == ReviewRequestTarget.GET_RECOMMENDED_FOR_YOU) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listRecommendForYou.addAll(list.size() > TCConstant.LIST_DEFAULT_10 ? list.subList(0, TCConstant.LIST_DEFAULT_10) : list);
                rcvRecommendForYou.onLoadMoreComplete();
                vRecommendForYou.setVisibility(View.VISIBLE);

                int count = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getCount();
                tvCountAllRecommendedForYou.setText(String.format(TCUtils.getString(R.string.count_all_discover), count));

            } else {
                vRecommendForYou.setVisibility(View.GONE);
            }

        } else if (requestTarget == CouponRequestTarget.COUPON_RECOMMEND) {
            BaseResultsResponseModel<UserCouponCatalogueDataModel> catalogueResponseModel
                    = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult());
            ArrayList<UserCouponCatalogueDataModel> list = catalogueResponseModel.getResults();
            if (list != null && list.size() > 0) {
                listCouponAndCashVoucher.addAll(list);
                rcvRecommendCoupons.onLoadMoreComplete();

                int count = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult()).getCount();
                tvCountAllRecommendedCoupon.setText(String.format(TCUtils.getString(R.string.count_all_discover), count));

            }
            vRecommendCoupons.setVisibility(listCouponAndCashVoucher.size() > 0 ? View.VISIBLE : View.GONE);


        } else if (requestTarget == CouponRequestTarget.TOP_RATED_EXPERIENCES) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listTopRate.addAll(list);
                rcvTopRate.onLoadMoreComplete();

                int count = ((BaseResultsResponseModel) response.getResult()).getCount();
                tvCountAllTopRate.setText(String.format(TCUtils.getString(R.string.count_all_discover), count));
            }
            vTopRate.setVisibility(listTopRate.size() > 0 ? View.VISIBLE : View.GONE);
        }

        nestedScrollView.setVisibility(View.VISIBLE);

        refreshView.setRefreshing(false);

    }

    public void reloadPriceCoupon(){
        if (null != recommendCouponsHorizontalAdapter)
            recommendCouponsHorizontalAdapter.notifyDataSetChanged();
    }

    @Override
    public void setBitmap(Bitmap bmImage) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
//        super.onFail(errorModel, statusCode, requestTarget);
        if (requestTarget == CouponRequestTarget.GET_LIST_COUNTRY) {
            currentCountryCode = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, true);
            tv_country.setText(currentCountryCode.getCountry_code());
            if (listCountry.size() == 0) {
                listCountry.add(currentCountryCode);
            }
            if (((TCMainActivity) getActiveActivity()).getListCountry() == null)
                ((TCMainActivity) getActiveActivity()).setListCountry(listCountry);
            //set country code
            ((TCMainActivity) getActiveActivity()).setCountryCodeModel();
            //((TCMainActivity) getActiveActivity()).setCountryCodeModel(currentCountryCode);

            //get data after get country code fail (set default is SG - Singapore)
            getData();
        } else if (requestTarget == ReviewRequestTarget.VENDOR_CATEGORIES) {
            vDiscover.setVisibility(listCatalogueToSort.size() > 0 ? View.VISIBLE : View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_RECOMMENDED_FOR_YOU) {
            vRecommendForYou.setVisibility(View.GONE);
        } else if (requestTarget == CouponRequestTarget.COUPON_RECOMMEND) {
            vRecommendCoupons.setVisibility(listCouponAndCashVoucher.size() > 0 ? View.VISIBLE : View.GONE);
        } else if (requestTarget == CouponRequestTarget.TOP_RATED_EXPERIENCES) {
            vTopRate.setVisibility(listTopRate.size() > 0 ? View.VISIBLE : View.GONE);
        } else if (requestTarget == ReviewRequestTarget.GET_LIST_ARTICLES) {
            v_feature_experience.setVisibility(featureExperienList.size() > 0 ? View.VISIBLE : View.GONE);
        } else if (requestTarget == ReviewRequestTarget.DISCOVER_BANNERS) {
            v_banner.setVisibility(bannerList.size() > 0 ? View.VISIBLE : View.GONE);
        }
        nestedScrollView.setVisibility(View.VISIBLE);

        refreshView.setRefreshing(false);

    }

    @Override
    public void onPositiveButtonClicked(int id, Object onWhat) {
//        TCUtils.openAppSetting();
        openSetting(false);

    }

    private void openSetting(boolean isCheckIn) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActiveActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, isCheckIn ? EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE_CHECK_IN.getValue()
                : EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE.getValue());
    }

    @Override
    public void onNegativeButtonClicked(int id, Object onWhat) {
        TCUtils.saveCountRequestPermissionLocation(onWhat);
        getCountryCode(false);
    }

    @Override
    public void onNeutralButtonClicked(int id, Object onWhat) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE.getValue()) {
            initData();
        } else if (requestCode == EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE_CHECK_IN.getValue()) {
            startCheckIn();
        } else if (requestCode == EnumMgr.RequestCode.SCAN_COUPON_CODE_CHECK_IN.getValue()) {
            IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
            if (result != null && result.getContents() != null) {
                QrCheckInModel qrCheckInModel = new QrCheckInModel();
                if (qrCheckInModel.validate(result.getContents())) {
                    qrCheckInModel = TCUtils.convertToModel(result.getContents(), QrCheckInModel.class);
                    VendorCodeCheckInModel vendorCodeCheckInModel = new VendorCodeCheckInModel(qrCheckInModel.getVendor_code(),
                            String.valueOf(getLocation().latitude), String.valueOf(getLocation().longitude));
                    vendorCodeCheckInModel.setFromHomeCheckIn(true);
                    addFragment(UserCheckInResultScreen.getInstance(vendorCodeCheckInModel, false));
                }
            }
        }
    }

    private void openDialogCountry() {

        if (listCountry.size() > 0) {

            SelectCountryForDiscoverDialog dialog = new SelectCountryForDiscoverDialog(getActiveActivity(),
                    listCountry, getCountryCodeModel(),
                    country -> {
                        ((TCMainActivity) getActiveActivity()).setCountryCodeModel(country);
                        tv_country.setText(getCountryCodeModel().getCountry_code());
                        getData();
                    });
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_discover_rl_location, R.id.frag_discover_view_more_wallet,
                R.id.frag_discover_v_search, R.id.v_back_top_top,
                R.id.frag_discover_view_all_discover, R.id.frag_discover_view_all_recommend_for_you,
                R.id.frag_discover_v_all_recommend_coupons, R.id.frag_discover_v_all_rate,
                R.id.floating_action_button, R.id.frag_discover_iv_earn_tec, R.id.frag_discover_view_all_checkin, R.id.view_circle_daily_tec_away_iv_gift, R.id.view_circle_daily_tec_away_iv_close);
    }

    private void refreshData() {
        refreshView.setOnRefreshListener(() -> {
            getData();
        });
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        super.onPostResumeWithResult(requestCode, finishedResultCode, finishedResult);
        if (requestCode == EnumMgr.RequestCode.GOTO_DETAIL_VENDOR_CATEGORY.getValue() && finishedResultCode == RESULT_OK) {
            if (categoryAdapter != null)
                categoryAdapter.unSelectedAllItem();
        } else if (requestCode == EnumMgr.RequestCode.GOTO_DAILY_REWARD.getValue() && finishedResultCode == RESULT_OK) {
            accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
            setBalance(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, accountModel.getBalance()));

        }
    }

    private void gotoVendorScreen() {
        if (getArguments() != null) {
            if (!TCUtils.isEmpty(getArguments().getString(USER_REVIEW_VENDOR_ID))) {
                gotoVendorDetailScreen(getArguments().getString(USER_REVIEW_VENDOR_ID),
                        getArguments().getBoolean(USER_REVIEW_GOTO_VENDOR_COUPON_LIST));
            }
        }
    }

}
