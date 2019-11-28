package com.teecoin.feature.couponSystem.user.checkinNotification;

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
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponCatalogueResultAdapter;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponNotificationResultAdapter;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponResultMenuAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CheckinNotificationResultModel;
import com.teecoin.model.couponsystem.CouponsCheckinNotificationModel;
import com.teecoin.model.couponsystem.MenuCouponModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserCheckInNotificationDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class CheckInNotificationScreen extends TCCouponBaseFragment {
    private static final String NOTIFICATION_MODEL = "PushNotificationModel";
    @BindView(R.id.view_coupon_result_processing_ll_view_processing)
    View view_processing;
    @BindView(R.id.view_coupon_check_in_success_view_header)
    View view_header;
    @BindView(R.id.frag_check_in_success_tv_vendor)
    TextView tv_vendor;
    @BindView(R.id.frag_check_in_success_tv_date)
    TextView tv_date;
    @BindView(R.id.frag_check_in_success_tv_exchange_rate)
    TextView tv_exchange_rate;
    //    @BindView(R.id.frag_check_in_success_tv_exchange)
//    TextView tv_exchange;
    @BindView(R.id.frag_check_in_success_tv_thank_you)
    TextView tv_thank_you;
    @BindView(R.id.frag_check_in_success_tv_visit)
    TextView tv_visit;
    @BindView(R.id.frag_check_in_success_tv_amount)
    TextView tv_amount;
    @BindView(R.id.frag_coupon_user_result_view_bottom)
    View view_bottom;
    @BindView(R.id.view_review)
    View view_review;
    @BindView(R.id.frag_coupon_user_result_view_more)
    View view_more;
    @BindView(R.id.frag_coupon_user_result_view_check_in)
    View check_in;
    @BindView(R.id.frag_coupon_user_result_view_coupon_running)
    View view_coupon_running;
    @BindView(R.id.frag_user_coupon_catalogue_result_tv_name)
    TextView tv_coupon_for_fee;
    @BindView(R.id.frag_user_coupon_catalogue_result_tv_notification)
    TextView tv_notification;
    @BindView(R.id.item_coupon_user_catalogue_rcv_running)
    TCRecyclerView rcv_running;
    @BindView(R.id.frag_coupon_user_result_view_coupon_purchase)
    View view_coupon_purchase;
    @BindView(R.id.frag_coupon_user_result_view_menu)
    View view_menu;
    @BindView(R.id.item_coupon_user_catalogue_rcv_menu)
    TCRecyclerView rcv_menu;
    @BindView(R.id.item_coupon_user_catalogue_rcv_purchasing)
    TCRecyclerView rcv_purchasing;
    private CheckinNotificationResultModel checkinNotificationResultModel;
    private PushNotificationModel pushNotificationModel;

    private UserCouponCatalogueResultAdapter adapter_purchasing;

    public static CheckInNotificationScreen getInstance(PushNotificationModel pushNotificationModel) {
        CheckInNotificationScreen screen = new CheckInNotificationScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NOTIFICATION_MODEL, pushNotificationModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_checkin_success, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showFooter();
        showHeader();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.text_details).toUpperCase());
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pushNotificationModel = (PushNotificationModel) bundle.getSerializable(NOTIFICATION_MODEL);
        }
        view_review.setVisibility(View.GONE);
        view_menu.setVisibility(View.GONE);
        getData();
        view_review.setOnClickListener(v -> addFragment(UserReviewShopScreen.getInstance(checkinNotificationResultModel)));
    }

    private void setupRecycler() {
        if (checkinNotificationResultModel.getCoupons() != null) {
            CouponsCheckinNotificationModel coupons = checkinNotificationResultModel.getCoupons();
            if (coupons.getNotificationCoupons() != null && coupons.getNotificationCoupons().size() > 0) {
                view_coupon_running.setVisibility(View.VISIBLE);
                rcv_running.setLayoutManager(new LinearLayoutManager(
                        getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
                UserCouponNotificationResultAdapter adapter_running =
                        new UserCouponNotificationResultAdapter(LayoutInflater.from(getActiveActivity()), coupons.getNotificationCoupons(), null);
                rcv_running.setAdapter(adapter_running);
                tv_coupon_for_fee.setText(TCUtils.getString(R.string.text_coupon_for_free));
                tv_notification.setVisibility(View.VISIBLE);
            } else {
                view_coupon_running.setVisibility(View.GONE);
            }

            if (coupons.getCatalogueCoupons() != null && coupons.getCatalogueCoupons().size() > 0) {
                view_coupon_purchase.setVisibility(View.VISIBLE);
                rcv_purchasing.setLayoutManager(new LinearLayoutManager(
                        getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter_purchasing = new UserCouponCatalogueResultAdapter(LayoutInflater.from(getActiveActivity()), coupons.getCatalogueCoupons(), (view, item, position, clickType) -> {
                    UserCouponCatalogueDataModel userCouponCatalogueDataModel = new UserCouponCatalogueDataModel();
                    userCouponCatalogueDataModel.setId(item.getId());
                    addFragment(UserMyCouponDetailScreen.newInstance(userCouponCatalogueDataModel));
                });
                rcv_purchasing.setAdapter(adapter_purchasing);
            } else {
                view_coupon_purchase.setVisibility(View.GONE);
            }

            if (view_coupon_running.getVisibility() == View.GONE && view_coupon_purchase.getVisibility() == View.GONE) {
                view_more.setVisibility(View.GONE);
            } else {
                view_more.setVisibility(View.VISIBLE);
            }

            // view_menu.setVisibility(View.VISIBLE);
            ArrayList<MenuCouponModel> listMenu = new ArrayList<>();
            UserCouponResultMenuAdapter adapter_menu = new UserCouponResultMenuAdapter(LayoutInflater.from(getActiveActivity()), listMenu, (view, item, position, clickType) -> {

            });
//            rcv_menu.setAdapter(adapter_menu);
//            for (int i = 0; i < 3; i++) {
//                MenuCouponModel menuCouponModel = new MenuCouponModel("1", "", "https://teecoin-test.s3.amazonaws.com/vendors/10152/coupons/AllYouCanEatSushi.jpg");
//                MenuCouponModel menuCouponModel1 = new MenuCouponModel("2", "", "https://teecoin-test.s3.amazonaws.com/vendors/10161/coupons/006.jpg");
//                listMenu.add(menuCouponModel);
//                listMenu.add(menuCouponModel1);
//            }
//            rcv_menu.onLoadMoreComplete();
        }


    }

    private void getData() {
        if (pushNotificationModel == null)
            return;
        requestApi(new CouponUserCheckInNotificationDetailRequest(pushNotificationModel.getData().getTransaction_id(),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        checkinNotificationResultModel = (CheckinNotificationResultModel) response.getResult();
                        if (checkinNotificationResultModel != null) {
                            view_processing.setVisibility(View.GONE);
                            view_header.setVisibility(View.VISIBLE);
                            view_review.setVisibility(View.VISIBLE);
                            fillData();
                            setupRecycler();
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        showBaseMessage(errorModel.getErrorMessage());
                    }
                }));
    }

    private void fillData() {
        tv_vendor.setText(checkinNotificationResultModel.getShop_name());
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(checkinNotificationResultModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));

        TransactionConfigModel transactionConfigModel = new TransactionConfigModel(RealmController.getInstance().getData(TransactionConfigModel.class));
        tv_exchange_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                transactionConfigModel.getCoinExchange(), transactionConfigModel.getCode()));

        tv_thank_you.setText(String.format(TCUtils.getString(R.string.coupon_check_in_thank_you), checkinNotificationResultModel.getShop_name()));
        tv_visit.setText(Html.fromHtml("<u>" + String.format(TCUtils.getString(R.string.coupon_check_in_visit), checkinNotificationResultModel.getShop_name()) + "</u>"));
        tv_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, checkinNotificationResultModel.getAmount()));

        tv_visit.setOnClickListener(v -> {
            if (checkinNotificationResultModel != null) {
                if (checkinNotificationResultModel.getVendor() != null) {
                    addFragment(VendorScreen.getInstance(checkinNotificationResultModel.getVendor().getId()));
                }

            }
        });
    }

    public void reloadPriceCoupon() {
        if (null != adapter_purchasing)
            adapter_purchasing.notifyDataSetChanged();
    }
}
