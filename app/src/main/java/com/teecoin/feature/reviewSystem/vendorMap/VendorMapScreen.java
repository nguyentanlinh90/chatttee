package com.teecoin.feature.reviewSystem.vendorMap;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.StringTokenizer;

import butterknife.BindView;

public class VendorMapScreen extends TCReviewBaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;

    private LatLng latLngDefault = TCConstant.LOCATION_DEFAULT;
    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";
    private VendorDetailModel detailModel;
    private LatLng locationVendor;
    @BindView(R.id.frg_vendor_map_iv_directions)
    ImageView iv_directions;
    public static VendorMapScreen newInstance(VendorDetailModel detailModel) {
        VendorMapScreen screen = new VendorMapScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_map, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();

    }

    @Override
    public void onBindView() {
        //TCLog.e("onBindView");
        Bundle bundle = getArguments();
        if (bundle != null) {
            detailModel = (VendorDetailModel) bundle.getSerializable(VENDOR_DETAIL_MODEL);
        }
        if (detailModel != null) {
            if (detailModel.getLocation() != null) {
                String location = detailModel.getLocation();
                if (TCUtils.isEmpty(location)) {
                    return;
                }
                StringTokenizer tokens = new StringTokenizer(location, ",");
                double lat = TCUtils.convertToDouble(tokens.nextToken());
                double lng = TCUtils.convertToDouble(tokens.nextToken());
                locationVendor = new LatLng(lat, lng);
            }
        }
        iv_directions.setOnClickListener(v -> gotoDirection());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupMap();
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        //        // for Map tool disable
       // googleMap.getUiSettings().setMapToolbarEnabled(true);
        //for Location  Button enable on Google Map
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setIndoorEnabled(true);
       // googleMap.setMapType(No);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);


        int height = TCScreenSize.getHeight(getActiveActivity());
        googleMap.setPadding(0,0,0,height/3);

        if (locationVendor != null) {
            moveCameraToPosition(locationVendor);
        } else {
            moveCameraToPosition(latLngDefault);
        }


    }

    private void moveCameraToPosition(LatLng latLng) {
        if (latLng != null) {
        Marker marker=  googleMap.addMarker(new MarkerOptions().position(latLng).title(!TCUtils.isEmpty(detailModel.getName())?detailModel.getName():"here")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
            marker.showInfoWindow();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, TCConstant.MAP_ZOOM_DEFAULT);
            googleMap.animateCamera(cameraUpdate);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActiveActivity(), "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        marker.hideInfoWindow();

    }
    private void gotoDirection(){
        if(locationVendor!=null){
            if(isGoogleMapsInstalled()){
                //Uri gmmIntentUri = Uri.parse("google.navigation:q=28.5675,77.3260");
                Uri uri = Uri.parse("google.navigation:q="+String.valueOf(locationVendor.latitude)+","+String.valueOf(locationVendor.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }else{
                TCUtils.showToast("Please install Google Maps");
            }

        }

    }
    private boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getActiveActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
