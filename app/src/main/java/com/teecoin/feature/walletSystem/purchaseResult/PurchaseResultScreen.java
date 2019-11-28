package com.teecoin.feature.walletSystem.purchaseResult;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.reviewSystem.inactiveUsers.InactiveUserScreen;
import com.teecoin.feature.walletSystem.purchase.PurchaseScreen;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.RippleBackground;

import butterknife.BindView;

public class PurchaseResultScreen extends TCWalletBaseFragment {

    @BindView(R.id.frg_purchase_result_ll_add_new)
    LinearLayout ll_add_new;
    @BindView(R.id.ll_try_again)
    LinearLayout ll_try_again;
    @BindView(R.id.frg_purchase_result_tv_back_to_list)
    TextView tv_back_to_list;
    @BindView(R.id.frg_purchase_result_tv_inactive_users_count)
    TextView frg_purchase_result_tv_inactive_users_count;
    @BindView(R.id.frg_purchase_result_tv_title)
    TextView frg_purchase_result_tv_title;
    @BindView(R.id.frg_purchase_result_tv_total_tec)
    TextView frg_purchase_result_tv_total_tec;
    @BindView(R.id.frg_purchase_result_tv_error_message)
    TextView tv_error_message;

    @BindView(R.id.ll_rip_coinback_success)
    LinearLayout ll_rip_success;
    @BindView(R.id.ll_rip_coinback_fail)
    LinearLayout ll_rip_fail;
    @BindView(R.id.rip_coinback_success)
    RippleBackground rip_success;
    @BindView(R.id.rip_coinback_fail)
    RippleBackground rip_fail;

    public static PurchaseResultScreen getInstance() {
        PurchaseResultScreen screen = new PurchaseResultScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        updateUI(EnumMgr.TransactionStatus.Success);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fillData();
        }
        init();
        initClickEvent();
    }

    private void updateUI(EnumMgr.TransactionStatus status) {
        if (status == EnumMgr.TransactionStatus.Loading) {
            ll_add_new.setVisibility(View.GONE);
            tv_back_to_list.setVisibility(View.GONE);
            ll_try_again.setVisibility(View.GONE);
            ll_rip_fail.setVisibility(View.GONE);
            ll_rip_success.setVisibility(View.GONE);
            tv_error_message.setVisibility(View.GONE);
        } else if (status == EnumMgr.TransactionStatus.Success) {
            rip_success.startRippleAnimation();
            ll_rip_success.setVisibility(View.VISIBLE);
            ll_add_new.setVisibility(View.VISIBLE);
            tv_back_to_list.setVisibility(View.VISIBLE);
            ll_try_again.setVisibility(View.GONE);
            tv_error_message.setVisibility(View.GONE);
        } else {
            ll_rip_fail.setVisibility(View.VISIBLE);
            ll_rip_success.setVisibility(View.GONE);
            rip_fail.startRippleAnimation();
            ll_add_new.setVisibility(View.GONE);
            tv_back_to_list.setVisibility(View.VISIBLE);
            ll_try_again.setVisibility(View.VISIBLE);
            tv_error_message.setVisibility(View.VISIBLE);
        }
    }

    private void fillData() {

    }

    private void init() {

    }

    private void initClickEvent() {
        ll_add_new.setOnClickListener(v -> gotoPurchaseScreen());
        tv_back_to_list.setOnClickListener(v -> gotoInactiveUserScreen());
    }

    private void gotoPurchaseScreen() {
        replaceFragment(PurchaseScreen.getInstance(), false);
    }

    private void gotoInactiveUserScreen() {
        replaceFragment(InactiveUserScreen.getInstance(), false);
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
