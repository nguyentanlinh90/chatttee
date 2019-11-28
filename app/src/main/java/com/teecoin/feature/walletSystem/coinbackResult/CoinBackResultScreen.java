package com.teecoin.feature.walletSystem.coinbackResult;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.CoinBackModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.RippleBackground;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class CoinBackResultScreen extends TCWalletBaseFragment {
    private static final String COIN_BACK_MODEL = "CoinBackModel";

    @BindView(R.id.ll_rip_coinback_success)
    LinearLayout ll_rip_success;
    @BindView(R.id.ll_rip_coinback_fail)
    LinearLayout ll_rip_fail;
    @BindView(R.id.rip_coinback_success)
    RippleBackground rip_success;
    @BindView(R.id.rip_coinback_fail)
    RippleBackground rip_fail;
    @BindView(R.id.ll_history_gold)
    LinearLayout ll_history_gold;
    @BindView(R.id.ll_history_white)
    LinearLayout ll_history_white;
    @BindView(R.id.ll_try_again)
    LinearLayout ll_try_again;
    @BindView(R.id.frg_coinback_result_tv_total_tee)
    TextView tv_total_tee;
    @BindView(R.id.frg_coinback_result_tv_invoice_id)
    TextView tv_invoice_id;
    @BindView(R.id.frg_coinback_result_tv_payment)
    TextView tv_payment;
    @BindView(R.id.frg_coinback_result_tv_address)
    TextView tv_address;
    @BindView(R.id.frg_coinback_result_tv_error_message)
    TextView tv_error_message;


    private CoinBackModel coinBackModel;

    public static CoinBackResultScreen getInstance(CoinBackModel coinBackModel) {
        CoinBackResultScreen screen = new CoinBackResultScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COIN_BACK_MODEL, coinBackModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coinback_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        updateUI(EnumMgr.TransactionStatus.Loading);
        Bundle bundle = getArguments();
        if (bundle != null) {
            coinBackModel = (CoinBackModel) bundle.getSerializable(COIN_BACK_MODEL);
            fillData();
        }
        init();
        initClickEvent();
    }

    private void updateUI(EnumMgr.TransactionStatus status) {
        if (status == EnumMgr.TransactionStatus.Loading) {
            ll_history_gold.setVisibility(View.GONE);
            ll_history_white.setVisibility(View.GONE);
            ll_try_again.setVisibility(View.GONE);
            ll_rip_fail.setVisibility(View.GONE);
            ll_rip_success.setVisibility(View.GONE);
            tv_error_message.setVisibility(View.GONE);
        } else if (status == EnumMgr.TransactionStatus.Success) {
            rip_success.startRippleAnimation();
            ll_rip_success.setVisibility(View.VISIBLE);
            ll_history_gold.setVisibility(View.VISIBLE);
            ll_history_white.setVisibility(View.GONE);
            ll_try_again.setVisibility(View.GONE);
            tv_error_message.setVisibility(View.GONE);
        } else {
            ll_rip_fail.setVisibility(View.VISIBLE);
            ll_rip_success.setVisibility(View.GONE);
            rip_fail.startRippleAnimation();
            ll_history_gold.setVisibility(View.GONE);
            ll_history_white.setVisibility(View.VISIBLE);
            ll_try_again.setVisibility(View.VISIBLE);
            tv_error_message.setVisibility(View.VISIBLE);
        }
    }

    private void fillData() {
        tv_total_tee.setText(String.format(TCUtils.getString(R.string.string_format_1), coinBackModel.getTotalAmount(), TCUtils.getString(R.string.tee_coin_symbol)));
        if (coinBackModel != null) {
            tv_invoice_id.setText(coinBackModel.getInvoiceId());
            tv_payment.setText(String.format("%s %s", coinBackModel.getAmount(), TCUtils.getString(R.string.tee_coin_symbol)));
            tv_address.setText(coinBackModel.getDestination());
        }

    }

    private void init() {
        StellarBusinessProcess.getInstance().startCoinBack(coinBackModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                updateUI(EnumMgr.TransactionStatus.Success);
                RealmController.getInstance().updateFieldCoinBackTransactionDetailModel(coinBackModel.getPaymentTransactionHash(), true);
                fillData();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                updateUI(EnumMgr.TransactionStatus.Fail);
                tv_error_message.setText(errorModel.getErrorMessage());
            }
        });
    }

    private void initClickEvent() {
        ll_try_again.setOnClickListener(v -> init());
        ll_history_white.setOnClickListener(v -> gotoHistoryList());
        ll_history_gold.setOnClickListener(v -> gotoHistoryList());
    }

    private void gotoHistoryList() {
        ((TCMainActivity) getActiveActivity()).openHistoryScreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rip_fail != null) {
            if (rip_fail.isRippleAnimationRunning()) {
                rip_fail.stopRippleAnimation();
            }
        }
        if (rip_success != null) {
            if (rip_success.isRippleAnimationRunning()) {
                rip_success.stopRippleAnimation();
            }
        }
    }
}
