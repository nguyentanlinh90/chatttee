package com.teecoin.feature.reviewSystem.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.nex3z.flowlayout.FlowLayout;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.filter.FilterItem;
import com.teecoin.feature.reviewSystem.filter.FilterModel;
import com.teecoin.feature.reviewSystem.filter.FilterSelectedView;
import com.teecoin.feature.reviewSystem.location.LocationScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.CuisineModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetCuisineListByIdRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetFilterRestaurantRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetVendorLatLngRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetVendorRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNewMerchantRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class RestaurantScreen extends TCReviewBaseFragment implements APIResponseListener {
    @BindView(R.id.ll_cuisine_type)
    View ll_cuisine_type;
    @BindView(R.id.ic_down_arrow)
    ImageView ic_down_arrow;
    @BindView(R.id.ll_change_location)
    View ll_change_location;
    @BindView(R.id.frg_restaurant_rcv_restaurant)
    TCRecyclerView rcv_restaurant;
    @BindView(R.id.frg_restaurant_nested_scroll)
    NestedScrollView nested_scroll;

    @BindView(R.id.frg_restaurant_fl_filters_selected)
    FlowLayout fl_filters_selected;
    @BindView(R.id.tv_count)
    TextView tv_count;
    private ArrayList<CuisineModel> listCuisineSelected;
    private ArrayList<VendorModel> listRestaurant;
    private RestaurantAdapter adapterRestaurant;

    private int pageLoad = 1;
    private boolean isFilter = false;
    private FilterModel filterModel;
    private static final String CUISINE_MODEL = "CUISINE_MODEL";
    private static final String LAT_LNG = "LAT_LNG";
    private static final String KEY_WORK = "KEY_WORK";

    private static final String TYPE_RESTAURANTS = "TYPE_RESTAURANTS";
    private static final String IS_FROM_LOCATION_SCREEN = "IS_FROM_LOCATION_SCREEN";
    private CuisineModel cuisinesModel;
    private LatLng latLngForGetListRestaurant;
    private boolean isScrooll;

    private String keyword;

    private int typeRestaurant = EnumMgr.TypeRestaurants.Restaurants.getValue();
    private boolean is_from_location_screen = false;

    public static RestaurantScreen getInstance(CuisineModel cuisines) {
        RestaurantScreen screen = new RestaurantScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CUISINE_MODEL, cuisines);
        screen.setArguments(bundle);
        return screen;
    }

    public static RestaurantScreen getInstance(boolean is_from_location_screen, LatLng latLng, int typeRestaurants, String keyword) {
        RestaurantScreen screen = new RestaurantScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_LOCATION_SCREEN, is_from_location_screen);
        bundle.putParcelable(LAT_LNG, latLng);
        bundle.putInt(TYPE_RESTAURANTS, typeRestaurants);
        bundle.putString(KEY_WORK, keyword);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showFilterToolbar();
        showButtonBackToolbar();
        showFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        ll_cuisine_type.setVisibility(View.VISIBLE);
        tv_count.setText(String.format(TCUtils.getString(R.string.cuisines_type_count), 0));
        updateTitleHeader(TCUtils.getString(R.string.text_restaurant).toUpperCase());
        if (bundle != null) {
            cuisinesModel = (CuisineModel) bundle.getSerializable(CUISINE_MODEL);
            is_from_location_screen = bundle.getBoolean(IS_FROM_LOCATION_SCREEN);
            latLngForGetListRestaurant = bundle.getParcelable(LAT_LNG);
            typeRestaurant = bundle.getInt(TYPE_RESTAURANTS);
            keyword = bundle.getString(KEY_WORK);
            if (cuisinesModel != null) {
                ic_down_arrow.setVisibility(View.GONE);
                tv_count.setText(String.format(TCUtils.getString(R.string.cuisines_type_count), 1));
            }
        }
        listCuisineSelected = new ArrayList<>();
        setupRecycler();
        initOnClickEvent();
    }

    private void initOnClickEvent() {
        ll_change_location.setOnClickListener(v -> gotoMaps());
    }

    private void gotoMaps() {
        if (listRestaurant != null) {
            if (is_from_location_screen) {
                getActiveActivity().onBackPressed();
            } else {
                addFragmentForResult(EnumMgr.RequestCode.GOTO_MAP_FROM_RESTAURANT.getValue(), LocationScreen.getInstance(listRestaurant, true));
            }
        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_MAP_FROM_RESTAURANT.getValue()) {
            if (finishedResult != null && finishedResult.getExtras() != null) {
                Bundle bundle = finishedResult.getExtras();
                latLngForGetListRestaurant = bundle.getParcelable(TCConstant.LAT_LNG_CURRENT);
                pageLoad = 1;
                getRestaurant(latLngForGetListRestaurant);
            }
        }
    }

    private void setupRecycler() {
        listRestaurant = new ArrayList<>();
        rcv_restaurant.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));

        adapterRestaurant = new RestaurantAdapter(LayoutInflater.from(getActiveActivity()), listRestaurant, (view, item, position, clickType) -> addFragment(VendorScreen.getInstance(item.getId())));
        rcv_restaurant.setAdapter(adapterRestaurant);
        rcv_restaurant.setNestedScrollingEnabled(false);

        nested_scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                //  Log.e(TAG, "Scroll DOWN");
            }
            if (scrollY < oldScrollY) {
                // Log.e(TAG, "Scroll UP");
            }

            if (scrollY == 0) {
                // Log.e(TAG, "TOP SCROLL");
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                //  Log.e(TAG, "BOTTOM SCROLL");
                new Handler().postDelayed(() -> {
                    isScrooll = true;
                    pageLoad++;
                    fillGetData();
                }, 300);
            }
        });
        filterModel = new FilterModel();
        fillGetData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideFilterToolbar();
    }

    private void fillGetData() {
        if (isFilter) {
            filterRestaurant();
        } else {
            if (cuisinesModel != null) {
                getListCuisinesByID();
            } else {
                if (!isScrooll) {
                    filterModel = new FilterModel();
                    if (!TCUtils.isEmpty(keyword)) {// from search box
                        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Keyword, keyword));
                    } else if (typeRestaurant == EnumMgr.TypeRestaurants.NewMerchants.getValue()) {// if choose view all from new merchant
                        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.NewMerchant));
                    } else {
                        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.NearMe));
                        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Distance, "5"));
                    }

                }

            }
            fl_filters_selected.removeAllViews();
            for (FilterItem filter : filterModel.getFilterList()) {
                FilterSelectedView v = new FilterSelectedView(getActiveActivity(), filter, (filterAAA, needToRemoveView) -> {
                    filterModel.remove(filter);
                    fl_filters_selected.removeView(needToRemoveView);
                    resetFilter();
                    filterRestaurant();
                });

                //  TCLog.e("filter item "+filter.getFilter());
                fl_filters_selected.addView(v);
                if (filter.getFilter().equals(FilterModel.Filter.Page) || filter.getFilter().equals(FilterModel.Filter.Latitude) || filter.getFilter().equals(FilterModel.Filter.Longitude)) {
                    // fl_filters_selected.addView(v);
                    fl_filters_selected.removeView(v);
                }
            }

            if (!TCUtils.isEmpty(keyword)) {
                filterRestaurant();
            } else {
                getRestaurant(latLngForGetListRestaurant);
            }
        }
    }


    private void getRestaurant(LatLng latLng) {
        TCLog.e("getRestaurant");
        if (typeRestaurant == EnumMgr.TypeRestaurants.Restaurants.getValue()) {
            if (latLng != null) {
                requestApi(new ReviewGetVendorLatLngRequest(pageLoad, latLng, TCConstant.KM_DEFAULT, TCDateUtility.getTimeZone(), this));
            } else {
                requestApi(new ReviewGetVendorRequest(pageLoad, TCConstant.KM_DEFAULT, TCDateUtility.getTimeZone(), this));
            }

        } else if (typeRestaurant == EnumMgr.TypeRestaurants.NewMerchants.getValue()) {
            requestApi(new ReviewUserGetNewMerchantRequest(pageLoad, TCConstant.KM_DEFAULT, "1", TCDateUtility.getTimeZone(), latLng, true, this));
        } else if (typeRestaurant == EnumMgr.TypeRestaurants.NearYou.getValue()) {
            if (latLng == null) {
                latLng = TCUtils.getGPS(getActiveActivity());
                if (latLng == null) {
                    latLng = TCConstant.LOCATION_DEFAULT;
                }
            }
//            requestApi(new UserDiscoverGetRecommendForYouRequest(false,
//                    new PlaceAutoCompleteModel(pageLoad, latLng), this));
        } else {
            if (latLng != null) {
                requestApi(new ReviewGetVendorLatLngRequest(pageLoad, latLng, TCConstant.KM_DEFAULT, TCDateUtility.getTimeZone(), this));

            }
        }

    }

    private void getListCuisinesByID() {
        LatLng latLng = TCUtils.getGPS(getActiveActivity());
        // latLngForGetListRestaurant=TCUtils.getGPS();: todo: get locatioln by device
        latLng = TCConstant.LOCATION_DEFAULT;// todo: get loaction default
        requestApi(new ReviewGetCuisineListByIdRequest(pageLoad, latLng, TCConstant.KM_DEFAULT, cuisinesModel.getId(), TCDateUtility.getTimeZone(), this));

    }

    public void filter(FilterModel filters) {
        isFilter = true;
        resetFilter();
        filterModel = filters;
        fl_filters_selected.removeAllViews();
        for (FilterItem filter : filterModel.getFilterList()) {
            FilterSelectedView v = new FilterSelectedView(getActiveActivity(), filter, (filterAAA, needToRemoveView) -> {
                filterModel.remove(filter);
                fl_filters_selected.removeView(needToRemoveView);

                resetFilter();
                filterRestaurant();
            });
            fl_filters_selected.addView(v);
        }
        if (cuisinesModel != null) {
            filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Cuisine, cuisinesModel.getId()));
        }
        filterRestaurant();
    }

    public FilterModel getFilter() {
        return filterModel;
    }

    private void filterRestaurant() {
        TCLog.e("filterRestaurant");
        rcv_restaurant.setAdapter(adapterRestaurant);
        filterModel.removePageUpdate(filterModel.getFilterList());// remove page
        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Page, String.valueOf(pageLoad)));// updateTo page
        if (latLngForGetListRestaurant == null) {
            filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Latitude, String.valueOf(TCConstant.LOCATION_DEFAULT.latitude)));
            filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Longitude, String.valueOf(TCConstant.LOCATION_DEFAULT.longitude)));
        } else {
            filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Latitude, String.valueOf(latLngForGetListRestaurant.latitude)));
            filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Longitude, String.valueOf(latLngForGetListRestaurant.longitude)));
        }
        requestApi(new ReviewGetFilterRestaurantRequest(filterModel, this));

        if (pageLoad == 1) {
            nestedScrollToTop(nested_scroll);
        }
    }

    private void resetFilter() {
        if (listRestaurant != null && listRestaurant.size() > 0) {
            listRestaurant.clear();
        }
        pageLoad = 1;
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_VENDORS_LAT_LNG
                || requestTarget == ReviewRequestTarget.GET_VENDORS
                || requestTarget == ReviewRequestTarget.GET_LIST_CUISINES_BY_ID
                || requestTarget == ReviewRequestTarget.GET_NEW_MERCHANTS
                || requestTarget == ReviewRequestTarget.GET_RECOMMEND_FOR_YOU) {
            if (((BaseResultsResponseModel) response.getResult()).getResults() != null) {
                ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                listRestaurant.addAll(list);

            }
        } else if (requestTarget == ReviewRequestTarget.GET_FILTER_RESTAURANT) {
            ArrayList<VendorModel> list = (ArrayList<VendorModel>) ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listRestaurant.addAll(list);
            }
        }
        rcv_restaurant.onLoadMoreComplete();

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
        rcv_restaurant.onLoadMoreComplete();
    }

}
