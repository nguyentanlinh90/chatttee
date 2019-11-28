package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.catalogues.FilterCatalogueAdapter;
import com.teecoin.feature.couponSystem.user.catalogues.UserCatalogueListScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.DisCoverVendorCatalogueNameAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCataloguesRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class UserCouponCatalogueScreen extends TCCouponBaseFragment implements APIResponseListener, AppBarLayout.OnOffsetChangedListener {

    private static final String USER_COUPON_GOTO_CATEGORY_COUPON_ID = "USER_COUPON_GOTO_CATEGORY_COUPON_ID";

    @BindView(R.id.frg_coupon_user_catalogue_refresh_view)
    TCSwipeRefreshLayout refreshView;

    @BindView(R.id.frg_coupon_user_catalogue_rcv_filter)
    TCRecyclerView rcv_filter;

    @BindView(R.id.frg_coupon_user_catalogue_appbar_layout)
    AppBarLayout appBar;

    @BindView(R.id.frag_coupon_user_catalogue_ncv_catalogue)
    NestedScrollView scrollView;

    @BindView(R.id.view_coupon_user_catalogue_horizontal_list_rcv_catalogue_list)
    TCRecyclerView rcvCategoryCoupon;

    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcvCatalogueImage;

    @BindView(R.id.frg_coupon_user_catalogue_rcv_category_name)
    TCRecyclerView rcv_category_name;

    @BindView(R.id.frg_coupon_user_catalogue_view_category_name)
    View view_category_name;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;

    @BindView(R.id.view_coupon_user_view_how_to_earn)
    View view_how_to_earn;
    @BindView(R.id.view_circle_how_to_earn_iv_goto)
    ImageView iv_how_to_earn;

    @BindView(R.id.view_circle_how_to_earn_iv_icon)
    ImageView iv_icon;

    @BindView(R.id.view_circle_how_to_earn_iv_close)
    ImageView iv_close_how_to_earn;


    private ArrayList<UserCouponCatalogueModel> listCoupon;

    private ArrayList<VendorCategoryModel> vendorCategoryList;

    private DiscoverVendorCatalogueImageAdapter vendorCategoryAdapter;
    private DisCoverVendorCatalogueNameAdapter categoryNameAdapter;

    private FilterCatalogueAdapter filterAdapter;

    private UserCouponCatalogueHorizontalAdapter adapterCoupon;

    private String TAG_TYPE_COUPON = "";

    private String keyword = "";
    private String categoryCouponId;

    public static UserCouponCatalogueScreen newInstance() {
        return new UserCouponCatalogueScreen();
    }

    public static UserCouponCatalogueScreen newInstance(String categoryCouponId) {
        UserCouponCatalogueScreen screen = new UserCouponCatalogueScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_COUPON_GOTO_CATEGORY_COUPON_ID, categoryCouponId);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_user_catalogue, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        appBar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        appBar.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onBindView() {
        setupRecycler();

        refreshData();
        registerSingleClick(
                R.id.view_circle_how_to_earn_iv_goto, R.id.view_circle_how_to_earn_iv_close);

        view_how_to_earn.setVisibility(TCSharePreferenceManager.getInstance().getBoolean(DataKey.ShowHowToEarnTec) ? View.VISIBLE : View.GONE);

        view_category_name.setAlpha(0);
        TAG_TYPE_COUPON = EnumMgr.SortByTypeCoupon.Coupons.getValue();
        if (getArguments() != null) {
            categoryCouponId = getArguments().getString(USER_COUPON_GOTO_CATEGORY_COUPON_ID);
        }
        initHowToEanTEC();
    }

    public void setupRecycler() {
        refreshView.setEnabled(false);
        listCoupon = new ArrayList<>();
        vendorCategoryList = new ArrayList<>();
        adapterCoupon = new UserCouponCatalogueHorizontalAdapter(getActiveActivity(), LayoutInflater.from(getActiveActivity()),
                listCoupon, true,
                (view, item, position, clickType) -> {
                    if (clickType.equals(EnumMgr.ClickType.ViewAll)) {
                        VendorCategoryModel vendorCategoryModel = new VendorCategoryModel(item.getId(), item.getName(), true);
                        filterCategory(vendorCategoryModel);
                    }
                },
                (view, item, position, clickType) -> {
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(), UserMyCouponDetailScreen.newInstance(item));
                });

        rcvCategoryCoupon.setAdapter(adapterCoupon);

        rcv_filter.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new FilterCatalogueAdapter(LayoutInflater.from(getActiveActivity()), ((TCMainActivity) getActiveActivity()).getListFilterCatalogue(), (view, item, position, clickType) -> {
            if (item.isSelector())
                return;
            filterAdapter.updateSelector(item);
            ((TCMainActivity) getActiveActivity()).updateListFilter(item);
            TAG_TYPE_COUPON = item.getId();
            listCoupon.clear();
            //rcvCategoryCoupon.setNextPageIndex(1);
            getData("");

            adapterCoupon.setTypeSort(TAG_TYPE_COUPON);
        });
        rcv_filter.setAdapter(filterAdapter);

        setupVendorCategory();
        getData("");

    }


    public void getData(String keyword) {
        this.keyword = keyword;
        requestApi(new CouponUserGetCouponCataloguesRequest(TCUtils.paramsGetCategoryCoupon(getLocation(), getCountryCodeModel().getCountry_code(), "", keyword, TAG_TYPE_COUPON), this));
        //requestApi(new CouponUserGetCouponCataloguesRequest(TCUtils.paramsGetCategoryCoupon(getLocation(), getCountryCodeModel().getCountry_code(), "", keyword), this));
    }

    private void refreshData() {
        refreshView.setOnRefreshListener(() -> {
            scrollView.setVisibility(View.GONE);
            // getData("");
            getData(keyword);

        });
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_circle_how_to_earn_iv_goto:
                openHowtoEarnTECScreen();
                break;
            case R.id.view_circle_how_to_earn_iv_close:
                view_how_to_earn.setVisibility(View.GONE);
                TCSharePreferenceManager.getInstance().setBoolean(DataKey.ShowHowToEarnTec, false);
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(
                R.id.view_circle_how_to_earn_iv_goto, R.id.view_circle_how_to_earn_iv_close);
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue()
                || requestCode == EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue()
                && finishedResultCode == RESULT_OK) {
            categoryCouponId = null;//clear categoryCoupon so that gotoCatalogueList() will not go to UserCatalogueListScreen
            setupRecycler();
            if (vendorCategoryAdapter != null)
                vendorCategoryAdapter.unSelectedAllItem();
        }
    }

    private void gotoCatalogueList() {
        if (listCoupon != null && listCoupon.size() > 0 && !TCUtils.isEmpty(categoryCouponId)) {
            for (UserCouponCatalogueModel item : listCoupon) {
                if (item.getId().equals(categoryCouponId)) {
                    VendorCategoryModel vendorCategoryModel = new VendorCategoryModel(item.getId(), item.getName(), true);
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(),
                            UserCatalogueListScreen.getInstance(vendorCategoryList, vendorCategoryModel, 2, TAG_TYPE_COUPON));
                }
            }
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUES) {
            ArrayList<UserCouponCatalogueModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                if (listCoupon.size() > 0) listCoupon.clear();
                for (UserCouponCatalogueModel catalogueModel : list) {
                    if (catalogueModel.getCatalogues() != null && catalogueModel.getCatalogues().size() > 0) {
                        listCoupon.add(catalogueModel);
                    }
                }
                showData(true);
                rcvCategoryCoupon.onLoadMoreComplete();
                gotoCatalogueList();
            } else {
                showData(false);
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUES) {
            showData(false);
        }
    }

    private void filterCategory(VendorCategoryModel vendorCategoryModel) {

        if (vendorCategoryModel == null && vendorCategoryList.size() == 0)
            return;
        vendorCategoryAdapter.setAllSelector(false);
        for (VendorCategoryModel couponCategoryModel : vendorCategoryList) {
            if (couponCategoryModel.getId().equals(vendorCategoryModel.getId())) {
                couponCategoryModel.setSelector(true);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, vendorCategoryModel, 2, TAG_TYPE_COUPON));
                return;
            }
        }
    }

    private void showData(boolean isShow) {
        refreshView.setEnabled(true);
        refreshView.setRefreshing(false);
        scrollView.setVisibility(View.VISIBLE);
        rcvCategoryCoupon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNoData.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        handelScrollingCategory(appBarLayout, verticalOffset, rcvCatalogueImage, view_category_name);
    }

    public void setupVendorCategory() {
        if (null != getListCategorySort()) {
            vendorCategoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), vendorCategoryList, (view, item, position, clickType) -> {
                vendorCategoryAdapter.unSelectedAllItem();
                vendorCategoryAdapter.setSelectedItem(position);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, item, 2, TAG_TYPE_COUPON));
            });

            categoryNameAdapter = new DisCoverVendorCatalogueNameAdapter(LayoutInflater.from(getActiveActivity()), vendorCategoryList, (view, item, position, clickType) -> {
                categoryNameAdapter.unSelectedAllItem();
                categoryNameAdapter.setSelectedItem(position);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, item, 2, TAG_TYPE_COUPON));

            });

            rcvCatalogueImage.setAdapter(vendorCategoryAdapter);
            rcv_category_name.setAdapter(categoryNameAdapter);
            if (vendorCategoryList != null && vendorCategoryList.size() > 0) {
                vendorCategoryList.clear();
            }
            vendorCategoryList.addAll(getListCategorySort());
            rcvCatalogueImage.onLoadMoreComplete();
            rcv_category_name.onLoadMoreComplete();
        }
    }

    private void initHowToEanTEC() {
        if (getFeeConfig() != null) {
            if (!TCUtils.isEmpty(getFeeConfig().getUrlFloating()) && !TCUtils.isEmpty(getFeeConfig().getImageFloating())) {
                iv_icon.setVisibility(View.VISIBLE);
                iv_how_to_earn.setVisibility(View.GONE);
                Glide.with(getActiveActivity()).load(getFeeConfig().getImageFloating()).into(iv_icon);
                iv_icon.setOnClickListener(v -> addFragment(WebViewScreen.getInstance(getFeeConfig().getUrlFloating(), getFeeConfig().getTitleFloating())));
            }
        }
    }

    public void reloadPriceCoupon() {
        if (null != adapterCoupon)
            adapterCoupon.setPrice();
    }
}
