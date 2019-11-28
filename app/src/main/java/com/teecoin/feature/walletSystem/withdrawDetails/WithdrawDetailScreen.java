package com.teecoin.feature.walletSystem.withdrawDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.WithdrawModel;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class WithdrawDetailScreen extends TCWalletBaseFragment {
    private static final String TRANSACTION_ID = "TRANSACTION_ID";
    private static final String TYPE = "TYPE";
    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_name;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;
    @BindView(R.id.frag_withdraw_detail_view_note)
    View view_note;
    @BindView(R.id.frag_withdraw_detail_tv_note)
    TextView tv_note;
    @BindView(R.id.frag_withdraw_detail_tv_amount)
    TextView tv_amount;
    @BindView(R.id.frag_withdraw_detail_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frag_withdraw_detail_view_sgd_balance)
    View view_sgd_balance;
    @BindView(R.id.frag_withdraw_detail_tv_sgd_balance)
    TextView tv_sgd_balance;
    @BindView(R.id.frag_withdraw_detail_iv_image)
    ImageView iv_image;
    @BindView(R.id.frag_withdraw_detail_view_you_will)
    View view_you_will;
    @BindView(R.id.frag_withdraw_detail_tv_tv_interest_amount)
    TextView tv_interest_amount;


    private String transaction_id ="";
    private String type ="";
    private WithdrawModel withdraw;
    public static WithdrawDetailScreen getInstance(String transactionID,String type) {
        WithdrawDetailScreen screen = new WithdrawDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_ID, transactionID);
        bundle.putSerializable(TYPE, type);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_withdraw_details, container, false);
    }
    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showFooter();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.withdraw_details));
    }

    @Override
    public void onBindView() {
        super.onBindView();
        tv_rate.setVisibility(View.GONE);
        Bundle bundle =getArguments();
        if(bundle!=null){
            transaction_id =bundle.getString(TRANSACTION_ID);
            type =bundle.getString(TYPE);
        }

        view_note.setVisibility(View.GONE);
        view_sgd_balance.setVisibility(View.GONE);
        iv_image.setVisibility(View.GONE);
        view_you_will.setVisibility(View.GONE);

        if(!TCUtils.isEmpty(transaction_id)){
            getWithdrawDetail(transaction_id);
        }
        registerSingleClick(R.id.frag_withdraw_detail_iv_image);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frag_withdraw_detail_iv_image);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_withdraw_detail_iv_image:
                if(withdraw!=null)
                new DialogBillDetail(getActiveActivity(),withdraw.getInvoice_url()).show();
                break;
        }
    }
    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_WITHDRAW_COMPLETED_DETAIL) {
            withdraw =(WithdrawModel)response.getResult();
            setDataWithdraw();
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        new TCFailDialog(getActiveActivity(),null,TCUtils.getString(R.string.text_message),errorModel.getErrorMessage(),TCUtils.getString(R.string.text_ok)).show();
        setDataEmpty();
    }

    private void setDataWithdraw(){
        if(withdraw==null){
            setDataEmpty();
            return;
        }
        tv_name.setText(withdraw.getSender());
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(withdraw.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdraw.getCashAmount()), withdraw.getCurrencyCode()));
        tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdraw.getWithdrawalProcessingFee()), withdraw.getCurrencyCode()));

        if(withdraw.getStatus().toLowerCase().equals(TCConstant.TAG_SUCCESS.toLowerCase())){
            view_note.setVisibility(View.GONE);
            view_sgd_balance.setVisibility(View.GONE);
            tv_sgd_balance.setText("");
            iv_image.setVisibility(View.VISIBLE);
            view_you_will.setVisibility(View.VISIBLE);
            tv_interest_amount.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.you_will_earn_an_estimated_title_withdraw_screen), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdraw.getInterest_amount()))));

            if(!TCUtils.isEmpty(withdraw.getInvoice_url())){
                Glide.with(getActiveActivity()).load(withdraw.getInvoice_url()).into(iv_image);
            }else{
                iv_image.setVisibility(View.GONE);
            }
        }else{
            view_note.setVisibility(View.VISIBLE);
            tv_note.setText(String.format(TCUtils.getString(R.string.withdraw_request_detail_note),TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdraw.getCashAmount()),TCDateUtility.convertToCurrentTimeZoneDate(withdraw.getCreated(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY))));
            view_sgd_balance.setVisibility(View.VISIBLE);
            tv_sgd_balance.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdraw.getFiat_balance()), withdraw.getCurrencyCode()));
            iv_image.setVisibility(View.GONE);
            view_you_will.setVisibility(View.GONE);
        }
        //
//        if(type.equals(EnumMgr.FiatTransactionType.WithdrawRequest.getValue())||type.equals(EnumMgr.PushNotification.WithdrawRequest.getValue())){
//            // unknown
//        }else{
//            // success
//        }

    }
    private void setDataEmpty(){
        tv_name.setText("");
        tv_date.setText("");
        tv_amount.setText("");
        tv_fee.setText("");
        iv_image.setVisibility(View.GONE);
    }
}
