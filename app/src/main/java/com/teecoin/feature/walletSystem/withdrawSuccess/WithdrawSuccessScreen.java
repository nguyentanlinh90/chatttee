package com.teecoin.feature.walletSystem.withdrawSuccess;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.walletsystem.WithdrawModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class WithdrawSuccessScreen extends TCWalletBaseFragment {
    private static final String WITHDRAW_MODEL = "WITHDRAW_MODEL";
    @BindView(R.id.frg_transaction_tv_success)
    TextView tv_success;
    @BindView(R.id.frag_withdraw_success_tv_withdraw_amount)
    TextView tv_withdraw_amount;
    @BindView(R.id.frag_withdraw_success_tv_fee)
    TextView tv_fee;
    @BindView(R.id.frag_withdraw_success_tv_sgd_balance)
    TextView tv_sgd_balance;
    private WithdrawModel withdrawModel;
    public static WithdrawSuccessScreen getInstance(WithdrawModel withdrawModel) {
        WithdrawSuccessScreen screen = new WithdrawSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(WITHDRAW_MODEL, withdrawModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_withdraw_success, container, false);
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
        if (null != bundle) {
            withdrawModel = (WithdrawModel) bundle.getSerializable(WITHDRAW_MODEL);
        }
        registerSingleClick(R.id.frag_withdraw_success_tv_back_to_wallet);
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frag_withdraw_success_tv_back_to_wallet);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_withdraw_success_tv_back_to_wallet:
                openWalletScreen(true, false);
                break;
        }
    }

    private void setData() {
        if (withdrawModel != null) {
            tv_success.setText(TCUtils.getString(R.string.payment_processing));
            tv_withdraw_amount.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdrawModel.getCashAmount()), withdrawModel.getCurrencyCode()));
            tv_fee.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, withdrawModel.getWithdrawalProcessingFee()), withdrawModel.getCurrencyCode()));
            tv_sgd_balance.setText(String.format("%s %s", TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, TCUtils.convertToDouble(withdrawModel.getFiat_balance())), withdrawModel.getCurrencyCode()));
        }
    }

}
