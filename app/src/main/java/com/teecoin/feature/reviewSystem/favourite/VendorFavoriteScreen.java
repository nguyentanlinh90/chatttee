package com.teecoin.feature.reviewSystem.favourite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetFavoriteListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class VendorFavoriteScreen extends TCCouponBaseFragment implements APIResponseListener {

    @BindView(R.id.fragment_favorite_recent_v_container)
    View vContainer;

    @BindView(R.id.fragment_favorite_recent_rcv)
    TCRecyclerView rcvFavorite;

    @BindView(R.id.view_no_data_tv)
    TextView vNoData;

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    private ArrayList<VendorModel> vendorModels;

    private FavoriteAndRecentAdapter favoriteAndRecentAdapter;

    public static VendorFavoriteScreen getInstance() {
        return new VendorFavoriteScreen();

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
        getListFavorite(1);

        //Swipe My Favorite the text of other tab bold when there exists no data both tabs (My favorite)
        vContainer.setOnClickListener(v -> {
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && null != rcvFavorite) {
            vendorModels.clear();
            getListFavorite(1);

        }
    }

    private void initView() {
        rcvFavorite.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        initAdapter();
        rcvFavorite.setAdapter(favoriteAndRecentAdapter);
        loadMore();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(favoriteAndRecentAdapter));
        itemTouchHelper.attachToRecyclerView(rcvFavorite);

        rcvFavorite.setNextPageIndex(1);
        floatingActionButton.hide();

    }

    private void initAdapter() {
        favoriteAndRecentAdapter = new FavoriteAndRecentAdapter(LayoutInflater.from(getActiveActivity()), vendorModels, (view, item, position, clickType) -> {
            if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                writeReview(item);
            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                getShareVendor(item);

            } else if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                ImageView ivFavorite = (ImageView) view;
                submitFavourite(position, item, (position12, vendorModel) ->
                        favoriteAndRecentAdapter.setStateIconFavorite(position12, ivFavorite, !vendorModel.isFavorite()));
            } else {
                addFragment(VendorScreen.getInstance(String.valueOf(item.getId())));
            }
        });
    }

    private void loadMore() {

        rcvFavorite.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                TCLog.e("todo todo");
                if (!rcvFavorite.isCanLoadMore())
                    return;
                rcvFavorite.setNextPageIndex(rcvFavorite.getNextPageIndex() + 1);
                getListFavorite(rcvFavorite.getNextPageIndex());

            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });

    }

    private void getListFavorite(int pageIndex) {
        requestApi(new ReviewUserGetFavoriteListRequest(pageIndex, getLocation(), getCountryCodeModel() != null ? getCountryCodeModel().getCountry_code() : "",
                this));
    }

    private void showViewNoData(boolean isNoData) {

        vNoData.setVisibility(isNoData ? View.VISIBLE : View.GONE);
        rcvFavorite.setVisibility(isNoData ? View.GONE : View.VISIBLE);

        if (vNoData.getVisibility() == View.VISIBLE) {
            String src = TCUtils.getString(R.string.favourite_no_data);
            SpannableString str = new SpannableString(src);
            int index = src.indexOf("#");
            if (index > 0) {
                str.setSpan(new ImageSpan(getContext(), R.drawable.ic_heart_border_no_background, DynamicDrawableSpan.ALIGN_BOTTOM), index, index + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                vNoData.setText(str);
            }

            /* set view NoData at the 1/3 top-center of parent view */
            int hei = getResources().getDisplayMetrics().heightPixels / 3;
            vNoData.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hei));
            /* set view NoData at the 1/3 top-center of parent view */
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        super.onSuccess(response, requestTarget);
        if (((BaseResultsResponseModel) response.getResult()).getResults() != null
                && ((BaseResultsResponseModel) response.getResult()).getResults().size() > 0) {

            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            vendorModels.addAll(list);

            rcvFavorite.onLoadMoreComplete();
            if (!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext())) {
                rcvFavorite.setCanLoadMore(true);
            } else {
                rcvFavorite.setCanLoadMore(false);
            }
            //TCUtils.setNetPageIndexRecycleVew(response, rcvFavorite);
        }
        showViewNoData(vendorModels.size() <= 0);
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showViewNoData(vendorModels.size() <= 0);
    }
}
