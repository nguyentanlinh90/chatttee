package com.teecoin.feature.walletSystem.convertToTecDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.ConvertToTecModel;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ConvertToTecDetailScreen extends TCWalletBaseFragment {
    private static final String TRANSACTION_ID = "TRANSACTION_ID";
    private static final String TYPE = "TYPE";
    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_name;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;

    @BindView(R.id.frag_convert_to_tec_detail_tv_note)
    TextView tv_note;
    @BindView(R.id.frag_convert_to_tec_detail_tv_converted_amount)
    TextView tv_converted_amount;

    @BindView(R.id.frag_convert_to_tec_detail_view_fee)
    View view_fee;
    @BindView(R.id.frag_convert_to_tec_detail_tv_fee)
    TextView tv_fee;

    @BindView(R.id.frag_convert_to_tec_detail_view_receivable_conversison)
    View view_receivable_conversison;
    @BindView(R.id.frag_convert_to_tec_detail_tv_receivable_conversison)
    TextView tv_receivable_conversison;

    @BindView(R.id.frag_convert_to_tec_detail_view_line)
    View view_line;
    @BindView(R.id.frag_convert_to_tec_detail_view_receivable_incoming)
    View view_receivable_incoming;
    @BindView(R.id.frag_convert_to_tec_detail_tv_receivable_incoming)
    TextView tv_receivable_incoming;

    private String transaction_id ="";
    private String type ="";

    public static ConvertToTecDetailScreen getInstance(String transactionID,String type) {
        ConvertToTecDetailScreen screen = new ConvertToTecDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_ID, transactionID);
        bundle.putSerializable(TYPE, type);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_convert_to_tec_detail, container, false);
    }
    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showFooter();
        showButtonBackToolbar();
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle =getArguments();
        if(bundle!=null){
            transaction_id =bundle.getString(TRANSACTION_ID);
            type =bundle.getString(TYPE);
        }

        tv_rate.setVisibility(View.INVISIBLE);
        view_fee.setVisibility(View.GONE);
        view_receivable_conversison.setVisibility(View.GONE);
        view_receivable_incoming.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);

        if(!TCUtils.isEmpty(transaction_id)&&!TCUtils.isEmpty(type)){
            getConvertToTECDetail(transaction_id);
        }
    }
    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.GET_CONVERT_TO_TEC_DETAIL) {
            setDataConverToTEC((ConvertToTecModel)response.getResult());

        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        new TCFailDialog(getActiveActivity(),null,TCUtils.getString(R.string.text_message),errorModel.getErrorMessage(),TCUtils.getString(R.string.text_ok)).show();
        setDataEmpty();
    }
    private void setDataConverToTEC(ConvertToTecModel detailModel){
        if(detailModel==null)
            return;

        tv_name.setText(detailModel.getSender());
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(detailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));

        tv_note.setText(String.format(TCUtils.getString(R.string.convert_to_tec_success_note),TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, detailModel.getConvertCashAmount()),detailModel.getCurrencyCode()));
        tv_converted_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, detailModel.getConvertCashAmount()), detailModel.getCurrencyCode()));



        if(type.equals(EnumMgr.PushNotification.ConvertToTEC.getValue())||type.equals(EnumMgr.FiatTransactionType.ConvertToTEC.getValue())||type.equals(EnumMgr.TransactionType.ConvertToTEC.getValue())){
            updateTitleHeader(TCUtils.getString(R.string.tec_conversion_details));
            tv_rate.setVisibility(View.INVISIBLE);
            view_fee.setVisibility(View.GONE);
            view_receivable_conversison.setVisibility(View.VISIBLE);// Receivable = Amount-(feeAmount+baseFeeAmount)
            tv_receivable_conversison.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(detailModel.getReceivable_amount())) , TCUtils.getString(R.string.tee_coin_symbol)));

            view_line.setVisibility(View.GONE);
            view_receivable_incoming.setVisibility(View.GONE);
        }else{
            updateTitleHeader(TCUtils.getString(R.string.incoming_tec_details));
            tv_rate.setVisibility(View.VISIBLE);
            tv_rate.setText(String.format(TCUtils.getString(R.string.general_exchange_coin),
                    TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FULL_FORMAT, detailModel.getRate()),
                    detailModel.getCurrencyCode()));
            tv_note.setText(String.format(TCUtils.getString(R.string.incoming_success_note),TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, detailModel.getConvertCashAmount()),detailModel.getCurrencyCode()));

            view_fee.setVisibility(View.VISIBLE);
            tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(detailModel.getFeeAmount()) + TCUtils.convertToDouble(detailModel.getBasicFeeAmount())), TCUtils.getString(R.string.tee_coin_symbol)));
            view_receivable_conversison.setVisibility(View.GONE);
            view_receivable_incoming.setVisibility(View.VISIBLE);
            tv_receivable_incoming.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(detailModel.getReceivable_amount())), TCUtils.getString(R.string.tee_coin_symbol)));

        }

    }

    private  void setDataEmpty(){
        tv_name.setText("");
        tv_date.setText("");
        tv_rate.setText("");
        tv_receivable_conversison.setText("");
        tv_fee.setText("");
        tv_receivable_incoming.setText("");
        tv_note.setText("");
        tv_converted_amount.setText("");
        updateTitleHeader("");
    }


}
