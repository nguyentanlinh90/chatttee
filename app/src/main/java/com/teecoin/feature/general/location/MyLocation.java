package com.teecoin.feature.general.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.discover.DiscoverScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.DiscoverUserGetCountryRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_NAME;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_CURRENCY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_SELECTED;
import static core.base.BaseApplication.getActiveActivity;

public class MyLocation implements LocationListener {
    private Context mContext;
    private LocationManager locationManager;
    private LatLng latLng;

    private CountryCodeModel countryCodeModel = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, DEFAULT_SELECTED);

    private boolean reloadDiscover;

    private ArrayList<CountryCodeModel> listCountry;


    public MyLocation(Context mContext) {
        this.mContext = mContext;

        listCountry = new ArrayList<>();
    }

    public void getLocationListener() {
        LatLng getLatLng = TCUtils.getGPS(mContext);
        if (getLatLng != null) {
            setLatLng(getLatLng);
        } else {
            try {
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void setLatLng(LatLng latLng) {

        this.latLng = latLng;
        Fragment fragment = ((TCMainActivity) getActiveActivity()).getTopFragment();
        if (fragment instanceof DiscoverScreen)
            if (this.reloadDiscover) {
                DiscoverScreen screen = (DiscoverScreen) fragment;
                screen.getCountryCode(true);
                this.reloadDiscover = false;
            }

        getCountryCode();
    }

    public void getCountryCode() {
        if (getListCountry().size() > 0)
            return;
        ((TCMainActivity) getActiveActivity()).requestApi(new DiscoverUserGetCountryRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == CouponRequestTarget.GET_LIST_COUNTRY) {

                    ArrayList<CountryCodeModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                    if (list != null && list.size() > 0) {
                        listCountry.clear();
                        listCountry.addAll(list);
                        CountryCodeModel currentCountryCode = TCUtils.checkCountryCode(listCountry, getLocation());
                        setCountryCodeModel(currentCountryCode);
                        for (int i = 0; i < listCountry.size(); i++) {

                            if (currentCountryCode.getCountry_code().toUpperCase().equals(listCountry.get(i).getCountry_code().toUpperCase())) {

                                listCountry.get(i).setSelected(true);
                            }
                        }
                    } else {
                        //set default is SG - Singapore
                        CountryCodeModel currentCountryCode = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, DEFAULT_SELECTED);

                        if (listCountry.size() == 0) {

                            listCountry.add(currentCountryCode);
                        }
                    }
                    setListCountry(listCountry);

                    setCountryCodeModel();
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
            }
        }));
    }

    public void removeUpdatesLocationManager() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setReloadDiscoverScreen() {
        this.reloadDiscover = true;
    }

    public void setCountryCodeModel() {
        if (getListCountry() != null && getListCountry().size() > 0) {
            for (CountryCodeModel country : getListCountry()) {
                if (country.isSelected())
                    setCountryCodeModel(country);
            }
        }
    }

    public CountryCodeModel getCountryCodeModel() {
        return countryCodeModel;
    }

    public void setCountryCodeModel(CountryCodeModel countryCodeModel) {
        this.countryCodeModel = countryCodeModel;
    }

    public ArrayList<CountryCodeModel> getListCountry() {
        return listCountry;
    }

    public void setListCountry(ArrayList<CountryCodeModel> listCountry) {
        this.listCountry = listCountry;
    }

    public LatLng getLocation() {
        return latLng != null ? latLng : TCConstant.LOCATION_DEFAULT;
    }
}
