package com.teecoin.base;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.checkinNotification.CheckInNotificationScreen;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.general.popup.MessageBaseScreen;
import com.teecoin.feature.payment.shopPaymentDetail.ShopPaymentDetailScreen;
import com.teecoin.feature.payment.userPaymentAndCoinbackDetail.UserPaymentAndCoinBackDetailScreen;
import com.teecoin.feature.reviewSystem.detailReview.UserReviewDetailScreen;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.walletSystem.convertToTecDetails.ConvertToTecDetailScreen;
import com.teecoin.feature.walletSystem.historyPaymentDetail.TransferMoneyDetailScreen;
import com.teecoin.feature.walletSystem.withdrawDetails.WithdrawDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.general.FiatWalletModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetConvertToTecDetailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetPaymentDetailRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetWithdrawDetail;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

public class TCWalletBaseFragment extends TCBaseFragment implements APIResponseListener {

    @Override
    public void onBindView() {

    }

    public boolean appIsOpening() {
        return ((TCMainActivity) getActiveActivity()).isOpening();
    }
    protected void getPaymentDetail(String transaction_id) {
        requestApi(new WalletGetPaymentDetailRequest(transaction_id, this));
    }
    protected void getConvertToTECDetail(String transaction_id){
        requestApi(new WalletGetConvertToTecDetailRequest(transaction_id, this));
    }
    protected void getWithdrawDetail(String transaction_id){
        requestApi(new WalletGetWithdrawDetail(transaction_id, this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

    }

    public void itemClickTransaction(TransactionDetailModel item, EnumMgr.ClickType clickType) {
        if (clickType == EnumMgr.ClickType.WriteReview) {
            addFragment(UserReviewShopScreen.getInstance(item));
        } else if (clickType == EnumMgr.ClickType.CoinBack && !isAppUser()) {
            getPaymentDetail(item.getTransaction_id());
        } else if (item.getType().equals(EnumMgr.TransactionType.Reward.getValue())
                || item.getType().equals(EnumMgr.TransactionType.TransferMoney.getValue())) {
            addFragment(TransferMoneyDetailScreen.getInstance(item));
        } else if (item.getType().equals(EnumMgr.TransactionType.CoinBack.getValue())
                || item.getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
            addFragment(isAppUser() ? UserPaymentAndCoinBackDetailScreen.getInstance(item) : ShopPaymentDetailScreen.getInstance(item.getTransaction_id()));
        } else if (item.getType().equals(EnumMgr.TransactionType.Checkin.getValue())) {
            DataPushNotificationModel dataPushNotificationModel = new DataPushNotificationModel(TCUtils.getIDFromURL(item.getUrl()));
            PushNotificationModel pushNotificationModel = new PushNotificationModel();
            pushNotificationModel.setData(dataPushNotificationModel);
            addFragment(CheckInNotificationScreen.getInstance(pushNotificationModel));
        } else if (item.getType().equals(EnumMgr.TransactionType.CouponPurchase.getValue())) {
            addFragment(UserMyCouponDetailScreen.newInstance(item));
        } else if (item.getType().equals(EnumMgr.TransactionType.ConvertToTEC.getValue())) {
            addFragment(ConvertToTecDetailScreen.getInstance(item.getTransaction_id(),item.getType()));
        }
        else {
            addFragment(UserReviewDetailScreen.getInstance(item));
        }
    }

    public void ScanQRCodeInvalid() {
        new MessageBaseScreen(getActiveActivity(), TCUtils.getString(R.string.qr_code_is_invalid)).show();
    }
    public void showPopupBalanceEnough(String message){
        new TCFailDialog(getActiveActivity(), TCUtils.getDrawable(R.drawable.ic_message), TCUtils.getString(R.string.tec_insufficient),
                message, TCUtils.getString(R.string.text_ok)).show();
    }
    public void itemSGDWalletClick(FiatWalletModel item){
        if(item==null)
            return;
        if (item.getType().equals(EnumMgr.FiatTransactionType.Payment.getValue())) {
            addFragment(ShopPaymentDetailScreen.getInstance(item.getTransaction_id()));
        } else if (item.getType().equals(EnumMgr.FiatTransactionType.ConvertToTEC.getValue())) {
            addFragment(ConvertToTecDetailScreen.getInstance(item.getTransaction_id(), item.getType()));
        }else if(item.getType().equals(EnumMgr.FiatTransactionType.WithdrawRequest.getValue())){
            addFragment(WithdrawDetailScreen.getInstance(item.getTransaction_id(),item.getType()));
        }
    }

}
