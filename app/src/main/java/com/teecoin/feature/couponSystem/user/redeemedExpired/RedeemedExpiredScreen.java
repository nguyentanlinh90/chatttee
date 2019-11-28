package com.teecoin.feature.couponSystem.user.redeemedExpired;

import android.os.Bundle;
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
import com.teecoin.feature.couponSystem.user.myCoupon.UserMyCouponAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponExpiredRedeemedListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class RedeemedExpiredScreen extends TCCouponBaseFragment {

    @BindView(R.id.frg_coupon_user_expired_redeemed_rcv_coupon_data)
    TCRecyclerView rcv_coupon;
    @BindView(R.id.view_no_coupon)
    NestedScrollView view_not_coupon;
    @BindView(R.id.frag_vender_coupon_tv_no_worries)
    TextView tvNoWorries;

    private ArrayList<UserCouponModel> listCoupon;
    private UserMyCouponAdapter adapter;
    private int pageLoad = 1;
    private boolean loadMore;

    public static RedeemedExpiredScreen getInstance() {
        return new RedeemedExpiredScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_redeemed_expired, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showFooter();
        showHeader();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.coupon_title_redeemed_expired));
    }

    @Override
    public void onBindView() {
        super.onBindView();
        setupRecycler();

        registerSingleClick(R.id.frag_vender_coupon_tv_get_more_deals);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        if (v.getId() == R.id.frag_vender_coupon_tv_get_more_deals) {
            replaceFragment(UserCouponScreen.getInstance(), true);
        }
    }

    private void setupRecycler() {
        listCoupon = new ArrayList<>();
        rcv_coupon.setLayoutManager(new LinearLayoutManager(
                getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new UserMyCouponAdapter(LayoutInflater.from(getActiveActivity()), listCoupon, true, (view, item, position, clickType) -> addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(), UserMyCouponDetailScreen.newInstance(item, true)));
        rcv_coupon.setAdapter(adapter);
        rcv_coupon.setNestedScrollingEnabled(false);
        rcv_coupon.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMore) {
                    pageLoad++;
                    getData();
                }
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });

        getData();
    }

    private void getData() {
        requestApi(new CouponUserGetCouponExpiredRedeemedListRequest(pageLoad, 0, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {
                    BaseResultsResponseModel<UserCouponModel> userCouponModels = (BaseResultsResponseModel<UserCouponModel>) response.getResult();
                    listCoupon.addAll(userCouponModels.getResults());
                    rcv_coupon.onLoadMoreComplete();
                    checkData();
                    if (!TCUtils.isEmpty(((BaseResultsResponseModel<UserCouponModel>) response.getResult()).getNext())) {
                        loadMore = true;
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                loadMore = false;
                checkData();
            }
        }));
    }

    private void checkData() {
        if (listCoupon != null && listCoupon.size() > 0) {
            rcv_coupon.setVisibility(View.VISIBLE);
            view_not_coupon.setVisibility(View.GONE);
        } else {
            rcv_coupon.setVisibility(View.GONE);
            view_not_coupon.setVisibility(View.VISIBLE);
            tvNoWorries.setText(Html.fromHtml(TCUtils.getString(R.string.coupons_no_worries_start_shopping)));
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_vender_coupon_tv_get_more_deals);
    }
}
