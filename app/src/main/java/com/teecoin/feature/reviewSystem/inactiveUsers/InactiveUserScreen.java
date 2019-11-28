package com.teecoin.feature.reviewSystem.inactiveUsers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.walletSystem.purchase.PurchaseScreen;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class InactiveUserScreen extends TCReviewBaseFragment implements RecycleListener<InactiveUserModel> {

    @BindView(R.id.frg_inactive_user_ll_purchase)
    View ll_purchase;
    @BindView(R.id.frg_inactive_user_rcv_purchase_history)
    TCRecyclerView rcv_purchase_history;

    public static InactiveUserScreen getInstance() {
        return new InactiveUserScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inactive_user, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.business_insights_upper_case));
        showButtonBackToolbar();
        hideFooter();
        showMenuNextBottom();
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
        ArrayList<InactiveUserModel> list = new ArrayList<>();
        list.add(new InactiveUserModel("2018/09/12", "Free ice-cream", "valid from 2018/09/30 - 2018/09/30", "120 Inactive Users - Paid 123.6"));
        list.add(new InactiveUserModel("2018/09/12", "Free ice-cream", "valid from 2018/09/30 - 2018/09/30", "120 Inactive Users - Paid 123.6"));
        list.add(new InactiveUserModel("2018/09/08", "Free ice-cream", "valid from 2018/09/30 - 2018/09/30", "120 Inactive Users - Paid 123.6"));
        list.add(new InactiveUserModel("2018/09/08", "Free a cup coffee", "valid from 2018/09/30 - 2018/09/30", "120 Inactive Users - Paid 123.6"));

        InactiveUserAdapter adapter = new InactiveUserAdapter(getLayoutInflater(), list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActiveActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_purchase_history.setLayoutManager(layoutManager);
        rcv_purchase_history.setAdapter(adapter);
    }

    private void initClickEvent() {
        ll_purchase.setOnClickListener(v -> {
            addFragment(PurchaseScreen.getInstance());
        });
    }

    @Override
    public void onItemClick(View view, InactiveUserModel item, int position, EnumMgr.ClickType clickType) {

    }
}
