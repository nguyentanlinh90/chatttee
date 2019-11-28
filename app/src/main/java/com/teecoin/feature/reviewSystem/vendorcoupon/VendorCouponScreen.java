package com.teecoin.feature.reviewSystem.vendorcoupon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.couponSystem.user.couponCatalogue.UserCouponCatalogueVerticalAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetVendorCouponRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class VendorCouponScreen extends TCCouponBaseFragment {

    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";
    @BindView(R.id.frag_vender_coupon_rcv_coupon)
    TCRecyclerView rcv_catalogue;
    @BindView(R.id.view_no_coupon)
    View view_no_coupon;
    @BindView(R.id.frag_vender_coupon_tv_no_worries)
    TextView tv_no_worries;
    @BindView(R.id.frag_vender_coupon_tv_get_more_deals)
    TextView tv_get_more_deals;

    private  UserCouponCatalogueVerticalAdapter adapter;

    private VendorDetailModel detailModel;
    private ArrayList<UserCouponCatalogueDataModel> couponDetailList;

    public static VendorCouponScreen getInstance(VendorDetailModel detailModel) {
        VendorCouponScreen screen = new VendorCouponScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_coupon, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            detailModel = (VendorDetailModel) bundle.getSerializable(VENDOR_DETAIL_MODEL);

            tv_no_worries.setText(Html.fromHtml(TCUtils.getString(R.string.coupons_no_worries_start_shopping)));
            setupRecycler();
        }
        registerSingleClick(R.id.frag_vender_coupon_tv_get_more_deals);
    }

    private void setupRecycler() {
        couponDetailList = new ArrayList<>();
        rcv_catalogue.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));

         adapter = new UserCouponCatalogueVerticalAdapter(LayoutInflater.from(getActiveActivity()), couponDetailList, true, true,
                (view, item, position, clickType) -> {
                    if (!isAppUser())
                        return;
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(), UserMyCouponDetailScreen.newInstance(item));
                });
        rcv_catalogue.setAdapter(adapter);
        rcv_catalogue.setNestedScrollingEnabled(true);
        rcv_catalogue.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestData();
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });
        requestData();
    }


    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_vender_coupon_tv_get_more_deals:
                ((TCMainActivity) getActiveActivity()).selectCoupon();
                replaceFragment(UserCouponScreen.getInstance(), true);
                break;

        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_vender_coupon_tv_get_more_deals);

    }

    private void requestData() {
        requestApi(new ReviewUserGetVendorCouponRequest(detailModel.getId(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<UserCouponCatalogueDataModel> list =
                        ((BaseResultsResponseModel) response.getResult()).getResults();
                if (list != null && list.size() > 0) {
                    couponDetailList.addAll(list);
                    rcv_catalogue.onLoadMoreComplete();
                }
                rcv_catalogue.setLimit(((BaseResultsResponseModel) response.getResult()).getCount());
                showViewNoData(couponDetailList.size() <= 0);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showViewNoData(couponDetailList.size() <= 0);
            }
        }));
    }

    private void showViewNoData(boolean isNoData) {
        if (isNoData) {
            view_no_coupon.setVisibility(View.VISIBLE);
            tv_get_more_deals.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
            rcv_catalogue.setVisibility(View.GONE);
        } else {
            view_no_coupon.setVisibility(View.GONE);
            rcv_catalogue.setVisibility(View.VISIBLE);
        }
    }

    public void reloadPriceCoupon() {
        if (null != adapter)
            adapter.notifyDataSetChanged();
    }
}
