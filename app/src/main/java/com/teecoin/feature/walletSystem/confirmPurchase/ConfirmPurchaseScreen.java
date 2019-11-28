package com.teecoin.feature.walletSystem.confirmPurchase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.reviewSystem.inactiveUsers.InactiveUserScreen;
import com.teecoin.feature.walletSystem.purchaseResult.PurchaseResultScreen;
import com.teecoin.utils.TCUtils;

public class ConfirmPurchaseScreen extends TCWalletBaseFragment {

    public static ConfirmPurchaseScreen getInstance() {
        return new ConfirmPurchaseScreen();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_purchase, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.confirm_purchase_upper_case));
        showButtonBackToolbar();
        showFooter();
        showMenuNextBottom();
        showButtonDone();
        hideButtonNext();
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {

    }

    private void initClickEvent() {

    }

    private void gotoInactiveUsers() {
        addFragment(InactiveUserScreen.getInstance());
    }

    public boolean validate() {
        addFragment(PurchaseResultScreen.getInstance());
        ((TCMainActivity) getActiveActivity()).disableDoneButton();
        return false;
    }
}
