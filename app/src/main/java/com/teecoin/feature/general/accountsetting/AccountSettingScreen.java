package com.teecoin.feature.general.accountsetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.feature.general.changeLanguage.ChangeLanguageScreen;
import com.teecoin.feature.general.coinbackSetting.CoinBackSettingScreen;
import com.teecoin.feature.general.updateAccount.UpdateAccountScreen;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;


public class AccountSettingScreen extends TCGeneralBaseFragment {
    @BindView(R.id.frg_account_setting_rl_update_user_info)
    View vUserInfo;
    @BindView(R.id.frg_account_setting_rl_update_shop_info)
    View vShopInfo;
    @BindView(R.id.frg_account_setting_rl_coin_back)
    View vCoinBack;

    public static AccountSettingScreen getInstance() {
        return new AccountSettingScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_setting, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.text_setting));
        hideViewQrCode();
        hideButtonAddCoupon();
    }

    @Override
    public void onBindView() {
        vUserInfo.setVisibility(isAppUser() ? View.VISIBLE : View.GONE);
        vShopInfo.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        vCoinBack.setVisibility(isAppUser() ? View.GONE : View.VISIBLE);
        initClickEvent();
    }

    private void initClickEvent() {
        registerSingleClick(
                R.id.frg_account_setting_rl_update_user_info, R.id.frg_account_setting_rl_languages,
                R.id.frg_account_setting_rl_update_shop_info, R.id.frg_account_setting_rl_coin_back);
    }


    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_account_setting_rl_update_user_info:
                addFragment(UpdateAccountScreen.getInstance());
                break;
            case R.id.frg_account_setting_rl_languages:
                addFragmentForResult(EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue(), ChangeLanguageScreen.getInstance());
                break;

            case R.id.frg_account_setting_rl_update_shop_info:
                addFragment(UpdateAccountScreen.getInstance());
                break;

            case R.id.frg_account_setting_rl_coin_back:
                openCoinBackSetting();
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frg_account_setting_rl_update_user_info, R.id.frg_account_setting_rl_languages,
                R.id.frg_account_setting_rl_update_shop_info, R.id.frg_account_setting_rl_coin_back);
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.CHANGE_LANGUAGE_REQUEST_CODE.getValue()) {
            refreshFragment(this);
        }
    }

    private void openCoinBackSetting() {
        inputPassword(CoinBackSettingScreen.getInstance(), EnumMgr.TypeDialog.InputPassword);
    }
}
