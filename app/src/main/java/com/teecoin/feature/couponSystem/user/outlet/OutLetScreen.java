package com.teecoin.feature.couponSystem.user.outlet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.discover.FavouriteAndShareListener;
import com.teecoin.feature.couponSystem.user.discoverVendor.DetailVendorCategoryAdapter;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetListOutletRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class OutLetScreen extends TCCouponBaseFragment implements FavouriteAndShareListener, APIResponseListener {
    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";
    @BindView(R.id.frag_outlet_rcv_vendor)
    TCRecyclerView rcvVendor;
    private VendorDetailModel vendorDetailModel;
    private ArrayList<VendorModel> listVendor;
    private DetailVendorCategoryAdapter detailVendorCategoryAdapter;

    public static OutLetScreen getInstance(VendorDetailModel vendorDetailModel) {
        OutLetScreen screen = new OutLetScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outlet, container, false);
    }

    @Override
    public void onBaseResume() {
        showHeader();
        showFooter();
        showButtonBackToolbar();

    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            vendorDetailModel = (VendorDetailModel) bundle.getSerializable(VENDOR_DETAIL_MODEL);
        }
        if (vendorDetailModel != null) {
            updateTitleHeader(vendorDetailModel.getOutlet_count() > 1 ? String.format(TCUtils.getString(R.string.text_other_outlets), vendorDetailModel.getOutlet_count()) : String.format(TCUtils.getString(R.string.text_other_outlet), vendorDetailModel.getOutlet_count()));

        }
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listVendor = new ArrayList<>();

        detailVendorCategoryAdapter = new DetailVendorCategoryAdapter(LayoutInflater.from(getActiveActivity()), listVendor, false, (view, item, position, clickType) -> {
            if (clickType == EnumMgr.ClickType.Vendor_Favourite) {

                submitFavourite(position, item, this);

            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                getShareVendor(item);

            } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                writeReview(item);

            } else {
                addFragment(VendorScreen.getInstance(item.getId()));
            }
        });

        rcvVendor.setAdapter(detailVendorCategoryAdapter);
        rcvVendor.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(rcvVendor.isLoading()||!rcvVendor.isCanLoadMore())
                    return;

                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int  pastVisiblesItems =  ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount-3)// near to bottom
                    {
                        TCLog.e("rcvVendor.getNextPageIndex() "+rcvVendor.getNextPageIndex());
                        getListVendor();
                    }

                }
            }
        });

        rcvVendor.setNextPageIndex(1);
        getListVendor();

    }

    private void getListVendor() {
        if (vendorDetailModel == null)
            return;
        rcvVendor.setLoading(true);
        requestApi(new CouponUserGetListOutletRequest(vendorDetailModel.getId(), rcvVendor.getNextPageIndex(), getLocation(), this));
    }

    @Override
    public void submitFavoriteSuccess(int position, VendorModel vendorModel) {
        detailVendorCategoryAdapter.setStateIconFavorite(position, !vendorModel.isFavorite());
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.GET_OUTLET_LIST) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null & list.size() > 0) {
                listVendor.addAll(list);

                if (!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext())) {
                    rcvVendor.setCanMore(true);
                    rcvVendor.setNextPageIndex(rcvVendor.getNextPageIndex()+1);
                } else {
                    rcvVendor.setCanMore(false);
                }
                rcvVendor.onLoadMoreComplete();
            }else{
                rcvVendor.setCanMore(false);
            }
            rcvVendor.setLoading(false);
        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.GET_OUTLET_LIST) {
            rcvVendor.setCanMore(false);
            rcvVendor.setLoading(false);
        }
    }
}
