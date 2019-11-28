package com.teecoin.feature.walletSystem.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.couponSystem.user.checkinNotification.CheckInNotificationScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.payment.shopPaymentDetail.ShopPaymentDetailScreen;
import com.teecoin.feature.payment.userPaymentAndCoinbackDetail.UserPaymentAndCoinBackDetailScreen;
import com.teecoin.feature.reviewSystem.detailReview.UserReviewDetailScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.walletSystem.coinback.CoinBackScreen;
import com.teecoin.feature.walletSystem.historyPaymentDetail.TransferMoneyDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.walletsystem.PaymentDetailModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import core.view.RecycleListener;

public class HistoryScreen extends TCWalletBaseFragment implements RecycleListener<TransactionDetailModel>, APIResponseListener {

    @BindView(R.id.frg_history_rcv)
    TCRecyclerView rcv_history;
    public static HistoryScreen getInstance() {
        return new HistoryScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_history).toUpperCase());
        showButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        new LoadHistoryDataProcess(getActiveActivity(), rcv_history, this);
    }

    @Override
    public void onItemClick(View view, TransactionDetailModel item, int position, EnumMgr.ClickType clickType) {
        itemClickTransaction(item,clickType);
    }


    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            PaymentDetailModel detailModel = (PaymentDetailModel) response.getResult();
            if (!isTransferCoinToYourself(detailModel.getSource())) {
                addFragment(CoinBackScreen.getInstance((PaymentDetailModel) response.getResult()));
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_SHOP_TRANSACTION_HISTORY_LIST
                || requestTarget == WalletRequestTarget.GET_USER_TRANSACTION_HISTORY_LIST) {
            rcv_history.onLoadMoreComplete();
        } else if (requestTarget == WalletRequestTarget.GET_PAYMENT_DETAIL) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
        }
    }
}
