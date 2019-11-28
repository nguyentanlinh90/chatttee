package com.teecoin.feature.couponSystem.user.catalogues;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponCatalogue.UserCouponCatalogueVerticalAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.DisCoverVendorCatalogueNameAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponItem;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.UserFilterCouponDialog;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.reviewSystem.search.SearchVendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCatalogueListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.paramsToGetCategory;


public class UserCatalogueListScreen extends TCCouponBaseFragment implements AppBarLayout.OnOffsetChangedListener {
    private static final String FROM_COUPON = "FROM_COUPON";
    private static final String COUPON_CATALOGUE_MODEL = "CouponCatalogueModel";
    private static final String USER_COUPON_ITEM_CATALOGUE = "UserCouponItemCatalogueModel";
    public static final String SEARCH_OPTION = "SEARCH_OPTION";
    public static final String TYPE_FILTER = "TYPE_FILTER";

    @BindView(R.id.frg_coupon_user_list_catalogue_refresh_view)
    TCSwipeRefreshLayout refresh_view;

    @BindView(R.id.frg_coupon_user_catalogue_list_appbar_layout)
    AppBarLayout appBar;

    @BindView(R.id.view_filter_number_rl_filter)
    View vFilter;
    @BindView(R.id.view_filter_number_tv_num_filter)
    TextView tvNumFilter;


    @BindView(R.id.view_list_catalogue_rcv_filter)
    TCRecyclerView rcv_filter;

    @BindView(R.id.frg_coupon_user_catalogue_list_view_category_name)
    View view_category_name;


    @BindView(R.id.frg_coupon_user_catalogue_list_rcv_category_name)
    TCRecyclerView rcv_category_name;

    @BindView(R.id.item_coupon_user_catalogue_rcv_vertical)
    TCRecyclerView rcv_catalogue;

    @BindView(R.id.view_no_data_tv)
    View tv_no_data;

    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcv_catalogue_image;

    @BindView(R.id.view_list_catalogue_tv_catalogue)
    TextView tv_catalogue;

    @BindView(R.id.frg_coupon_user_catalogue_list_tv_title)
    TextView frg_coupon_user_catalogue_list_tv_title;


    @BindView(R.id.frg_coupon_user_catalogue_list_nested_scroll)
    NestedScrollView nestedScrollView;

    private FilterCatalogueAdapter filterAdapter;

    private VendorCategoryModel vendorCategoryModel;
    private UserCouponCatalogueVerticalAdapter adapter;
    private ArrayList<UserCouponCatalogueDataModel> listItemCatalogue;
    private ArrayList<VendorCategoryModel> listCatalogueToSort;

    private FilterCouponModel filterCouponModel;
    private DiscoverVendorCatalogueImageAdapter categoryImageAdapter;
    private DisCoverVendorCatalogueNameAdapter categoryNameAdapter;
    private String TAG_TYPE_COUPON = EnumMgr.SortByTypeCoupon.Coupons.getValue();
    private int fromCoupon;
    private ArrayList<FilterCouponItem> listFilter;

    public static UserCatalogueListScreen getInstance(ArrayList<VendorCategoryModel> listCatalogueToSort, VendorCategoryModel data, int fromCoupon,String typeFilter) {
        UserCatalogueListScreen screen = new UserCatalogueListScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COUPON_CATALOGUE_MODEL, listCatalogueToSort);
        bundle.putSerializable(USER_COUPON_ITEM_CATALOGUE, data);
        bundle.putSerializable(FROM_COUPON, fromCoupon);
        bundle.putString(TYPE_FILTER, typeFilter);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserCatalogueListScreen getInstance(UserCouponCatalogueModel data) {
        UserCatalogueListScreen screen = new UserCatalogueListScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_COUPON_ITEM_CATALOGUE, data);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_user_catalogues_list, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showFooter();
        hideHeader();
        showButtonBackToolbar();
        appBar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onBindView() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            listCatalogueToSort = (ArrayList<VendorCategoryModel>) bundle.getSerializable(COUPON_CATALOGUE_MODEL);
            vendorCategoryModel = (VendorCategoryModel) bundle.getSerializable(USER_COUPON_ITEM_CATALOGUE);
            fromCoupon = bundle.getInt(FROM_COUPON);
            TAG_TYPE_COUPON = bundle.getString(TYPE_FILTER);
            if (vendorCategoryModel != null) {
                updateCatalogueTitle(vendorCategoryModel.getName());
            }
        }
        vFilter.setVisibility(View.VISIBLE);

        listFilter = new ArrayList<>();
        filterCouponModel = new FilterCouponModel();
        if (fromCoupon == EnumMgr.FilterCoupons.Discover.getValue()) {
            listFilter.add(new FilterCouponItem(FilterCouponItem.RECOMMENDED_COUPON));
        } else {
            listFilter.add(new FilterCouponItem(FilterCouponItem.POPULAR));
        }
        filterCouponModel = new FilterCouponModel(listFilter);
        setNumberFilter();
        setupRecycler();
        onClick();
        refreshData();
    }

    @Override
    public void onStop() {
        super.onStop();
        appBar.removeOnOffsetChangedListener(this);
    }

    private void setupRecycler() {
        rcv_catalogue.setNestedScrollingEnabled(false);
        rcv_catalogue.setLayoutManager(new GridLayoutManager(getActiveActivity(), 2, GridLayout.VERTICAL, false));
      //  rcv_catalogue.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        listItemCatalogue = new ArrayList<>();
        adapter = new UserCouponCatalogueVerticalAdapter(LayoutInflater.from(getActiveActivity()), listItemCatalogue, true,false,
                (view, item, position, clickType) -> UserCatalogueListScreen.this.addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(), UserMyCouponDetailScreen.newInstance(item)));
        rcv_catalogue.setAdapter(adapter);

        rcv_catalogue.setNextPageIndex(1);// userCouponCatalogueModel.getCatalogues() contain 1st page data
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                if (!rcv_catalogue.isCanLoadMore() || rcv_catalogue.isLoading()){
                    return;
                }

                if (scrollY >= ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) * 2 / 3) {

                    getData(filterCouponModel, false, false);
                }
            }
        });

        rcv_filter.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new FilterCatalogueAdapter(LayoutInflater.from(getActiveActivity()), ((TCMainActivity)getActiveActivity()).getListFilterCatalogue(), (view, item, position, clickType) -> {
            if (item.isSelector())
                return;
            filterAdapter.updateSelector(item);
            ((TCMainActivity)getActiveActivity()).updateListFilter(item);
            TAG_TYPE_COUPON = item.getId();
            listItemCatalogue.clear();
            rcv_catalogue.setNextPageIndex(1);
            getData(filterCouponModel, true, true);
        });
        rcv_filter.setAdapter(filterAdapter);

        categoryImageAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), listCatalogueToSort, (view, item, position, clickType) -> {
            if (item.isSelector())
                return;
            categoryImageAdapter.unSelectedAllItem();
            categoryImageAdapter.setSelectedItem(position);
            categoryNameAdapter.notifyDataSetChanged();

            rcv_category_name.scrollToPosition(position);

            listItemCatalogue.clear();
            vendorCategoryModel = item;
            updateCatalogueTitle(vendorCategoryModel.getName());
            rcv_catalogue.setNextPageIndex(1);
            getData(filterCouponModel, true, true);
        });

        rcv_catalogue_image.setAdapter(categoryImageAdapter);

        categoryNameAdapter = new DisCoverVendorCatalogueNameAdapter(LayoutInflater.from(getActiveActivity()), listCatalogueToSort, (view, item, position, clickType) -> {
            if (item.isSelector())
                return;
            categoryNameAdapter.unSelectedAllItem();
            categoryNameAdapter.setSelectedItem(position);
            categoryImageAdapter.notifyDataSetChanged();

            rcv_catalogue_image.scrollToPosition(position);

            listItemCatalogue.clear();
            vendorCategoryModel = item;
            updateCatalogueTitle(vendorCategoryModel.getName());
            rcv_catalogue.setNextPageIndex(1);
            getData(filterCouponModel, true, true);
        });
        rcv_category_name.setAdapter(categoryNameAdapter);

        if (listCatalogueToSort != null && listCatalogueToSort.size() > 0) {
            for (int i = 0; i < listCatalogueToSort.size(); i++) {
                if (listCatalogueToSort.get(i).isSelector()) {
                    rcv_catalogue_image.scrollToPosition(i);
                    rcv_category_name.scrollToPosition(i);
                }
            }
        }

        getData(filterCouponModel, false, true);
    }

    private void onClick() {
        //rcv_category_name.setAlpha( 0);
        view_category_name.setAlpha(0);
        registerSingleClick(R.id.frg_coupon_user_catalogue_list_iv_back, R.id.view_filter_number_rl_filter, R.id.frg_coupon_user_catalogue_list_tv_title);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_coupon_user_catalogue_list_iv_back:
                finishWithResult(RESULT_OK, new Intent());
                break;
            case R.id.view_filter_number_rl_filter:
                if (listCatalogueToSort != null)
                    openFilterCatalogue(listCatalogueToSort, vendorCategoryModel);
                break;
            case R.id.frg_coupon_user_catalogue_list_tv_title:
                addFragmentForResult(EnumMgr.RequestCode.GOTO_SEARCH_COUPON.getValue(), SearchVendorScreen.getInstance(EnumMgr.SearchType.SearchCouponByCatalogue.getValue()));
                break;
        }
    }
    public void openFilterCatalogue(ArrayList<VendorCategoryModel> listCatalogue, VendorCategoryModel catalogueSelect) {
        UserFilterCouponDialog filterCouponDialog = new UserFilterCouponDialog(filterCouponModel, listCatalogue, catalogueSelect, (listCatalogue1, filterCouponModel) -> {
            listCatalogueToSort = new ArrayList<>();
            listCatalogueToSort.addAll(listCatalogue1);
            for (VendorCategoryModel categoryModel : listCatalogueToSort) {
                if (categoryModel.isSelector())
                    vendorCategoryModel = categoryModel;
            }
            if (vendorCategoryModel != null) {
                updateCatalogueTitle(vendorCategoryModel.getName());
            }
            rcv_catalogue_image.onLoadMoreComplete();
            rcv_catalogue.setNextPageIndex(1);
            getData(filterCouponModel, true, true);
            setNumberFilter();

        });
        filterCouponDialog.show(getActiveActivity().getSupportFragmentManager(), "FilterCatalogueDialog");
    }

    private void getData(FilterCouponModel filterCoupon, boolean isRefreshFilter, boolean isLoading) {
        filterCouponModel = filterCoupon;
        if (vendorCategoryModel == null) {
            return;
        }
        rcv_catalogue.setLoading(true);

        requestApi(new CouponUserGetCouponCatalogueListRequest(vendorCategoryModel.getId(),
                paramsToGetCategory(rcv_catalogue.getNextPageIndex(),vendorCategoryModel.getId(), getLocation(), getCountryCodeModel().getCountry_code(), TAG_TYPE_COUPON, filterCouponModel),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        rcv_catalogue.removeLoadingItem();
                        BaseResultsResponseModel<UserCouponCatalogueDataModel> catalogueResponseModel
                                = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult());
                        ArrayList<UserCouponCatalogueDataModel> list = catalogueResponseModel.getResults();
                        if (isRefreshFilter) {
                            listItemCatalogue.clear();
                        }
                        if (list != null && list.size() > 0) {
                            listItemCatalogue.addAll(list);
                        }
                        rcv_catalogue.setLimit(catalogueResponseModel.getCount());
                        // TCUtils.setNetPageIndexRecycleVew(response, rcv_catalogue);
                        if (!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext())) {
                            rcv_catalogue.setNextPageIndex(rcv_catalogue.getNextPageIndex() + 1);
                            rcv_catalogue.setCanLoadMore(true);
                        } else {
                            rcv_catalogue.setCanLoadMore(false);
                        }
                        showViewNoData(listItemCatalogue.size() <= 0);
                        if (refresh_view.isRefreshing()) {
                            refresh_view.setRefreshing(false);
                        }
                        if (listItemCatalogue.size() > 0 && isRefreshFilter) {
                            rcv_catalogue.scrollToPosition(0);
                        }
                        rcv_catalogue.onLoadMoreComplete();
                        refresh_view.setEnabled(true);
                     //   rcv_catalogue.insertLoadingItem();// progressBar bottom
                        rcv_catalogue.setLoading(false);

                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        showViewNoData(listItemCatalogue.size() <= 0);
                        rcv_catalogue.onLoadMoreComplete();
                        rcv_catalogue.setCanMore(false);
                        rcv_catalogue.setLoading(false);
                    }
                }));
    }

    private void showViewNoData(boolean isNoData) {
        tv_no_data.setVisibility(isNoData ? View.VISIBLE : View.GONE);
        rcv_catalogue.setVisibility(isNoData ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue()
                && finishedResultCode == RESULT_OK) {
            rcv_catalogue.setNextPageIndex(1);
            setupRecycler();
        } else if (requestCode == EnumMgr.RequestCode.GOTO_SEARCH_COUPON.getValue() && finishedResultCode == RESULT_OK) {
            String search = finishedResult.getStringExtra(SEARCH_OPTION);
            if (filterCouponModel != null) {
                filterCouponModel.setTextSearch(search);
                rcv_catalogue.setNextPageIndex(1);
                frg_coupon_user_catalogue_list_tv_title.setText(search);
                getData(filterCouponModel, true, true);
            }
        }
    }


    private void refreshData() {
        refresh_view.setOnRefreshListener(() -> {
            //setupRecycler();
            listItemCatalogue.clear();
            rcv_catalogue.setNextPageIndex(1);
            getData(filterCouponModel, false, true);
        });
    }

    private void updateCatalogueTitle(String name) {
        tv_catalogue.setText(name);

    }

    private void setNumberFilter() {
        int numberFilter = 0;
        if (filterCouponModel != null) {
            numberFilter = filterCouponModel.getNumberFilter(filterCouponModel);

        }
        if (vendorCategoryModel != null)
            numberFilter++;

        tvNumFilter.setText(String.valueOf(numberFilter));
        tvNumFilter.setVisibility(numberFilter > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        handelScrollingCategory(appBarLayout,verticalOffset,rcv_catalogue_image,view_category_name);
    }

    public void reloadPriceCoupon(){
        if (null!= adapter)
            adapter.notifyDataSetChanged();
    }
}
