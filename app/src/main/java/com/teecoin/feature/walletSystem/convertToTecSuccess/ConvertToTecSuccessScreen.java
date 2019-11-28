package com.teecoin.feature.walletSystem.convertToTecSuccess;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.walletsystem.ConvertToTecModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import retrofit2.http.HTTP;

public class ConvertToTecSuccessScreen extends TCWalletBaseFragment {
    private static final String CONVERT_TO_TEC_MODEL = "CONVERT_TO_TEC_MODEL";

    @BindView(R.id.frag_convert_to_tec_success_tv_convert_amount)
    TextView tv_convert_amount;
    @BindView(R.id.frag_convert_to_tec_success_tv_receivable)
    TextView tv_receivable;

    private ConvertToTecModel convertToTecModel;

    public static ConvertToTecSuccessScreen getInstance(ConvertToTecModel convertToTecModel) {
        ConvertToTecSuccessScreen screen = new ConvertToTecSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONVERT_TO_TEC_MODEL, convertToTecModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_convert_to_tec_success, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
             convertToTecModel = (ConvertToTecModel) bundle.getSerializable(CONVERT_TO_TEC_MODEL);
        }
        registerSingleClick(R.id.frag_convert_to_tec_success_tv_new_convert, R.id.frag_convert_to_tec_success_tv_back_to_wallet);
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frag_convert_to_tec_success_tv_new_convert, R.id.frag_convert_to_tec_success_tv_back_to_wallet);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_convert_to_tec_success_tv_new_convert:
                onBackResult();
                break;
            case R.id.frag_convert_to_tec_success_tv_back_to_wallet:
                openWalletScreen(true, false);
                break;
        }
    }
    private void setData(){
        if(convertToTecModel==null)
            return;
        tv_convert_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, convertToTecModel.getConvertCashAmount()), convertToTecModel.getCurrencyCode()));
        tv_receivable.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(convertToTecModel.getReceivable_amount())), TCUtils.getString(R.string.tee_coin_symbol)));

    }
}
