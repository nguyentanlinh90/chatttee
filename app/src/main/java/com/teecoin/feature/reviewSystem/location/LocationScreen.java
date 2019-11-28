package com.teecoin.feature.reviewSystem.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.general.popup.RequestLocationPermissionDialog;
import com.teecoin.feature.reviewSystem.restaurant.RestaurantScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.reviewsystem.PlaceAutoCompleteModel;
import com.teecoin.model.reviewsystem.VendorMarker;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.CustomView;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.getDistance;

public class LocationScreen extends TCReviewBaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener, TCDecisionListener {
    private static final String LIST_RESTAURANT = "LIST_RESTAURANT";
    private static final String IS_FROM_RESTAURANT_SCREEN = "FROM_RESTAURANT_SCREEN";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int MAX_REQUEST_LOCATION_TIMES = 3;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.frag_location_et_search)
    AutoCompleteTextView et_search;
    @BindView(R.id.frag_location_iv_remove_address)
    View iv_remove_address;
    @BindView(R.id.bt_load_more)
    TextView bt_load_more;
    @BindView(R.id.view_detail_marker)
    View view_detail_marker;
    @BindView(R.id.list_marker)
    ViewPager slide_marker;
    @BindView(R.id.detail_marker_iv_remove)
    ImageView detail_marker_iv_remove;
    @SuppressLint("InflateParams")
    View view_default = ((LayoutInflater) getActiveActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
    @SuppressLint("InflateParams")
    View view_click = ((LayoutInflater) getActiveActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_click, null);
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private ArrayList<VendorModel> listVendor;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private LatLng latLngCurrent;
    private PlaceAutoCompleteModel placeAutoCompleteModel;
    private int pageLoad = 1;
    private LatLng lastLatLngClicked = null;
    private VendorModel vendorModel;
    private boolean is_from_restaurant_screen = false;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = places -> {
        if (!places.getStatus().isSuccess()) {
            return;
        }
        // Selecting the first object buffer.
        final Place place = places.get(0);

        placeAutoCompleteModel = new PlaceAutoCompleteModel(pageLoad, place.getLatLng());
        latLngCurrent = placeAutoCompleteModel.latLng;
        requestListPlace(place, placeAutoCompleteModel);

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

    public static LocationScreen getInstance() {
        return new LocationScreen();
    }

    public static LocationScreen getInstance(ArrayList<VendorModel> listRestaurant, boolean is_from_restaurant_screen) {
        LocationScreen screen = new LocationScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_RESTAURANT, listRestaurant);
        bundle.putBoolean(IS_FROM_RESTAURANT_SCREEN, is_from_restaurant_screen);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
        getMyLocation();
    }

    private void getMyLocation() {
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
            latLngCurrent = TCConstant.LOCATION_DEFAULT;
        } else {
            if (TCUtils.isEnableGPS()) {
                latLngCurrent = TCUtils.getGPS(getActiveActivity());
            } else {
                latLngCurrent = TCConstant.LOCATION_DEFAULT;
            }
        }
        setupMap();
    }

    @Override
    public void onPositiveButtonClicked(int id, Object onWhat) {
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

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        listVendor = new ArrayList<>();
        if (bundle != null) {
            listVendor = (ArrayList<VendorModel>) bundle.getSerializable(LIST_RESTAURANT);
            is_from_restaurant_screen = bundle.getBoolean(IS_FROM_RESTAURANT_SCREEN);
        }

        onClick();
        setupAutoCompleteSearch();
    }

    private void setupAutoCompleteSearch() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActiveActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActiveActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        et_search.setThreshold(3);
        et_search.setOnItemClickListener(mAutocompleteClickListener);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("SG")
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActiveActivity(), android.R.layout.simple_list_item_1, autocompleteFilter);
        et_search.setAdapter(mPlaceArrayAdapter);
    }

    private void moveCameraToPosition(LatLng latLng) {
        if (latLng != null) {
            hideKeyBoard();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, TCConstant.MAP_ZOOM_DEFAULT);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    private void closeMarker() {
        ((TCMainActivity) getActiveActivity()).handleBackPressed();
    }

    private void showListRestaurant() {
        if (latLngCurrent != null) {
            if (isAppUser()) {
                if (is_from_restaurant_screen) {
                    putLocationResult();
                } else {
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_RESTAURANT_FROM_LOCATION.getValue(), RestaurantScreen.getInstance(true, latLngCurrent, EnumMgr.TypeRestaurants.Restaurants.getValue(), ""));
                }
            }
            // send location to send notification screen on Shop App
            else {
                putLocationResult();
            }
        }
    }

    public void putLocationResult() {
        setFinishedWithResult(true);
        Intent intent = new Intent();
        intent.putExtra(TCConstant.LAT_LNG_CURRENT, latLngCurrent);
        setFinishedResult(intent);
        popFragment();
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_RESTAURANT_FROM_LOCATION.getValue()) {
            if (finishedResult != null && finishedResult.getExtras() != null) {
                //todo do something if Restaurant screen sent data
            }
        }
    }

    private void setupMap() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map_view, mapFragment).commit();
    }

    private void removeInputSearch() {
        et_search.setText("");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick() {
        iv_back.setOnClickListener(v -> closeMarker());
        tv_done.setOnClickListener(v -> showListRestaurant());
        detail_marker_iv_remove.setOnClickListener(v -> view_detail_marker.setVisibility(View.GONE));
        iv_remove_address.setOnClickListener(v -> removeInputSearch());

        TCUtils.showHideRemoveIconInEditText(et_search, iv_remove_address);
        bt_load_more.setOnClickListener(v -> requestListPlace(null, placeAutoCompleteModel));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        view_detail_marker.setVisibility(View.VISIBLE);
        if (listVendor != null && listVendor.size() > 0) {
            for (int i = 0; i < listVendor.size(); i++) {
                if (listVendor.get(i).getVendorMarker().getMarkerId().equals(marker.getId())) {
//                    listVendor.get(i).getVendorMarker().setLat(marker.getPosition().latitude);
//                    listVendor.get(i).getVendorMarker().setLng(marker.getPosition().longitude);
                    slide_marker.setCurrentItem(i);
                    break;
                }
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDataListMarker() {
        lastLatLngClicked = null;

        MarkerAdapter markerAdapter = new MarkerAdapter(getActiveActivity(), listVendor);
        slide_marker.setAdapter(markerAdapter);
        slide_marker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                vendorModel = listVendor.get(position);
                if (lastLatLngClicked != null) {
                    googleMap.addMarker(new MarkerOptions().position(lastLatLngClicked).icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(), view_default))));
                }
                lastLatLngClicked = vendorModel.getVendorMarker().getLatLng();
                googleMap.addMarker(new MarkerOptions().position(lastLatLngClicked).icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(), view_click))));
                moveCameraToPosition(lastLatLngClicked);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        slide_marker.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gotoVendorDetail();
            }
            return false;
        });
    }

    private void gotoVendorDetail() {
        if (vendorModel != null) {
            addFragment(VendorScreen.getInstance(vendorModel.getId()));
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraChangeListener(this);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        if (listVendor != null && listVendor.size() > 0) {//todo from List restaurant filter
            for (int i = 0; i < listVendor.size(); i++) {
                StringTokenizer locationTokenizer = new StringTokenizer(listVendor.get(i).getLocation(), ",");
                double lat = TCUtils.convertToDouble(locationTokenizer.nextToken());
                double lng = TCUtils.convertToDouble(locationTokenizer.nextToken());
                if (i == 0) {
                    latLngCurrent = new LatLng(lat, lng);
                }
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(), view_default))));
                listVendor.get(i).setVendorMarker(new VendorMarker(marker.getId(), marker.getPosition()));
            }
            initDataListMarker();

        } else {
            //from menu location and home review screen
            placeAutoCompleteModel = new PlaceAutoCompleteModel(pageLoad, latLngCurrent);
            requestListPlace(null, placeAutoCompleteModel);
        }
        moveCameraToPosition(latLngCurrent);
    }

    private void requestListPlace(Place place, PlaceAutoCompleteModel placeAutocompleteModel) {
        /*requestApi(new UserDiscoverGetRecommendForYouRequest(TCUtils.paramsGetVendorList(placeAutocompleteModel.getLatLng()), new APIResponseListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(BaseResponseModel response, RequestTarget requestTarget) {
                googleMap.clear();

                if (place != null) {
                    moveCameraToPosition(place.getLatLng());
                } else {
                    bt_load_more.setVisibility(View.GONE);
                    view_detail_marker.setVisibility(View.GONE);
                }

                latLngCurrent = placeAutocompleteModel.latLng;

                if (isAppUser()) {
                    if (bt_load_more.getVisibility() == View.VISIBLE) {
                        bt_load_more.setVisibility(View.GONE);
                        view_detail_marker.setVisibility(View.GONE);
                    }

                    if (listVendor.size() > 0) {
                        listVendor.clear();
                    }

                    StringTokenizer location;
                    ArrayList<VendorModel> list = ((BaseResultsResponseModel<VendorModel>) response.getResult()).getResults();
                    listVendor.addAll(list);
                    for (int i = 0; i < listVendor.size(); i++) {
                        location = new StringTokenizer(listVendor.get(i).getLocation(), ",");
                        double lat = TCUtils.convertToDouble(location.nextToken());
                        double lng = TCUtils.convertToDouble(location.nextToken());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(), view_default))));
                        listVendor.get(i).setVendorMarker(new VendorMarker(marker.getId(), marker.getPosition()));
                    }

                    initDataListMarker();
                } else {
                    //goto map from send notification screen (shop app)
                    bt_load_more.setVisibility(View.GONE);
                    googleMap.addMarker(new MarkerOptions().position(latLngCurrent).icon(BitmapDescriptorFactory.fromBitmap(CustomView.CustomMarker(getActiveActivity(), view_click))));

                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, RequestTarget requestTarget) {
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
            }
        }));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.LOCATION.getValue()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGPS();
            } else {
                latLngCurrent = TCConstant.LOCATION_DEFAULT;
                setupMap();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActiveActivity(), "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (isAppUser()) {
            LatLng latLngAfterMoveCamera = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
            placeAutoCompleteModel = new PlaceAutoCompleteModel(pageLoad, latLngAfterMoveCamera);
            if (latLngCurrent != null) {
                if (getDistance(latLngCurrent, latLngAfterMoveCamera) > TCConstant.DISTANT_MIN_TO_LOAD_MORE_PLACE) {
                    bt_load_more.setVisibility(View.VISIBLE);
                } else {
                    bt_load_more.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}