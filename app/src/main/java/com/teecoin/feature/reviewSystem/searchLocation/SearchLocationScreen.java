package com.teecoin.feature.reviewSystem.searchLocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.reviewSystem.location.PlaceArrayAdapter;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.PlaceAutoCompleteModel;
import com.teecoin.model.reviewsystem.VendorMarker;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.UserDiscoverGetRecommendForYouRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCViewPager;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.getDistance;

public class SearchLocationScreen extends TCCouponBaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener {
    private static final int GOOGLE_API_CLIENT_ID = 0;
    @BindView(R.id.fragment_search_location_iv_back)
    ImageView ivBack;
    @BindView(R.id.fragment_search_location_et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.fragment_search_location_iv_clear)
    ImageView ivClear;
    @BindView(R.id.view_bottom_sheet_map_tv_load_more)
    TextView tvLoadMore;
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

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private GoogleApiClient mGoogleApiClient;

    private ArrayList<VendorModel> listVendor;
    private ViewPagerOnMapAdapter viewPagerOnMapAdapter;

    private Marker markerSelect = null;

    BottomSheetBehavior sheetBehavior;

    private LatLng latLng;

    private LatLng latLngWhenChangeCamera;

    private boolean isFirstMoveCamera = true;

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = places -> {

        if (!places.getStatus().isSuccess()) {
            return;
        }
        // Selecting the first object buffer.
        final Place place = places.get(0);

        requestListPlace(place.getLatLng());

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutoCompleteModel item = mPlaceArrayAdapter.getItem(position);
            if (item != null) {
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    };

    public static SearchLocationScreen getInstance() {
        return new SearchLocationScreen();
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

        //latLng = getLatLngCurrent();
        latLng = getLocation();

        onClick();

        initSearchInput();

        listVendor = new ArrayList<>();

        setupAutoCompleteSearch();

        setupMap();

        intBottomSheet();

    }

    private void onClick() {

        ivBack.setOnClickListener(v -> handleBackPressed());

        tvLoadMore.setOnClickListener(v -> {
            requestListPlace(latLngWhenChangeCamera);
            //latLng.setLatLng(latLngWhenChangeCamera);
            latLng = latLngWhenChangeCamera;
        });

        vGps.setOnClickListener(v -> {

            for (int i = 0; i < listVendor.size(); i++) {

                // un Select all marker
                drawMarker(listVendor.get(i).getVendorMarker().getLatLng(), CustomViewMarker.TYPE_DEFAULT,
                        "", listVendor.get(i).isHaveCatalogueCoupon(), listVendor.get(i).isHaveCashVoucher());

            }

            setStateBottomSheet(BottomSheetBehavior.STATE_HIDDEN);

            googleMap.setPadding(0, 0, 0, 0);

            moveCameraToPosition(TCUtils.getGPS(getActiveActivity()));
        });

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

                    case BottomSheetBehavior.STATE_EXPANDED: {
                        setPaddingMapToolBar(newState, layoutBottomSheet.getHeight());
                    }
                    break;

                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        setPaddingMapToolBar(newState, rlBehaviorPeekHeight.getHeight());
                    }
                    break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;

                    case BottomSheetBehavior.STATE_SETTLING:
                        break;

                }
                setMarginForViewLoadMore(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private void setupAutoCompleteSearch() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActiveActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActiveActivity(), GOOGLE_API_CLIENT_ID, connectionResult ->
                        Toast.makeText(getActiveActivity(), String.format(TCUtils.getString(R.string.google_places_api_connection_failed_with_error_code),
                                connectionResult.getErrorCode()), Toast.LENGTH_LONG).show())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mPlaceArrayAdapter.setGoogleApiClient(null);
                    }
                })
                .build();
        etSearch.setThreshold(3);
        etSearch.setOnItemClickListener(mAutocompleteClickListener);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActiveActivity(), android.R.layout.simple_list_item_1, autocompleteFilter);
        etSearch.setAdapter(mPlaceArrayAdapter);

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

            googleMap.setMyLocationEnabled(true);

            vGps.setVisibility(View.VISIBLE);
        }

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraChangeListener(this);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActiveActivity(), R.raw.map_style));

        requestListPlace(latLng);

    }

    private void requestListPlace(LatLng latLngSearch) {

        setStateBottomSheet(BottomSheetBehavior.STATE_HIDDEN);

        //hardcode distance = 2km
        requestApi(new UserDiscoverGetRecommendForYouRequest(getLocation(), latLngSearch,
                getCountryCodeModel().getCountry_code(), "2",
                new APIResponseListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                        googleMap.clear();

                        listVendor.clear();

                        tvLoadMore.setVisibility(View.GONE);

                        ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();

                        listVendor.addAll(list);

                        initView();

                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                        if (null == errorModel.getErrorMessage() || TCUtils.isEmpty(errorModel.getErrorMessage())) {
                            showMessage(TCUtils.getString(R.string.text_fail));
                        } else {
                            showMessage(errorModel.getErrorMessage());
                        }
                    }
                }));

    }

    private void initView() {

        if (listVendor.size() > 0) {

//            setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED);

            for (int i = 0; i < listVendor.size(); i++) {

                StringTokenizer location = new StringTokenizer(listVendor.get(i).getLocation(), ",");

                double lat = TCUtils.convertToDouble(location.nextToken());
                double lng = TCUtils.convertToDouble(location.nextToken());

                /*if (0 == i) {
                    //set lat lng current select

                    latLng = new LatLng(lat, lng);
                    moveCameraToPosition(latLng);
                }*/

                //draw list marker
                Marker marker = drawMarker(new LatLng(lat, lng), CustomViewMarker.TYPE_DEFAULT,
                        listVendor.get(i).getName(), listVendor.get(i).isHaveCatalogueCoupon(), listVendor.get(i).isHaveCashVoucher());

                listVendor.get(i).setVendorMarker(new VendorMarker(marker.getId(), marker.getPosition()));

            }

            //set marker select the first
//            markerSelect = drawMarker(latLng, CustomViewMarker.TYPE_CLICK,
//                    "", listVendor.get(0).isHaveCatalogueCoupon(), listVendor.get(0).isHaveCashVoucher());

        } else {

//            moveCameraToPosition(latLng);

//            setStateBottomSheet(BottomSheetBehavior.STATE_HIDDEN);

            Toast.makeText(getActiveActivity(), TCUtils.getString(R.string.no_result), Toast.LENGTH_LONG).show();
        }

        moveCameraToPosition(latLng);

        initListVendor();

    }

    private void initListVendor() {

        viewPagerOnMapAdapter = new ViewPagerOnMapAdapter(LayoutInflater.from(getActiveActivity()), listVendor, (view, item, position, clickType) -> {

            if (clickType == EnumMgr.ClickType.Vendor_Favourite) {

                submitFavourite(position, item,
                        (position12, vendorModel) -> viewPagerOnMapAdapter
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

        vpVendor.setAdapter(viewPagerOnMapAdapter);

        vpVendor.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                /*new Handler().postDelayed(() -> {

                    viewPagerOnMapAdapter.highlightVendorSlide(position);

                    drawMarker(markerSelect.getPosition(), CustomViewMarker.TYPE_DEFAULT,
                            "", listVendor.get(position).isHaveCatalogueCoupon(), listVendor.get(position).isHaveCashVoucher());

                    LatLng latLngSelect = listVendor.get(position).getVendorMarker().getLatLng();

                    markerSelect = drawMarker(latLngSelect, CustomViewMarker.TYPE_CLICK,
                            "", listVendor.get(position).isHaveCatalogueCoupon(), listVendor.get(position).isHaveCashVoucher());

                }, 300L);*/

                for (int i = 0; i < listVendor.size(); i++) {

                    // if marker select
                    if (i == position) {

                        drawMarker(listVendor.get(i).getVendorMarker().getLatLng(), CustomViewMarker.TYPE_CLICK,
                                "", listVendor.get(i).isHaveCatalogueCoupon(), listVendor.get(i).isHaveCashVoucher());

                        // if marker un select
                    } else {

                        drawMarker(listVendor.get(i).getVendorMarker().getLatLng(), CustomViewMarker.TYPE_DEFAULT,
                                "", listVendor.get(i).isHaveCatalogueCoupon(), listVendor.get(i).isHaveCashVoucher());
                    }

                }

                new Handler().postDelayed(() -> viewPagerOnMapAdapter.highlightVendorSlide(position), 300L);


//                moveCameraToPosition(latLngSelect);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //set marker select the first
        /*if (listVendor.size() > 0) {
            viewPagerOnMapAdapter.highlightVendorSlide(0);
        }*/

    }

    private Marker drawMarker(LatLng latLng, int type, String title, boolean isCoupon, boolean isCash) {
//        return googleMap.addMarker(new MarkerOptions().position(latLng)
//                .icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(),
//                        new CustomViewMarker(getActiveActivity(), type, 1, title, isCoupon, isCash)))));
        return null;
    }

    private void moveCameraToPosition(LatLng latLng) {

        float mapZoom = isFirstMoveCamera ? TCConstant.MAP_ZOOM_DEFAULT : googleMap.getCameraPosition().zoom;

//        hideKeyBoard();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, mapZoom);

        googleMap.animateCamera(cameraUpdate);

        isFirstMoveCamera = false;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED);

        for (int i = 0; i < listVendor.size(); i++) {

            if (listVendor.get(i).getVendorMarker().getMarkerId().equals(marker.getId())) {

//                drawMarker(marker.getPosition(), CustomViewMarker.TYPE_CLICK,
//                        "", listVendor.get(i).isHaveCatalogueCoupon(), listVendor.get(i).isHaveCashVoucher());

                //scroll list to this marker click
                vpVendor.setCurrentItem(i, true);

                break;
            }
        }

//        markerSelect = marker;

//        googleMap.getUiSettings().setMapToolbarEnabled(true);

        return true; // can't move camera map by this, false if want move camera

    }

    private void setStateBottomSheet(int bottomSheet) {

        sheetBehavior.setState(bottomSheet);

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        latLngWhenChangeCamera = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

        if (latLng != null) {

            if (getDistance(latLng, latLngWhenChangeCamera) > TCConstant.DISTANT_MIN_TO_LOAD_MORE_PLACE) {

                tvLoadMore.setVisibility(View.VISIBLE);

                setMarginForViewLoadMore(sheetBehavior.getState());

            } else {

                tvLoadMore.setVisibility(View.GONE);

            }
        }

    }

    private void setMarginForViewLoadMore(int state) {

        int marginExtra = 50;
        ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams) tvLoadMore.getLayoutParams();
        if (BottomSheetBehavior.STATE_EXPANDED == state) {
            lpt.setMargins(0, 0, 0, layoutBottomSheet.getHeight() + marginExtra);
        } else if (BottomSheetBehavior.STATE_COLLAPSED == state) {
            lpt.setMargins(0, 0, 0, rlBehaviorPeekHeight.getHeight() + marginExtra);
        } else {
            lpt.setMargins(0, 0, 0, marginExtra);
        }

    }

    private void setPaddingMapToolBar(int viewState, int height) {

        vShowBottomSheet.setVisibility(viewState == BottomSheetBehavior.STATE_COLLAPSED ? View.VISIBLE : View.GONE);
        vHideBottomSheet.setVisibility(viewState == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
        new Handler().postDelayed(() -> googleMap.setPadding(0, 0, 0, height), 100L);

    }

    @Override
    public void onDestroy() {
        if (null != getActivity()) {
            mGoogleApiClient.stopAutoManage(getActivity());
        }
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

}
