package com.teecoin.feature.reviewSystem.favourite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetRecentListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

import butterknife.BindView;

public class VendorRecentScreen extends TCCouponBaseFragment implements APIResponseListener {

    @BindView(R.id.fragment_favorite_recent_rcv)
    TCRecyclerView rcvRecent;

    @BindView(R.id.view_no_data_tv)
    TextView vNoData;

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    private ArrayList<VendorModel> vendorModels;

    private FavoriteAndRecentAdapter recentAdapter;

    public static VendorRecentScreen getInstance() {
        return new VendorRecentScreen();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_recent, container, false);
    }

    @Override
    public void onBindView() {
        vendorModels = new ArrayList<>();
        initView();
        getRecentList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && null != rcvRecent) {
            vendorModels.clear();
            getRecentList();
        }
    }

    private void initView() {
        initAdapter();
        rcvRecent.setAdapter(recentAdapter);
        floatingActionButton.hide();
    }

    private void initAdapter() {
        rcvRecent.setNextPageIndex(1);

        recentAdapter = new FavoriteAndRecentAdapter(LayoutInflater.from(getActiveActivity()), vendorModels, (view, item, position, clickType) -> {
            if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                writeReview(item);
            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                getShareVendor(item);

            } else if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                ImageView ivFavorite = (ImageView) view;
                submitFavourite(position, item, (position12, vendorModel) ->
                        recentAdapter.setStateIconFavorite(position12, ivFavorite, !vendorModel.isFavorite()));

            } else {
                addFragment(VendorScreen.getInstance(String.valueOf(item.getId())));
            }
        });
        rcvRecent.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rcvRecent.setNextPageIndex(rcvRecent.getNextPageIndex() + 1);
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });
    }

    private void getRecentList() {
        requestApi(new ReviewUserGetRecentListRequest(rcvRecent.getNextPageIndex(), getLocation(), getCountryCodeModel() != null ? getCountryCodeModel().getCountry_code() : "", this));
    }

    private void showViewNoData() {
        vNoData.setVisibility(View.VISIBLE);
        rcvRecent.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        super.onSuccess(response, requestTarget);
        if (((BaseResultsResponseModel) response.getResult()).getResults() != null
                && ((BaseResultsResponseModel) response.getResult()).getResults().size() > 0) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            vendorModels.addAll(list);
            rcvRecent.onLoadMoreComplete();
        }
        if (vendorModels.size() == 0) {
            showViewNoData();
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
//        super.onFail(errorModel, statusCode, requestTarget);
        if (vendorModels.size() == 0) {
            showViewNoData();
        }
    }
}
