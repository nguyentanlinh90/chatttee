package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.discover.DiscoverScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCheckInListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class UserCheckInResultScreen extends TCCouponBaseFragment {

    private static final String COUPON_CHECK_IN_DATA = "COUPON_CHECK_IN_DATA";
    @BindView(R.id.view_coupon_result_processing_ll_view_processing)
    View vProcessing;
    @BindView(R.id.frg_check_in_result_ll_check_in_success)
    View vCheckInSuccess;
    @BindView(R.id.view_header_check_in_result_rl_header)
    View vHeader;
    @BindView(R.id.view_header_check_in_result_iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.view_header_check_in_result_iv_header)
    ImageView ivHeader;
    @BindView(R.id.view_header_check_in_result_tv_name_vendor)
    TextView tvVendorName;
    @BindView(R.id.view_header_check_in_result_tv_rating)
    TextView tvVendorRating;
    @BindView(R.id.view_header_check_in_result_rating_bar)
    MaterialRatingBar ratingBar;
    @BindView(R.id.view_header_check_in_result_tv_review)
    TextView tvCountRating;
    @BindView(R.id.view_header_check_in_result_iv_is_coupon)
    ImageView ivCoupon;
    @BindView(R.id.view_header_check_in_result_iv_is_cash)
    ImageView ivCash;
    @BindView(R.id.frg_check_in_result_iv_status)
    ImageView ivStatus;
    @BindView(R.id.frg_check_in_result_tv_status)
    TextView tvStatus;
    @BindView(R.id.frg_check_in_result_tv_message_fail)
    TextView tvMessageFail;
    @BindView(R.id.frg_check_in_result_tv_thanks)
    TextView tvThanks;
    @BindView(R.id.frg_check_in_result_ll_coupon_for_you)
    View cvCouponForYou;
    @BindView(R.id.frg_check_in_result_rcv)
    RecyclerView recyclerView;
    private UserCouponRedeemDataModel couponRedeemData;

    private CheckInCatalogueResultAdapter adapter;

    public static UserCheckInResultScreen getInstance(VendorCodeCheckInModel vendorCodeCheckInModel, boolean fromWallet) {
        UserCheckInResultScreen screen = new UserCheckInResultScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel couponResultModel = new UserCouponRedeemDataModel(fromWallet, vendorCodeCheckInModel);
        bundle.putSerializable(COUPON_CHECK_IN_DATA, couponResultModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_in_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            couponRedeemData = (UserCouponRedeemDataModel) bundle.getSerializable(COUPON_CHECK_IN_DATA);
            if (null != couponRedeemData && null != couponRedeemData.getVendorCodeCheckInModel()) {
                startCheckIn(couponRedeemData.getVendorCodeCheckInModel());
            }
        }

        registerSingleClick(R.id.view_header_check_in_result_iv_back, R.id.view_header_check_in_result_iv_write_review,
                R.id.view_header_check_in_result_iv_share, R.id.view_header_check_in_result_iv_favorite,
                R.id.frg_check_in_result_tv_see_more, R.id.frg_check_in_result_ll_all);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.view_header_check_in_result_iv_back:
                ((TCMainActivity) getActiveActivity()).selectDiscover();
                replaceFragment(DiscoverScreen.getInstance(), true);
                break;

            case R.id.view_header_check_in_result_iv_write_review:
                writeReview(couponRedeemData.getVendor());
                break;

            case R.id.view_header_check_in_result_iv_share:
                getShareVendor(couponRedeemData.getVendor());
                break;

            case R.id.view_header_check_in_result_iv_favorite:
                submitFavourite(0, couponRedeemData.getVendor(),
                        (position, vendorModel) -> ivFavorite.setSelected(!ivFavorite.isSelected()));
                break;

            case R.id.frg_check_in_result_tv_see_more:
                ((TCMainActivity) getActiveActivity()).selectDiscover();
                replaceFragment(VendorScreen.getInstance(couponRedeemData.getVendor().getId()), true);
                break;

            case R.id.frg_check_in_result_ll_all:
                ((TCMainActivity) getActiveActivity()).selectDiscover();
                replaceFragment(VendorScreen.getInstance(couponRedeemData.getVendor().getId(), true), true);
                break;


        }
    }

    private void startCheckIn(VendorCodeCheckInModel vendorCodeCheckInModel) {
        requestApi(new CouponUserGetCouponCheckInListRequest(vendorCodeCheckInModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {

                    RedeemCodeCouponResultModel redeemCodeCouponResultModel = (RedeemCodeCouponResultModel) response.getResult();

                    if (couponRedeemData != null)
                        couponRedeemData.updateAfterCheckIn(redeemCodeCouponResultModel);

                    updateStatus(redeemCodeCouponResultModel);

                    initCatalogueCoupon(redeemCodeCouponResultModel.getCatalogueCoupons());
                } else {
                    updateStatus(null);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
//                updateStatus(null);
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void updateStatus(RedeemCodeCouponResultModel redeemCodeCouponResultModel) {
        vProcessing.setVisibility(View.GONE);
        vCheckInSuccess.setVisibility(View.VISIBLE);
        if (couponRedeemData.getVendor() != null) {
            tvVendorName.setText(couponRedeemData.getVendor().getName());
            tvVendorRating.setText(String.valueOf(couponRedeemData.getVendor().getRating()));
            ratingBar.setRating(couponRedeemData.getVendor().getRating());
            tvCountRating.setText(String.format(
                    TCUtils.getString(couponRedeemData.getVendor().getReviewCount() > 1 ?
                            R.string.count_reviews : R.string.count_review),
                    String.valueOf(couponRedeemData.getVendor().getReviewCount())));
            ivCoupon.setVisibility(couponRedeemData.getVendor().isHaveCatalogueCoupon() ? View.VISIBLE : View.GONE);
            ivCash.setVisibility(couponRedeemData.getVendor().isHaveCashVoucher() ? View.VISIBLE : View.GONE);
            ivFavorite.setSelected(couponRedeemData.getVendor().isFavorite());
        }

        ViewTreeObserver viewTreeObserver = vHeader.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ivHeader.getLayoutParams().height = vHeader.getMeasuredHeight();

                if (couponRedeemData.getVendor() != null) {
                    Glide.with(getActiveActivity()).load(couponRedeemData.getVendor().getFeaturedImage())
                            .apply(new RequestOptions().placeholder(TCUtils.getDrawable(R.drawable.ic_cover_chattee)).error(TCUtils.getDrawable(R.drawable.ic_cover_chattee)))
                            .into(ivHeader);
                }
            }
        });

        if (null != redeemCodeCouponResultModel
                && RedeemCodeCouponResultModel.Check_In_Error.SUCCESS.getValue()
                == redeemCodeCouponResultModel.getError_msg_code()) {

            ivStatus.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_check_success));
            tvStatus.setText(TCUtils.getString(R.string.coupon_successful));
            tvThanks.setText(String.format(TCUtils.getString(R.string.thank_you_for_checking_in_at_s),
                    couponRedeemData.getVendor() != null ? couponRedeemData.getVendor().getName() : ""));
        } else {
            ivStatus.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_fail));
            tvStatus.setTextColor(TCUtils.getColor(R.color.c_8e8e93));
            tvStatus.setText(TCUtils.getString(R.string.coupon_unsuccessful));
            tvMessageFail.setVisibility(View.VISIBLE);

            tvMessageFail.setText(null != redeemCodeCouponResultModel ?
                    redeemCodeCouponResultModel.getMessageErrorCheckIn()
                    : TCUtils.getString(R.string.sorry_something_went_wrong));
            tvThanks.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.thank_you_for_visiting_s),
                    couponRedeemData.getVendor() != null ? couponRedeemData.getVendor().getName() : "")));
        }

//        if (null != redeemCodeCouponResultModel
//                && RedeemCodeCouponResultModel.Check_In_Error.ERROR_6.getValue() == redeemCodeCouponResultModel.getError_msg_code()) {
//            tvThanks.setText("");
//        } else {
//            tvThanks.setText(String.format(TCUtils.getString(R.string.thank_you_for_checking_in_at_s),
//                    couponRedeemData.getVendor() != null ? couponRedeemData.getVendor().getName() : ""));
//        }

    }

    private void initCatalogueCoupon(ArrayList<UserCouponCatalogueDataModel> list) {

        if (list.size() > 0) {

            cvCouponForYou.setVisibility(View.VISIBLE);

            adapter = new CheckInCatalogueResultAdapter(LayoutInflater.from(getActiveActivity()), list, (view, item, position, clickType) -> {
                UserCouponCatalogueDataModel userCouponCatalogueDataModel = new UserCouponCatalogueDataModel();
                userCouponCatalogueDataModel.setId(item.getId());
                addFragment(UserMyCouponDetailScreen.newInstance(userCouponCatalogueDataModel));
            });

            recyclerView.setAdapter(adapter);
        }
    }

    public void reloadPriceCoupon() {
        if (null != adapter)
            adapter.notifyDataSetChanged();
    }
}
