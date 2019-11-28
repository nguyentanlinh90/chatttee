package com.teecoin.feature.reviewSystem.searchLocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverListener;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverModel;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.UserFilterDiscoverDialog;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.VendorListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCViewPager;
import com.teecoin.utils.CustomView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;

public class NewSearchLocationScreen extends TCCouponBaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener, FilterDiscoverListener {

    private static final String FILTER_DISCOVER_MODEL = "FILTER_DISCOVER_MODEL";
    private static final String VENDOR_CATEGORY_MODEL = "VENDOR_CATEGORY_MODEL";

    @BindView(R.id.fragment_search_location_map_view)
    MapView mapView;

    @BindView(R.id.fragment_search_location_iv_back)
    ImageView ivBack;

    @BindView(R.id.fragment_search_location_et_search)
    AutoCompleteTextView etSearch;

    @BindView(R.id.fragment_search_location_iv_clear)
    ImageView ivClear;

    @BindView(R.id.view_filter_number_rl_filter)
    View vFilter;

    @BindView(R.id.view_filter_number_tv_num_filter)
    TextView tvNumFilter;

    @BindView(R.id.view_bottom_sheet_map_vp_vendor)
    TCViewPager vpVendor;

    @BindView(R.id.view_bottom_sheet_map)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.view_bottom_sheet_map_rl_behavior_peek_height)
    View rlBehaviorPeekHeight;

    @BindView(R.id.view_bottom_sheet_map_v_show_bottom_sheet)
    ImageView vShowBottomSheet;

    @BindView(R.id.view_bottom_sheet_map_v_hide_bottom_sheet)
    View vHideBottomSheet;

    @BindView(R.id.fragment_search_location_rl_gps)
    View vGps;

    @BindView(R.id.fragment_search_location_tv_loading)
    TextView tvLoading;
    BottomSheetBehavior sheetBehavior;
    long countDownTime = 2000;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private ArrayList<VendorModel> listVendor;
    private ViewPagerOnMapAdapter mapAdapter;
    private FilterDiscoverModel filterDiscoverModel;
    private VendorCategoryModel categorySelectModel;
    private LatLng latLngForGetListVendor = getLocation();
    private boolean isFirstMoveCamera = true;
    private int posMarkerClickOld = -1;
    private float mapZoom = TCConstant.MAP_ZOOM_DEFAULT;

    CountDownTimer countDownTimer = new CountDownTimer(countDownTime, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

            requestListVendor();
        }
    };

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {

            mapAdapter.highlightVendorSlide(position);

            new Handler().postDelayed(() -> {

                if (posMarkerClickOld != -1) {

                    drawMarker(listVendor.get(posMarkerClickOld).getMarker().getPosition(), CustomViewMarker.TYPE_DEFAULT, false, listVendor.get(posMarkerClickOld));
                }
                drawMarker(listVendor.get(position).getMarker().getPosition(), CustomViewMarker.TYPE_CLICK, false, listVendor.get(position));

                posMarkerClickOld = position;

            }, 50L);
        }
    };

    public static NewSearchLocationScreen getInstance(FilterDiscoverModel filterDiscoverModel, VendorCategoryModel categorySelectModel) {

        NewSearchLocationScreen screen = new NewSearchLocationScreen();

        Bundle bundle = new Bundle();

        bundle.putSerializable(FILTER_DISCOVER_MODEL, filterDiscoverModel);

        bundle.putSerializable(VENDOR_CATEGORY_MODEL, categorySelectModel);

        screen.setArguments(bundle);

        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {

        Locale.setDefault(new Locale(TCUtils.getLanguageCode()));

        Bundle bundle = getArguments();

        if (null != bundle) {

            filterDiscoverModel = (FilterDiscoverModel) bundle.getSerializable(FILTER_DISCOVER_MODEL);

            categorySelectModel = (VendorCategoryModel) bundle.getSerializable(VENDOR_CATEGORY_MODEL);

            TCUtils.setNumberFilter(filterDiscoverModel, categorySelectModel, tvNumFilter);

            if (TCUtils.isEmpty(filterDiscoverModel.getKeyword())) {
                etSearch.setHint(TCUtils.getString(R.string.what_are_you_looking_for));

            } else {
                etSearch.setText(filterDiscoverModel.getKeyword());
            }
        }

        setupMap();

        initSearchInput();

        listVendor = new ArrayList<>();

        intBottomSheet();

        registerSingleClick(R.id.fragment_search_location_iv_back, R.id.view_filter_number_rl_filter, R.id.fragment_search_location_rl_gps);

        initViewPager();

        etSearch.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideKeyBoard();

                requestListVendor();
            }

            return false;
        });

        onClick();
    }

    private void onClick() {

        vShowBottomSheet.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        vHideBottomSheet.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
    }

    private void setupMap() {

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_search_location_map_view, mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        if (!(ActivityCompat.checkSelfPermission(getActiveActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActiveActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

//            googleMap.setMyLocationEnabled(true);

            vGps.setVisibility(View.VISIBLE);
        }

        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraChangeListener(this);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActiveActivity(), R.raw.map_style));

        moveCamera(latLngForGetListVendor);

    }

    public double calculateVisibleRadius() {

        float[] distanceWidth = new float[1];

        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        //calculate the distance between left <-> right of map on screen
        Location.distanceBetween((farLeft.latitude + nearLeft.latitude) / 2, farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2, farRight.longitude, distanceWidth);

        // visible radius is / 2
        return distanceWidth[0] / 2;
    }

    private void enableItem(boolean isEnable) {

        ivBack.setEnabled(isEnable);
        etSearch.setEnabled(isEnable);
        ivClear.setEnabled(isEnable);
        vFilter.setEnabled(isEnable);

    }

    private void requestListVendor() {

        tvLoading.setVisibility(View.VISIBLE);
        enableItem(false);

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
                || sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        filterDiscoverModel.setDistance(String.valueOf(calculateVisibleRadius() / 1000)); //meter -> km

        filterDiscoverModel.setKeyword(etSearch.getText().toString());

        requestApi(new VendorListRequest(TCUtils.paramsGetVendorList(1, TCConstant.PAGE_SIZE_30, getLocation(), latLngForGetListVendor,
                getCountryCodeModel().getCountry_code(), filterDiscoverModel.getKeyword(),
                filterDiscoverModel.getDistance(), filterDiscoverModel.getOpen_date(), categorySelectModel.getId(),
                filterDiscoverModel.getPrice_range(), filterDiscoverModel.getSort_condition(),
                filterDiscoverModel.getRatings()), new APIResponseListener() {

            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                tvLoading.setVisibility(View.GONE);
                enableItem(true);

                googleMap.clear();

                listVendor.clear();

                ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();

                listVendor.addAll(list);

                if (listVendor.size() > 0) {

                    posMarkerClickOld = -1;

                    drawMarker();

                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } else {

                    Toast.makeText(getActiveActivity(), TCUtils.getString(R.string.no_result), Toast.LENGTH_LONG).show();
                }
                mapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                tvLoading.setVisibility(View.GONE);
                enableItem(true);
                showMessage(TCUtils.getString(R.string.text_fail));
            }
        }));
    }

    private void drawMarker() {

        for (int i = 0; i < listVendor.size(); i++) {

            StringTokenizer location = new StringTokenizer(listVendor.get(i).getLocation(), ",");

            double lat = TCUtils.convertToDouble(location.nextToken());
            double lng = TCUtils.convertToDouble(location.nextToken());

            Marker marker = drawMarker(new LatLng(lat, lng), CustomViewMarker.TYPE_DEFAULT, true, listVendor.get(i));

            listVendor.get(i).setMarker(marker);
        }
    }

    private void moveCamera(LatLng latLng) {
        if (latLng == null) {
            // TCUtils.showToast("Please turn on your Location");
            return;
        }

        float mapZoom = isFirstMoveCamera ? TCConstant.MAP_ZOOM_DEFAULT : googleMap.getCameraPosition().zoom;

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoom));

        isFirstMoveCamera = false;

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        latLngForGetListVendor = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

    }

    @Override
    public void onCameraIdle() {

        mapZoom = googleMap.getCameraPosition().zoom;

        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        if (null != countDownTimer)
            countDownTimer.start();
    }

    private void initViewPager() {

        mapAdapter = new ViewPagerOnMapAdapter(LayoutInflater.from(getActiveActivity()), listVendor, (view, item, position, clickType) -> {

            if (clickType == EnumMgr.ClickType.Vendor_Favourite) {

                submitFavourite(position, item,
                        (position12, vendorModel) -> mapAdapter
                                .setStateIconFavorite(
                                        position12, !vendorModel.isFavorite()));

            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {

                getShareVendor(item);

            } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {

                writeReview(item);

            } else {

                addFragment(VendorScreen.getInstance(item.getId()));
            }
        });

        vpVendor.setAdapter(mapAdapter);

        vpVendor.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        for (int i = 0; i < listVendor.size(); i++) {
            if (listVendor.get(i).getMarker().getPosition().equals(marker.getPosition())) {
                int finalI = i;

//                if (finalI > 0) {
                vpVendor.setCurrentItem(i, true);
//                } else {
                vpVendor.post(() -> pageChangeListener.onPageSelected(finalI));
//                }
                break;
            }
        }
        return true;
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.fragment_search_location_iv_back:

                handleBackPressed();
                break;

            case R.id.view_filter_number_rl_filter:

                showFilter();
                break;

            case R.id.fragment_search_location_rl_gps:

                googleMap.setPadding(0, 0, 0, 0);
                isFirstMoveCamera = true;
                moveCamera(TCUtils.getGPS(getActiveActivity()));
                break;
        }
    }

    private void showFilter() {

        UserFilterDiscoverDialog filterCategories = new UserFilterDiscoverDialog(getActiveActivity(), true,
                getListCategorySort(), filterDiscoverModel, filterDiscoverModel.getSort_condition(), (filterDiscoverModel, listCategory) -> {

            startFilter();
        });
        filterCategories.show(getChildFragmentManager(), "FilterCategoriesDialog");
    }

    private void startFilter() {
        categorySelectModel = filterDiscoverModel.getCategoryModel();
        TCUtils.setNumberFilter(filterDiscoverModel, categorySelectModel, tvNumFilter);
        requestListVendor();
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.fragment_search_location_iv_back, R.id.view_filter_number_rl_filter,
                R.id.fragment_search_location_rl_gps);
    }

    private void initSearchInput() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        };

        etSearch.addTextChangedListener(textWatcher);

        ivClear.setOnClickListener(v -> etSearch.setText(""));
    }

    private void intBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        setPaddingMapToolBar(newState, 0);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        setPaddingMapToolBar(newState, layoutBottomSheet.getHeight());
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        setPaddingMapToolBar(newState, rlBehaviorPeekHeight.getHeight());
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private Marker drawMarker(LatLng latLng, int type, boolean isSetTitle, VendorModel vendorModel) {

        Bitmap bitmap = null;

        if (type == CustomViewMarker.TYPE_DEFAULT) {
            if (getBitMapUnSelect().size() > 0) {
                for (int i = 0; i < getBitMapUnSelect().size(); i++) {
                    if (categorySelectModel.getId().equals(getBitMapUnSelect().get(i).getId())) {
                        bitmap = getBitMapUnSelect().get(i).getBitmap();
                    }
                }
            } else
                bitmap = ((BitmapDrawable) TCUtils.getDrawable(R.drawable.ic_marker_default)).getBitmap();
        } else {
            if (getBitMapSelect().size() > 0) {
                for (int i = 0; i < getBitMapSelect().size(); i++) {
                    if (categorySelectModel.getId().equals(getBitMapSelect().get(i).getId())) {
                        bitmap = getBitMapSelect().get(i).getBitmap();
                    }
                }
            } else
                bitmap = ((BitmapDrawable) TCUtils.getDrawable(R.drawable.ic_marker_default_select)).getBitmap();
        }

        return googleMap.addMarker(new MarkerOptions().position(latLng)/*.anchor(0.5f, 0.5f)*/
                .icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(),
                        new CustomViewMarker(getActiveActivity(), bitmap, mapZoom, isSetTitle, vendorModel)))));
    }

    private void setPaddingMapToolBar(int viewState, int height) {
        vShowBottomSheet.setVisibility(viewState == BottomSheetBehavior.STATE_COLLAPSED ? View.VISIBLE : View.GONE);
        vHideBottomSheet.setVisibility(viewState == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
        new Handler().postDelayed(() -> googleMap.setPadding(0, 0, 0, height), 100L);
    }

    @Override
    public void onFilter(FilterDiscoverModel filterDiscoverModel, ArrayList<VendorCategoryModel> listCategory) {
        categorySelectModel = filterDiscoverModel.getCategoryModel();
        requestListVendor();
    }
}
