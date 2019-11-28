package com.teecoin.feature.couponSystem.user.coupon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.feature.couponSystem.user.couponCatalogue.UserCouponCatalogueScreen;
import com.teecoin.feature.couponSystem.user.myCoupon.UserMyCouponScreen;
import com.teecoin.feature.couponSystem.user.redeemedExpired.RedeemedExpiredScreen;
import com.teecoin.feature.couponSystem.user.searchCoupon.SearchCouponScreen;
import com.teecoin.feature.general.popup.RequestLocationPermissionDialog;
import com.teecoin.feature.reviewSystem.vendor.ViewPagerAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetVendorCategoriesRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

import static com.teecoin.utils.TCConstant.MAX_REQUEST_LOCATION_TIMES;

public class UserCouponScreen extends TCCouponBaseFragment implements APIResponseListener, TCDecisionListener, StellarResponseListener {
    private static final String USER_COUPON_GOTO_CATEGORY_COUPON_ID = "USER_COUPON_GOTO_CATEGORY_COUPON_ID";
    private static final String USER_REVIEW_VENDOR_ID = "USER_REVIEW_VENDOR_ID";
    private static final String USER_REVIEW_GOTO_VENDOR_COUPON_LIST = "USER_REVIEW_GOTO_VENDOR_COUPON_LIST";

    @BindView(R.id.frg_view_pager_rl_toolbar)
    View ll_toolbar;
    @BindView(R.id.view_coupon_user_toolbar_coupon_ll_redeem_expired)
    View ll_redeem_expired;
    @BindView(R.id.view_coupon_user_toolbar_coupon_ll_search)
    View ll_search;
    @BindView(R.id.frg_view_pager_tab_layout)
    TabLayout tabs;
    @BindView(R.id.frg_view_pager_view_pager)
    ViewPager view_pager;
    @BindView(R.id.view_tab_ll_my_coupon)
    View tab_my_coupon;
    @BindView(R.id.view_tab_ll_catalogue)
    View tab_category;
    @BindView(R.id.frg_view_pager_rl_add_for_shop)
    View rl_add_for_shop;
    @BindView(R.id.view_tab_iv_my_coupon)
    ImageView iv_my_coupon;
    @BindView(R.id.view_tab_tv_my_coupon)
    TextView tv_my_coupon;
    @BindView(R.id.view_tab_iv_catalogue)
    ImageView iv_category;
    @BindView(R.id.view_tab_tv_catalogue)
    TextView tv_category;

    private ViewPagerAdapter viewPagerAdapter;
    private String categoryCouponId;

    public static UserCouponScreen getInstance() {
        return new UserCouponScreen();
    }

    public static UserCouponScreen getInstance(String categoryCouponId) {
        UserCouponScreen screen = new UserCouponScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_COUPON_GOTO_CATEGORY_COUPON_ID, categoryCouponId);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserCouponScreen getInstance(String vendorId, boolean isGotoVendorCouponList) {
        UserCouponScreen screen = new UserCouponScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_REVIEW_VENDOR_ID, vendorId);
        bundle.putSerializable(USER_REVIEW_GOTO_VENDOR_COUPON_LIST, isGotoVendorCouponList);
        screen.setArguments(bundle);
        return screen;
    }


    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        showFooter();
        showTabMenuBottom();
        rl_add_for_shop.setVisibility(View.GONE);
        ll_toolbar.setVisibility(View.VISIBLE);
        reloadDataMyCoupon();

        //update when change to home screen
//      AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
//      stellarGetBalance(accountModel.getPublic_key(), true, this);
    }

    @Override
    public void onBindView() {

        tab_my_coupon.setVisibility(View.VISIBLE);
        tab_category.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            categoryCouponId = getArguments().getString(USER_COUPON_GOTO_CATEGORY_COUPON_ID);
        }
        hideButtonBackToolbar();
        setupTabMenu();
        onClick();

        requestApi(new UserDiscoverGetVendorCategoriesRequest(this));
        checkCurrentLocation();
        gotoVendorScreen();

        //update when change to home screen
//      getEvents();
//      ((TCMainActivity) getActiveActivity()).handleDeepLinkIntent(getActiveActivity().getIntent());
//      checkDeviceTokenExists();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    private void setupTabMenu() {
        tabs.setupWithViewPager(view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(UserCouponCatalogueScreen.newInstance(categoryCouponId), "");
        viewPagerAdapter.addFragment(UserMyCouponScreen.newInstance(), "");
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.setOffscreenPageLimit(2);
        activeTabCatalogue();
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                unSelectTab();
                if (position == 0) {
                    activeTabCatalogue();
                } else {
                    activeTabMyCoupon();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onClick() {

        registerSingleClick(R.id.view_coupon_user_toolbar_coupon_ll_redeem_expired, R.id.view_coupon_user_toolbar_coupon_ll_search,
                R.id.view_circle_how_to_earn_iv_goto, R.id.view_circle_how_to_earn_iv_close);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_coupon_user_toolbar_coupon_ll_redeem_expired:
                addFragment(RedeemedExpiredScreen.getInstance());
                break;
            case R.id.view_coupon_user_toolbar_coupon_ll_search:
                addFragment(SearchCouponScreen.getInstance());
                //  addFragmentForResult(EnumMgr.RequestCode.GOTO_SEARCH_COUPON.getValue(), SearchVendorScreen.getInstance(EnumMgr.SearchType.SearchCoupon.getValue()));
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.view_coupon_user_toolbar_coupon_ll_redeem_expired, R.id.view_coupon_user_toolbar_coupon_ll_search);
    }


    private void unSelectTab() {
        iv_my_coupon.setSelected(false);
        iv_category.setSelected(false);
        tv_category.setSelected(false);
        tv_my_coupon.setSelected(false);
    }

    private void activeTabMyCoupon() {
        iv_my_coupon.setSelected(true);
        tv_my_coupon.setSelected(true);
    }

    private void activeTabCatalogue() {
        iv_category.setSelected(true);
        tv_category.setSelected(true);
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (view_pager != null && viewPagerAdapter != null) {
            ((TCBaseFragment) viewPagerAdapter.getItem(view_pager.getCurrentItem()))
                    .onPostResumeWithResult(requestCode, finishedResultCode, finishedResult);
        }
        if (requestCode == EnumMgr.RequestCode.GOTO_SEARCH_COUPON.getValue()) {
            if (finishedResult != null && finishedResult.getExtras() != null) {
                Bundle bundle = finishedResult.getExtras();
                String keyword = bundle.getString(TCConstant.KEYWORD);
                reloadCatalogueCoupon(keyword);
            }
        }
    }

    private void reloadDataMyCoupon() {
        if (viewPagerAdapter != null) {
            UserMyCouponScreen userMyCouponScreen = (UserMyCouponScreen)
                    viewPagerAdapter.getFragment(UserMyCouponScreen.class.getCanonicalName());
            if (userMyCouponScreen != null) {
                userMyCouponScreen.reloadData();
            }
        }
    }

    private void reloadCatalogueCoupon(String keyword) {
        if (viewPagerAdapter != null) {
            UserCouponCatalogueScreen screen = (UserCouponCatalogueScreen)
                    viewPagerAdapter.getFragment(UserCouponCatalogueScreen.class.getCanonicalName());
            if (screen != null) {
                screen.getData(keyword);
            }
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.VENDOR_CATEGORIES) {
            ArrayList<VendorCategoryModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                ((TCMainActivity) getActiveActivity()).setListCategoryToSort(list);
                if (viewPagerAdapter != null && viewPagerAdapter.getItem(0) != null) {
                    UserCouponCatalogueScreen screen = (UserCouponCatalogueScreen) viewPagerAdapter.getItem(0);
                    screen.setupVendorCategory();

                }
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
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
                    ((TCMainActivity) getActiveActivity()).getCountryCode();
                }
            } else {
                requestPermissions(TCConstant.LOCATION_PERMISSIONS, EnumMgr.RequestCode.LOCATION.getValue());
            }
        } else {
            ((TCMainActivity) getActiveActivity()).getCountryCode();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        ((TCMainActivity) getActiveActivity()).setReloadDiscoverScreen();
        getLocationListener();
        hideKeyBoard();
    }

    @Override
    public void onPositiveButtonClicked(int id, Object onWhat) {

    }

    @Override
    public void onNeutralButtonClicked(int id, Object onWhat) {

    }

    @Override
    public void onNegativeButtonClicked(int id, Object onWhat) {
        TCUtils.saveCountRequestPermissionLocation(onWhat);
    }

    @Override
    public void onStellarSuccess(Object o) {
        if (o instanceof String) {
            String teeCoinAmount = (String) o;
            AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
            accountModel.setBalance(TCUtils.isEmpty(teeCoinAmount) ? "0" : teeCoinAmount);
            RealmController.getInstance().updateBalanceAccount(accountModel);
            ((TCMainActivity) getActiveActivity()).updateGoogleAnalyticUserProperty(accountModel);
        }
    }

    @Override
    public void onStellarFail(Throwable t) {

    }

    private void gotoVendorScreen() {
        if (getArguments() != null) {
            if (!TCUtils.isEmpty(getArguments().getString(USER_REVIEW_VENDOR_ID))) {
                gotoVendorDetailScreen(getArguments().getString(USER_REVIEW_VENDOR_ID),
                        getArguments().getBoolean(USER_REVIEW_GOTO_VENDOR_COUPON_LIST));
            }
        }
    }

    public void reloadPriceCoupon() {
        if (view_pager.getCurrentItem() == 0) {
            UserCouponCatalogueScreen userCouponCatalogueScreen = (UserCouponCatalogueScreen)
                    viewPagerAdapter.getFragment(UserCouponCatalogueScreen.class.getCanonicalName());
            if (null != userCouponCatalogueScreen) {
                userCouponCatalogueScreen.reloadPriceCoupon();
            }
        }
    }
}
