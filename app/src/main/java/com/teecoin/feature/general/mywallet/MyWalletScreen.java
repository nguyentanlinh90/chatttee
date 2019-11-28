package com.teecoin.feature.general.mywallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.ViewPagerAdapter;
import com.teecoin.feature.walletSystem.walletUser.WalletAccountScreen;
import com.teecoin.ui.TCViewPager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class MyWalletScreen extends TCBaseFragment {
    private static final String ID_TEC_WALLET = "ID_TEC_WALLET";

    @BindView(R.id.frg_view_pager_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.frg_view_pager_view_pager)
    TCViewPager viewPager;
    @BindView(R.id.view_tab_tv_sgd_wallet)
    TextView tvSGDWallet;
    @BindView(R.id.view_tab_tv_tec_wallet)
    TextView tvTECWallet;

    @BindView(R.id.frg_view_pager_rl_add_for_shop)
    View vAdd;
    @BindView(R.id.frg_view_pager_rl_toolbar)
    View vToolbar;

    private boolean isTECWallet = false;

    public static MyWalletScreen getInstance(boolean isTECWallet) {
        MyWalletScreen screen = new MyWalletScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ID_TEC_WALLET, isTECWallet);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.my_wallet));
        showButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isTECWallet = bundle.getBoolean(ID_TEC_WALLET);
        }
        setupTabMenu();
    }

    private void setupTabMenu() {
        vAdd.setVisibility(View.GONE);
        vToolbar.setVisibility(View.GONE);

        tvSGDWallet.setVisibility(View.VISIBLE);
        tvTECWallet.setVisibility(View.VISIBLE);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(FiatWalletScreen.getInstance(), "");
        viewPagerAdapter.addFragment(WalletAccountScreen.getInstance(), "");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSGDWallet.setSelected(position == 0);
                tvTECWallet.setSelected(position == 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (isTECWallet) {
            tvTECWallet.setSelected(true);
            viewPager.setCurrentItem(1);
        } else {
            tvSGDWallet.setSelected(true);
        }
    }


    @Override
    public void onBaseDestroyView() {

    }
}
