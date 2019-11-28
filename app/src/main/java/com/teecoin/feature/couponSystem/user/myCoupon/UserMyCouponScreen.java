package com.teecoin.feature.couponSystem.user.myCoupon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class UserMyCouponScreen extends TCCouponBaseFragment {
    @BindView(R.id.frg_coupon_user_my_coupon_refresh_view)
    TCSwipeRefreshLayout refresh_view;

    @BindView(R.id.frg_coupon_user_my_coupon_rcv_coupon_data)
    TCRecyclerView rcv_user_coupon;

    @BindView(R.id.view_no_coupon)
    NestedScrollView view_not_coupon;

    @BindView(R.id.frg_coupon_user_my_coupon_nested_scroll)
    NestedScrollView nested_scroll;

    @BindView(R.id.frag_vender_coupon_tv_no_worries)
    TextView tv_no_worries;

    private ArrayList<UserCouponModel> listCoupon;
    private UserMyCouponAdapter adapter;
    private FilterCouponModel filterCouponModel;
//    private boolean loadMore;

    public static UserMyCouponScreen newInstance() {
        return new UserMyCouponScreen();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_user_my_coupon, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();

    }

    @Override
    public void onBindView() {
        refreshData();

        registerSingleClick(R.id.frag_vender_coupon_tv_get_more_deals);

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        if (v.getId() == R.id.frag_vender_coupon_tv_get_more_deals) {
            replaceFragment(UserCouponScreen.getInstance(), true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setupRecycler();
        }
    }

    private void setupRecycler() {

        refresh_view.setEnabled(false);
        listCoupon = new ArrayList<>();
        rcv_user_coupon.setLayoutManager(new LinearLayoutManager(
                getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new UserMyCouponAdapter(LayoutInflater.from(getActiveActivity()), listCoupon, false,
                (view, item, position, clickType) ->
                        addFragmentForResult(
                                EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(),
                                UserMyCouponDetailScreen.newInstance(item, false)));
        rcv_user_coupon.setAdapter(adapter);

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
                    if (rcv_user_coupon.isCanLoadMore()) {
                        getData();
                    }
                }, 300);
            }
        });

//        rcv_user_coupon.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (rcv_user_coupon.isCanLoadMore()) {
//                    TCLog.e("load more");
//                    getData();
//                }
//            }
//
//            @Override
//            public boolean shouldOverrideRefresh() {
//                return false;
//            }
//        });
        rcv_user_coupon.setNextPageIndex(1);
        getData();
    }

    private void getData() {
        requestApi(new CouponUserGetCouponListRequest(rcv_user_coupon.getNextPageIndex(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {

                    if (refresh_view.isRefreshing()) {
                        refresh_view.setRefreshing(false);
                    }
                    refresh_view.setEnabled(true);

                    BaseResultsResponseModel<UserCouponModel> userCouponModels = (BaseResultsResponseModel<UserCouponModel>) response.getResult();
                    listCoupon.addAll(userCouponModels.getResults());
                    if (!TCUtils.isEmpty(((BaseResultsResponseModel<UserCouponModel>) response.getResult()).getNext())) {
                        rcv_user_coupon.setCanMore(true);
                        rcv_user_coupon.setNextPageIndex(rcv_user_coupon.getNextPageIndex() + 1);
                    } else {
                        rcv_user_coupon.setCanMore(false);
                    }
                    rcv_user_coupon.onLoadMoreComplete();
                    checkData();
                    rcv_user_coupon.setLimit(((BaseResultsResponseModel) response.getResult()).getCount());
                    if (rcv_user_coupon.getNextPageIndex() == 1) {
                        rcv_user_coupon.scrollToPosition(0);
                    }

                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                if (refresh_view.isRefreshing()) {
                    refresh_view.setRefreshing(false);
                }
                rcv_user_coupon.setCanMore(false);
                refresh_view.setEnabled(true);
                checkData();
            }
        }));
    }

    private void checkData() {
        if (listCoupon != null && listCoupon.size() > 0) {
            rcv_user_coupon.setVisibility(View.VISIBLE);
            view_not_coupon.setVisibility(View.GONE);
        } else {
            rcv_user_coupon.setVisibility(View.GONE);
            view_not_coupon.setVisibility(View.VISIBLE);
            tv_no_worries.setText(Html.fromHtml(TCUtils.getString(R.string.coupons_no_worries_start_shopping)));
        }
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue()
                && finishedResultCode == RESULT_OK) {//Purchase coupon success
            rcv_user_coupon.setNextPageIndex(1);
            getData();
        }
    }

    private void refreshData() {
        refresh_view.setOnRefreshListener(() -> {

            listCoupon.clear();
            rcv_user_coupon.setNextPageIndex(1);
            getData();
        });
    }

    public void reloadData() {
        if (refresh_view != null && rcv_user_coupon != null) {
            setupRecycler();
        }
    }


    private void showViewNoData(boolean isNoData) {
        if (isNoData) {
            view_not_coupon.setVisibility(View.VISIBLE);
            rcv_user_coupon.setVisibility(View.GONE);
        } else {
            view_not_coupon.setVisibility(View.GONE);
            rcv_user_coupon.setVisibility(View.VISIBLE);
        }
    }


}