package com.teecoin.feature.reviewSystem.vendorDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.couponSystem.user.outlet.OutLetScreen;
import com.teecoin.feature.general.profileShop.ProfileShopScreen;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.feature.reviewSystem.tip.TipCallback;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.feature.reviewSystem.vendorReview.ReviewDetailScreen;
import com.teecoin.feature.reviewSystem.vendorReview.VendorReviewListAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.GoogleReviewResponseModel;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.model.reviewsystem.OpenHoursVendorModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetGoogleReviewRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorReviewRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class VendorDetailScreen extends TCReviewBaseFragment implements RecycleListener<ImageModel>, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnInfoWindowClickListener {

    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";

    private static final int DESCRIPTION_BRIEF_LENGTH = 20;

    @BindView(R.id.frag_vendor_detail_nestedscroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.frag_vendor_detail_v_back_top)
    View v_back_top_top;
    @BindView(R.id.frag_vendor_detail_view_all_review)
    View view_all_review;

    @BindView(R.id.frag_vendor_detail_tv_address)
    TextView tv_address;

    @BindView(R.id.frag_vendor_detail_tv_phone)
    TextView tv_phone;

    @BindView(R.id.frag_vendor_detail_tv_cuisine)
    TextView tv_cuisine;

    @BindView(R.id.frag_vendor_detail_tv_website)
    TextView tv_website;

    @BindView(R.id.frag_vendor_detail_view_description)
    View view_description;

//    @BindView(R.id.frg_vendor_detail_v_more_description)
//    View v_more_description;

    @BindView(R.id.frag_vendor_detail_tv_description)
    TextView tv_description;


    @BindView(R.id.frag_vendor_detail_view_outlet)
    View view_outlet;
    @BindView(R.id.frag_vendor_detail_tv_outlet)
    TextView tv_outlet;
    @BindView(R.id.frag_vendor_detail_view_more_outlet)
    View view_more_outlet;



    @BindView(R.id.frag_vendor_detail_ll_all_open_hours)
    View ll_all_open_hours;

    @BindView(R.id.frag_vendor_detail_tv_all_open_hours)
    TextView tv_all_open_hours;

    @BindView(R.id.frag_vendor_detail_rcv_time_open)
    TCRecyclerView rcv_time_open;

    @BindView(R.id.frag_vendor_detail_rcv_conveniences)
    TCRecyclerView rcv_conveniences;

    @BindView(R.id.frag_vendor_detail_rcv_payments)
    TCRecyclerView rcv_payments;

    @BindView(R.id.frag_vendor_detail_view_phone)
    View view_phone;

    @BindView(R.id.frag_vendor_detail_view_cuisine)
    View view_cuisine;

    @BindView(R.id.frag_vendor_detail_view_avg_price)
    View view_avg_price;

    @BindView(R.id.frag_vendor_detail_tv_avg_price)
    TextView tv_avg_price;

    @BindView(R.id.frag_vendor_detail_view_website)
    View view_website;

    @BindView(R.id.frag_vendor_detail_view_times_open)
    View view_times_open;

    @BindView(R.id.frag_vendor_detail_tv_warning_open_time)
    TextView tvWarningOpenTime;

    @BindView(R.id.frag_vendor_detail_view_payment)
    View view_payment;

    @BindView(R.id.frag_vendor_detail_view_conveniences)
    View view_conveniences;

    @BindView(R.id.frag_vendor_detail_view_prices)
    View view_prices;

    @BindView(R.id.frag_vendor_detail_rcv_prices)
    TCRecyclerView detail_rcv_prices;

    @BindView(R.id.frag_vendor_detail_ll_review_list)
    View vReviewList;

    @BindView(R.id.frag_vendor_detail_rcv_review_list)
    TCRecyclerView rcv_review_list;

    @BindView(R.id.frg_vendor_detail_tv_review_count)
    TextView tv_review_count;

    @BindView(R.id.frg_vendor_detail_v_view_all_review)
    View v_view_all_review;

    private VendorDetailModel detailModel;

    private TimesOpenAdapter adapterTimesOpen;
    private ArrayList<OpenHoursVendorModel> listOpenHours;
    private ArrayList<OpenHoursVendorModel> listOpenHoursToShow;
    private ArrayList<ReviewVendorDetailModel> reviewList;
    private VendorReviewListAdapter adapterListReview;
    private boolean isAlreadyGetGoogleReviewData = false;
    //    @BindView(R.id.view_more_tv_more)
//    TextView view_more_tv_more;
    private LatLng locationVendor;
    private LatLng latLngDefault = TCConstant.LOCATION_DEFAULT;
    private GoogleMap googleMap;
    private String vendorId;

    public static VendorDetailScreen newInstance(VendorDetailModel detailModel) {
        VendorDetailScreen screen = new VendorDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        screen.vendorId = detailModel.getId();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideHeader();
    }

    @Override
    public void onBindView() {
        if (getArguments() != null) {
            detailModel = (VendorDetailModel) getArguments().getSerializable(VENDOR_DETAIL_MODEL);
        }
//        listOpenHours = new ArrayList<>();
        if (detailModel != null) {
            fillData();
        }
        v_back_top_top.setVisibility(View.VISIBLE);
        rcv_review_list.setNestedScrollingEnabled(false);
        rcv_time_open.setNestedScrollingEnabled(false);
        onClick();
        view_all_review.setVisibility(View.GONE);
    }

    private void onClick() {
        registerSingleClick(R.id.frag_vendor_detail_ll_all_open_hours, R.id.frag_vendor_detail_v_back_top, R.id.frag_vendor_detail_view_all_review,R.id.frag_vendor_detail_view_more_outlet);
        tv_phone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tv_phone.getText().toString(), null));
            startActivity(intent);
        });
        // v_view_all_review.setOnClickListener(v -> addFragment(VendorReviewScreen.newInstance(detailModel, true)));
        v_view_all_review.setOnClickListener(v -> viewAllReview());
//        v_more_description.setOnClickListener(v -> {
//            if (tv_description.getText().toString().length() >= DESCRIPTION_BRIEF_LENGTH + 1) {
//                view_more_tv_more.setText(TCUtils.getString(R.string.text_more));
//                tv_description.setText(detailModel.getDescription().substring(0, DESCRIPTION_BRIEF_LENGTH));
//            } else {
//                view_more_tv_more.setText(TCUtils.getString(R.string.text_less));
//                tv_description.setText(detailModel.getDescription());
//            }
//        });

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_vendor_detail_v_back_top:
                nestedScrollToTop(nestedScrollView);
                break;
            case R.id.frag_vendor_detail_view_all_review:
                viewAllReview();
                break;

            case R.id.frag_vendor_detail_ll_all_open_hours:

                showListOpenHours(TCUtils.getString(R.string.text_more).equals(tv_all_open_hours.getText().toString()));

                tv_all_open_hours.setText(TCUtils.getString(
                        TCUtils.getString(R.string.text_more).equals(tv_all_open_hours.getText().toString()) ?
                                R.string.text_less : R.string.text_more));
//                if () {
//
//                    tv_all_open_hours.setText(TCUtils.getString(R.string.text_less));
//
//                } else {
//
//                    tv_all_open_hours.setText(TCUtils.getString(R.string.text_more));
//                }
                break;
            case R.id.frag_vendor_detail_view_more_outlet:
                addFragment(OutLetScreen.getInstance(detailModel));
                break;

        }
    }

    @Override
    public void onBaseDestroyView() {
        super.displayView();
        unregisterSingleClick(R.id.frag_vendor_detail_ll_all_open_hours, R.id.frag_vendor_detail_v_back_top, R.id.frag_vendor_detail_view_all_review,R.id.frag_vendor_detail_view_more_outlet);
    }

    @SuppressLint("DefaultLocale")
    private void fillData() {
        //  Address
        if (detailModel.getAddress() != null && !TCUtils.isEmpty(detailModel.getAddress())) {
            tv_address.setText(detailModel.getAddress());
            tv_address.setVisibility(View.VISIBLE);
        }
        // outlet
        if(detailModel.getOutlet_count()>0){
            view_outlet.setVisibility(View.VISIBLE);
            tv_outlet.setText(detailModel.getOutlet_count()>1?String.format(TCUtils.getString(R.string.text_other_outlets),detailModel.getOutlet_count()):String.format(TCUtils.getString(R.string.text_other_outlet),detailModel.getOutlet_count()));
        }else{
            view_outlet.setVisibility(View.GONE);
        }
        //  Cuisine

        if (detailModel.getCuisineList() != null && detailModel.getCuisineList().size() > 0) {
            StringBuilder cuisines = new StringBuilder();
            if (detailModel.getCuisineList().size() > 5) {
                for (int i = 0; i < 5; i++) {
                    if (i < 4) {
                        cuisines.append(detailModel.getCuisineList().get(i)).append(",").append(" ");

                    } else {
                        cuisines.append(detailModel.getCuisineList().get(i));
                    }

                }

            } else {
                for (int i = 0; i < detailModel.getCuisineList().size(); i++) {
                    if (i < detailModel.getCuisineList().size() - 1) {
                        cuisines.append(detailModel.getCuisineList().get(i)).append(",").append(" ");
                    } else {
                        cuisines.append(detailModel.getCuisineList().get(i));
                    }
                }

            }
            tv_cuisine.setText(cuisines);
            view_cuisine.setVisibility(View.VISIBLE);
        }
        //  Phone
        if (detailModel.getPhone() != null && !TCUtils.isEmpty(detailModel.getPhone())) {
            // tv_phone.setText(Html.fromHtml("<u>" + detailModel.getPhone() + "</u>"));
            tv_phone.setText(detailModel.getPhone());
            view_phone.setVisibility(View.VISIBLE);
        }
        //  website
        if (detailModel.getWebsite() != null && !TCUtils.isEmpty(detailModel.getWebsite())) {
            tv_website.setText(detailModel.getWebsite());
            view_website.setVisibility(View.VISIBLE);
        }
        if (!TCUtils.isEmpty(detailModel.getAvgPrice())) {
            tv_avg_price.setText(detailModel.getAvgPrice());
            view_avg_price.setVisibility(View.VISIBLE);
        } else {
            view_avg_price.setVisibility(View.GONE);
        }
        if (detailModel.getLocation() != null) {
            String location = detailModel.getLocation();
            if (!TCUtils.isEmpty(location)) {
                locationVendor=(TCUtils.vendorGetLocation(detailModel.getLocation()));
            }
        }
        // Descriptionv_more_description
        if (!TCUtils.isEmpty(detailModel.getDescription())) {
            tv_description.setText(detailModel.getDescription());
            view_description.setVisibility(View.VISIBLE);
            //v_more_description.setVisibility(detailModel.getDescription().length() <= DESCRIPTION_BRIEF_LENGTH ? View.INVISIBLE : View.VISIBLE);
        } else {
            view_description.setVisibility(View.GONE);
//            v_more_description.setVisibility(View.INVISIBLE);
        }

        // todo Prices
//        if (detailModel.getTag_prices() != null && detailModel.getTag_prices().size() > 0) {
//            PricesAdapter pricesAdapter = new PricesAdapter(LayoutInflater.from(getActiveActivity()), detailModel.getTag_prices(), (view, item, position, clickType) -> {
//            });
//            detail_rcv_prices.setAdapter(pricesAdapter);
//            view_prices.setVisibility(View.VISIBLE);
//        }

        //Times Open
        if (detailModel.getOpen_hours() != null && detailModel.getOpen_hours().size() > 0) {

            listOpenHours = new ArrayList<>();
            listOpenHours.addAll(detailModel.getOpen_hours());

            listOpenHoursToShow = new ArrayList<>();

            adapterTimesOpen = new TimesOpenAdapter(LayoutInflater.from(getActiveActivity()), listOpenHoursToShow, (view, item, position, clickType) -> {

            });

            rcv_time_open.setAdapter(adapterTimesOpen);

            view_times_open.setVisibility(View.VISIBLE);

            tvWarningOpenTime.setVisibility(detailModel.getIs_headquarter() ? View.VISIBLE : View.GONE);
        }
        //  Payment
        if (detailModel.getPayments() != null && detailModel.getPayments().size() > 0) {
            PaymentsAdapter paymentsAdapter = new PaymentsAdapter(LayoutInflater.from(getActiveActivity()), detailModel.getPayments(), (view, item, position, clickType) -> {
            });
            rcv_payments.setAdapter(paymentsAdapter);
            view_payment.setVisibility(View.VISIBLE);
        }
        //  Conveniences
        if (detailModel.getConveniences() != null && detailModel.getConveniences().size() > 0) {
            ConveniencesAdapter conveniencesAdapter = new ConveniencesAdapter(LayoutInflater.from(getActiveActivity()), detailModel.getConveniences(), (view, item, position, clickType) -> {
            });
            rcv_conveniences.setAdapter(conveniencesAdapter);
            view_conveniences.setVisibility(View.VISIBLE);
        }
        reviewList = new ArrayList<>();
        adapterListReview = new VendorReviewListAdapter(LayoutInflater.from(getActiveActivity()),
                reviewList,true,
                (view, item, position, clickType) -> {
                    if (clickType == EnumMgr.ClickType.ReviewList_LikeClicked) {
                        clickLikeAndTip(item,position);
                    }else{
                        if(item.getImages() != null && item.getImages().size() > 0){
                            addFragment(ReviewDetailScreen.newInstance(item, position,0));
                        }

                    }

                });

        rcv_review_list.setAdapter(adapterListReview);
        getReviews();

        showListOpenHours(false);
    }
    private void clickLikeAndTip(ReviewVendorDetailModel data,int position){
        boolean tipbefore = false;
        if (!TCUtils.isEmpty(data.getTip_of_user())) {
            float tip = Float.parseFloat(data.getTip_of_user());
            if (tip > 0)
                tipbefore = true;
        }
        if (!TCUtils.isMyselfReview(data.getAuthor().getPublic_key())) {
            showPopupTip(tipbefore,data, position);

        } else {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
        }
    }
    private void showPopupTip(boolean tipbefore,ReviewVendorDetailModel data ,int position) {
        DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipbefore, data.getAuthor().getPublic_key(), data.getId(), new TipCallback() {
            @Override
            public void tipAmount(float amount) {
                if (amount > 0) {
                    if (data.getTip_amount() != null) {
                        float tip_amount = Float.parseFloat(data.getTip_amount());
                        data.setTip_amount((amount + tip_amount) + "");
                    }
                    if (data.getTip_of_user() != null) {
                        float tip_user = Float.parseFloat(data.getTip_of_user());
                        data.setTip_of_user((amount + tip_user) + "");
                    }

                    if (!data.isIs_like()) {
                        if (!TCUtils.isEmpty(data.getLike_count())) {
                            data.setLike_count(String.valueOf(Integer.parseInt(data.getLike_count()) + 1));
                        }
                        data.setIs_like(true);
                    }
                    if(reviewList!=null&&reviewList.size()>0){
                        if(position<reviewList.size()){
                            reviewList.get(position).setTip_amount(data.getTip_amount());
                            reviewList.get(position).setTip_of_user(data.getTip_of_user());
                            reviewList.get(position).setLike_count(data.getLike_count());
                        }
                    }adapterListReview.notifyItemChanged(position);
                }
            }
        });
        dialogTipVendor.show();
        dialogTipVendor.setCanceledOnTouchOutside(false);
    }
    public void updateItemList(int position) {
        if (adapterListReview != null) {
            adapterListReview.updateLikeItem(position);
        }
    }
    private void showListOpenHours(boolean isShowMore) {

        if (listOpenHoursToShow.size() > 0) {

            listOpenHoursToShow.clear();
        }

        if (adapterTimesOpen != null) {

            if (isShowMore) {

                listOpenHoursToShow.addAll(listOpenHours);

            } else {

                for (int i = 0; i < listOpenHours.size(); i++) {

                    if (TCDateUtility.isCurrentDayOfWeek(i)) {

                        listOpenHoursToShow.add(listOpenHours.get(i));
                    }
                }
            }
            rcv_time_open.onLoadMoreComplete();
        }
    }

    @Override
    public void onItemClick(View view, ImageModel item, int position, EnumMgr.ClickType clickType) {
    }

    private void getReviews() {

        requestApi(new ReviewShopGetVendorReviewRequest(detailModel.getId(), 1, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<ReviewVendorDetailModel> list = ((BaseResultsResponseModel<ReviewVendorDetailModel>) response.getResult()).getResults();
                if (list != null && list.size() > 0) {
                    reviewList.addAll(list);
                    for (ReviewVendorDetailModel reviewVendorDetailModel : reviewList) {
                        reviewVendorDetailModel.setName(detailModel.getName());
                    }
                }
                getGoogleReview();

                vReviewList.setVisibility(reviewList != null && reviewList.size() > 0 ? View.VISIBLE : View.GONE);
                rcv_review_list.onLoadMoreComplete();
                view_all_review.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                rcv_review_list.onLoadMoreComplete();
                getGoogleReview();
            }
        }));
    }

    private void getGoogleReview() {
        if (!isAlreadyGetGoogleReviewData && !TCUtils.isEmpty(detailModel.getPlace_id())) {
            requestApi(new ReviewGetGoogleReviewRequest(detailModel.getId(), new APIResponseListener() {
                @Override
                public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                    GoogleReviewResponseModel googleReviewModel = (GoogleReviewResponseModel) response.getResult();
                    if (googleReviewModel != null && googleReviewModel.getReviews() != null && googleReviewModel.getReviews().size() > 0) {
                        if (reviewList != null&&googleReviewModel.getReviews().size()>0) {
                            for (int i = 0; i < googleReviewModel.getReviews().size(); i++) {
                                reviewList.add(new ReviewVendorDetailModel(googleReviewModel.getReviews().get(i)));
                            }

                        }

                        vReviewList.setVisibility(reviewList != null && reviewList.size() > 0 ? View.VISIBLE : View.GONE);
                        rcv_review_list.onLoadMoreComplete();
                    }
                }

                @Override
                public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                }
            }));
            isAlreadyGetGoogleReviewData = true;
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleMap != null) {
            googleMap.clear();
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

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
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
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActiveActivity(), R.raw.map_style));
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
//
//        int height = TCScreenSize.getHeight((AppCompatActivity) getActiveActivity());
//        googleMap.setPadding(0,0,0,height/3);

        if (locationVendor != null) {
            moveCameraToPosition(locationVendor);
        } else {
            moveCameraToPosition(latLngDefault);
        }
        googleMap.setOnMapClickListener(latLng -> TCUtils.gotoDirection(locationVendor));
    }

    private void moveCameraToPosition(LatLng latLng) {
        if (latLng != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(!TCUtils.isEmpty(detailModel.getName()) ? detailModel.getName() : "here")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
            marker.showInfoWindow();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, TCConstant.MAP_ZOOM_DEFAULT);
            googleMap.animateCamera(cameraUpdate);
        }

    }

    private void viewAllReview() {
        if (getTopFragment() != null) {
            if (getTopFragment() instanceof VendorScreen) {
                VendorScreen vendorScreen = (VendorScreen) getTopFragment();
                if (vendorScreen != null) {
                    vendorScreen.openTabReviews();
                }
            }else if(getTopFragment() instanceof ProfileShopScreen){
                ProfileShopScreen profileShopScreen = (ProfileShopScreen) getTopFragment();
                if (profileShopScreen != null) {
                    profileShopScreen.openTabReviews();
                }
            }
        }

    }

    public String getVendorId() {
        return vendorId;
    }
}