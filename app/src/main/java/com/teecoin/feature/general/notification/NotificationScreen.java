package com.teecoin.feature.general.notification;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.feature.couponSystem.user.checkinNotification.CheckInNotificationScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.payment.shopPaymentDetail.ShopPaymentDetailScreen;
import com.teecoin.feature.payment.userPaymentAndCoinbackDetail.UserPaymentAndCoinBackDetailScreen;
import com.teecoin.feature.reviewSystem.detailReview.UserReviewDetailScreen;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.feature.walletSystem.convertToTecDetails.ConvertToTecDetailScreen;
import com.teecoin.feature.walletSystem.historyPaymentDetail.TransferMoneyDetailScreen;
import com.teecoin.feature.walletSystem.withdrawDetails.WithdrawDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.general.ReadNotificationRequestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralNotificationReadRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetNotificationFilterRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetNotificationRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class NotificationScreen extends TCBaseFragment implements APIResponseListener {

    @BindView(R.id.frag_notification_rl_transactions)
    View rl_transactions;
    @BindView(R.id.frag_notification_rl_coupons)
    View rl_coupons;
    @BindView(R.id.frag_notification_rl_general)
    View rl_general;
    @BindView(R.id.frag_notification_tv_general)
    TextView tv_general;

    @BindView(R.id.frg_notification_rcv_notify)
    TCRecyclerView rcv_notify;
    private ArrayList<DataPushNotificationModel> listNotification;
    private NotificationAdapter adapter;
    private int pageLoad = 1;
    private boolean filter = false;

    public static NotificationScreen getInstance() {
        return new NotificationScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        hideButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.text_notifications).toUpperCase());
        hideViewQrCode();
        hideButtonAddCoupon();
        showFooter();
        showReadAllNotification(true);
        ((TCMainActivity) getActiveActivity()).checkNewNotification();
    }

    @Override
    public void onBindView() {
        rl_general.setSelected(true);
        tv_general.setText(isAppUser() ? TCUtils.getString(R.string.general) : TCUtils.getString(R.string.text_all));
        setupRecycle();
        onClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //   showReadAllNotification(false);
    }

    private void setupRecycle() {
        //type = EnumMgr.FilterNotification.General.getValue();// make default general
        filter = true;// make default general
        listNotification = new ArrayList<>();
        rcv_notify.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NotificationAdapter(LayoutInflater.from(getActiveActivity()), listNotification, (view, item, position, clickType)
                -> {
            if (!adapter.isClickedItem()) {
                adapter.setClickedItem(true);
                gotoDetail(item, position);
            }
        }

        );
        rcv_notify.setAdapter(adapter);
        rcv_notify.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getFilterNotification(getFilter());
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });

        getFilterNotification(getFilter());
    }

    private void getAllData() {//same with type general
        requestApi(new ReviewUserGetNotificationRequest(pageLoad, this));

        adapter.notifyDataSetChanged();
    }

    private void onClick() {
        registerSingleClick(rl_general, rl_transactions, rl_coupons);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_notification_rl_general:
            case R.id.frag_notification_rl_transactions:
            case R.id.frag_notification_rl_coupons:
                if (v.isSelected()) {
                    return;
                } else {
                    updateStatusFilter(rl_general, rl_coupons, rl_transactions);
                    v.setSelected(true);
                    refreshFilter(getFilter());
                }
                break;
        }
    }

    private void updateStatusFilter(View... views) {
        for (View v : views) {
            v.setSelected(false);
        }
    }

    private EnumMgr.FilterNotification getFilter() {
        if (rl_transactions.isSelected())
            return EnumMgr.FilterNotification.Transactions;
        if (rl_coupons.isSelected())
            return EnumMgr.FilterNotification.Coupons;
        if (rl_general.isSelected())
            return EnumMgr.FilterNotification.General;
        return EnumMgr.FilterNotification.General;
    }

    private void getFilterNotification(EnumMgr.FilterNotification filterNotification) {
        requestApi(new ReviewGetNotificationFilterRequest(
                RealmController.getInstance().getData(AccountModel.class).getUuid(), filterNotification.getValue(), pageLoad,
                isAppUser() ? ReviewRequestTarget.USER_GET_NOTIFY_FILTER : ReviewRequestTarget.SHOP_GET_NOTIFY_FILTER,
                this));
    }

    private void refreshAllData() {
        filter = false;
        if (listNotification != null && listNotification.size() > 0) {
            listNotification.clear();
        }
        rcv_notify.onLoadMoreComplete();
        pageLoad = 1;
        getAllData();// same type general
    }

    private void refreshFilter(EnumMgr.FilterNotification filterNotification) {
        filter = true;
        if (listNotification != null && listNotification.size() > 0) {
            listNotification.clear();
        }
        //  rcv_notify.onLoadMoreComplete();
        pageLoad = 1;
        getFilterNotification(filterNotification);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.USER_GET_NOTIFY
                || requestTarget == ReviewRequestTarget.USER_GET_NOTIFY_FILTER
                || requestTarget == ReviewRequestTarget.SHOP_GET_NOTIFY
                || requestTarget == ReviewRequestTarget.SHOP_GET_NOTIFY_FILTER) {
            ArrayList<DataPushNotificationModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                pageLoad++;
                listNotification.addAll(list);
            }
            rcv_notify.onLoadMoreComplete();

        } else if (requestTarget == GeneralRequestTarget.USER_READ_NOTIFY || requestTarget == GeneralRequestTarget.SHOP_READ_NOTIFY) {
            // TCLog.e("read success");
        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        rcv_notify.onLoadMoreComplete();
    }

    private void gotoDetail(DataPushNotificationModel data, int position) {
        PushNotificationModel pushNotificationModel = new PushNotificationModel();
        pushNotificationModel.setData(data);
        if (!TCUtils.isEmpty(pushNotificationModel.getData().getTransaction_id())) {
            if (data.getType().equals(EnumMgr.PushNotification.Reward.getValue())
                    || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.TransferMoney.getValue())
            ) {
                addFragment(TransferMoneyDetailScreen.getInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Payment.getValue())
                    || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CoinBack.getValue())) {
                addFragment(isAppUser() ? UserPaymentAndCoinBackDetailScreen.getInstance(pushNotificationModel) : ShopPaymentDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id()));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Tip.getValue())) {
                addFragment(UserReviewDetailScreen.getInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue())
                    || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Event.getValue())) {
                addFragment(WebViewScreen.getInstance(pushNotificationModel.getData().getUrl(), pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue()) ? TCUtils.getString(R.string.review_promotion_detail_title) : TCUtils.getString(R.string.events)));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Referral.getValue())) {
                //showMessage("Referral");
                TCLog.d("Referral ");
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Coupon.getValue())) {
                pushNotificationModel.getData().setCoupon_id(TCUtils.getIDFromURL(pushNotificationModel.getData().getUrl()));
                addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
                addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CheckInCoupon.getValue())) {
                addFragment(CheckInNotificationScreen.getInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.ConvertToTEC.getValue()) || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Incoming.getValue())) {
                addFragment(ConvertToTecDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id(), pushNotificationModel.getData().getType()));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.WithdrawRequest.getValue()) || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.WithdrawCompleted.getValue())) {
                addFragment(WithdrawDetailScreen.getInstance(pushNotificationModel.getData().getTransaction_id(), pushNotificationModel.getData().getType()));
            }
        } else {
            if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue())
                    || pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Event.getValue())) {
                addFragment(WebViewScreen.getInstance(pushNotificationModel.getData().getUrl(), pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Promotion.getValue()) ? TCUtils.getString(R.string.review_promotion_detail_title) : TCUtils.getString(R.string.events)));
                // addFragment(WebViewScreen.getInstance(pushNotificationModel.getData().getUrl(), pushNotificationModel.getData().getType()));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Referral.getValue())) {
                // showMessage("Referral");
                TCLog.d("Referral ");
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.Coupon.getValue())) {
                pushNotificationModel.getData().setCoupon_id(TCUtils.getIDFromURL(pushNotificationModel.getData().getUrl()));
                addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
                pushNotificationModel.getData().setCoupon_id(TCUtils.getIDFromURL(pushNotificationModel.getData().getUrl()));
                addFragment(UserMyCouponDetailScreen.newInstance(pushNotificationModel));
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.VendorDetail.getValue())) {
                FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                        .addOnSuccessListener(getActiveActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                if (pendingDynamicLinkData != null) {
                                    String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                    if (!TCUtils.isEmpty(vendorId)) {
                                        ((TCMainActivity) getActiveActivity()).openHomeScreen(vendorId, false);
                                    }
                                }
                            }
                        });
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.VendorCouponList.getValue())) {
                FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                        .addOnSuccessListener(getActiveActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                if (pendingDynamicLinkData != null) {
                                    String vendorId = TCUtils.getVendorIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                    if (!TCUtils.isEmpty(vendorId)) {
                                        ((TCMainActivity) getActiveActivity()).openHomeScreen(vendorId, true);
                                    }
                                }
                            }
                        });
            } else if (pushNotificationModel.getData().getType().equals(EnumMgr.PushNotification.CategoryCatalogue.getValue())) {
                FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse(pushNotificationModel.getData().getUrl()))
                        .addOnSuccessListener(getActiveActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                if (pendingDynamicLinkData != null) {
                                    String couponId = TCUtils.getCouponIdFromCouponDynamicLink(pendingDynamicLinkData.getLink());
                                    if (!TCUtils.isEmpty(couponId)) {
                                        ((TCMainActivity) getActiveActivity()).openCouponUserScreen(couponId);
                                    }
                                }
                            }
                        });
            } else {
                TCLog.d("Can't get detail ");
                // showMessage("Can't get detail");
            }

        }

        if (!data.isIs_read()) {
            listNotification.get(position).setIs_read(true);
            adapter.notifyItemChanged(position);
        }

        requestApi(new GeneralNotificationReadRequest(new ReadNotificationRequestModel(data.getId()),
                isAppUser() ? GeneralRequestTarget.USER_READ_NOTIFY : GeneralRequestTarget.SHOP_READ_NOTIFY, this));
    }

    public void updateMarkAll() {
        if (adapter != null) {
            adapter.updateMarkAll();
        }

    }
}
