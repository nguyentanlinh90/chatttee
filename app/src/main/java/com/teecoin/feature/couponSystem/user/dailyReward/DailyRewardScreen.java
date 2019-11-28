package com.teecoin.feature.couponSystem.user.dailyReward;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponCatalogue.RecommendCouponsHorizontalAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.DailyRewardDayModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserDailyRewardRequest;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetListCouponBySortRequest;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGiveAwayRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class DailyRewardScreen extends TCCouponBaseFragment implements APIResponseListener, TCConfirmListener {

    @BindView(R.id.frag_daily_reward_iv_back)
    ImageView iv_back;
    @BindView(R.id.frag_daily_reward_iv_backgroud_gift)
    ImageView iv_backgroud_gift;

    @BindView(R.id.frag_daily_reward_nested_scroll)
    NestedScrollView nested_scroll;
    @BindView(R.id.frag_daily_reward_rcv_day)
    TCRecyclerView rcv_day;
    @BindView(R.id.frag_daily_reward_v_coupon)
    View vCoupon;
    @BindView(R.id.ll_view_all)
    View vAll;

    @BindView(R.id.frag_daily_reward_rcv_coupon)
    TCRecyclerView rcv_coupon;
    private ArrayList<DailyRewardDayModel> listday;
    private DailyRewardDayAdapter dailyRewardDayAdapter;
    private ArrayList<UserCouponCatalogueDataModel> listCouponAndCashVoucher;
    private boolean isReload;

    private RecommendCouponsHorizontalAdapter recommendCouponsHorizontalAdapter;

    public static DailyRewardScreen getInstance() {
        return new DailyRewardScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_reward, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        super.onBindView();

        loadGiftReward(false);
        setupRecyclerView();
        registerSingleClick(R.id.frag_daily_reward_iv_back, R.id.v_back_top_top, R.id.ll_view_all);
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_daily_reward_iv_back, R.id.v_back_top_top, R.id.ll_view_all);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_daily_reward_iv_back:
                //handleBackPressed();
                finishWithResult(RESULT_OK, new Intent());
                break;
            case R.id.v_back_top_top:
                nestedScrollToTop(nested_scroll);
                break;
            case R.id.ll_view_all:
                ((TCMainActivity) getActiveActivity()).openCouponUserScreen();
                break;
        }
    }

    private void loadGiftReward(boolean gif) {
        if (gif)
            // Glide.with(getActiveActivity()).asGif().listener(TCUtils.getRequestFileGif()).load(R.raw.bg_gift_tec_reward).into(iv_backgroud_gift);// not loop
            Glide.with(getActiveActivity()).asGif().load(R.raw.bg_gift_tec_reward).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false).into(iv_backgroud_gift);// loop
        else
            Glide.with(getActiveActivity()).load(TCUtils.getDrawable(R.drawable.ic_banner_give_away)).into(iv_backgroud_gift);// not loop
    }

    private void setupRecyclerView() {
        listday = new ArrayList<>();
        listCouponAndCashVoucher = new ArrayList<>();

         recommendCouponsHorizontalAdapter =
                new RecommendCouponsHorizontalAdapter(LayoutInflater.from(getActiveActivity()),
                        listCouponAndCashVoucher,
                        false,
                        (view, item, position, clickType) -> {
                            addFragment(UserMyCouponDetailScreen.newInstance(item));
                        });
        rcv_coupon.setAdapter(recommendCouponsHorizontalAdapter);


        dailyRewardDayAdapter = new DailyRewardDayAdapter(LayoutInflater.from(getActiveActivity()), listday, new RecycleListener<DailyRewardDayModel>() {
            @Override
            public void onItemClick(View view, DailyRewardDayModel item, int position, EnumMgr.ClickType clickType) {

            }
        });
        rcv_day.setAdapter(dailyRewardDayAdapter);
        getDailyReward();
        getCoupons();

    }

    private void getCoupons() {
        requestApi(new CouponUserGetListCouponBySortRequest(TCUtils.paramsGetCategoryCoupon(getLatLngCurrent(), getCountryCodeModel().getCountry_code(), "7", "", ""), this));
    }

    private void getDailyReward() {
        requestApi(new CouponUserDailyRewardRequest(this));
    }

    private void getGiveaway() {
        requestApi(new CouponUserGiveAwayRequest(this));

    }

    private void showPopUpRecieveTEC(String Amount) {
        loadGiftReward(false);
        new PopupReciveTec(getActiveActivity(), Amount, this).show();

    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.GET_LIST_COUPON_SORT_TYPE) {
            BaseResultsResponseModel<UserCouponCatalogueDataModel> catalogueResponseModel
                    = ((BaseResultsResponseModel<UserCouponCatalogueDataModel>) response.getResult());
            ArrayList<UserCouponCatalogueDataModel> list = catalogueResponseModel.getResults();
            if (list != null && list.size() > 0) {
                listCouponAndCashVoucher.addAll(list);
                rcv_coupon.onLoadMoreComplete();
            }
            vCoupon.setVisibility(listCouponAndCashVoucher.size() > 0 ? View.VISIBLE : View.GONE);

        } else if (requestTarget == CouponRequestTarget.GET_DAILY_REWARD) {
            BaseResultsResponseModel baseResultsResponseModel = (BaseResultsResponseModel) response.getResult();
            ArrayList<DailyRewardDayModel> list = baseResultsResponseModel.getResults();
            dailyRewardDayAdapter.setStatusDate(baseResultsResponseModel.isCan_get_giveaway_today(), baseResultsResponseModel.getCurrent_date() - 1);
            //  dailyRewardDayAdapter.setStatusDate(true,6);

            if (list != null && list.size() > 0) {
                listday.clear();
                listday.addAll(list);

                if (!isReload)
                    // dailyRewardDayAdapter.notifyDataSetChanged();
                    rcv_day.onLoadMoreComplete();
            }
            if (((BaseResultsResponseModel) response.getResult()).isCan_get_giveaway_today()) {
                getGiveaway();
            }

        } else if (requestTarget == CouponRequestTarget.GET_GIVE_AWAY) {
            isReload = true;
            DailyRewardDayModel dayModel = (DailyRewardDayModel) response.getResult();
            loadGiftReward(true);
            if (dayModel != null) {
                new Handler().postDelayed(() -> {
                    showPopUpRecieveTEC(dayModel.getAmount());
                    getDailyReward();
                }, 3500);

            }

        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.GET_LIST_COUPON_SORT_TYPE) {
            vCoupon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        rcv_day.onLoadMoreComplete();
    }

    public void reloadPriceCoupon(){
        if (null != recommendCouponsHorizontalAdapter)
            recommendCouponsHorizontalAdapter.notifyDataSetChanged();
    }
}
