package com.teecoin.feature.reviewSystem.favourite;

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
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.ViewPagerAdapter;
import com.teecoin.ui.TCViewPager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class FavouriteScreen extends TCReviewBaseFragment {
    @BindView(R.id.frg_view_pager_rl_add_for_shop)
    View vAddShopTitle;
    @BindView(R.id.frg_view_pager_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.frg_view_pager_view_pager)
    TCViewPager viewPager;
    @BindView(R.id.view_tab_tv_favourite)
    TextView tvFavorite;
    @BindView(R.id.view_tab_tv_recent)
    TextView tvRecent;

    @BindView(R.id.frg_view_pager_rl_toolbar)
    View rl_toolbar;

    private ViewPagerAdapter viewPagerAdapter;


    public static FavouriteScreen getInstance() {
        return new FavouriteScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onBaseResume() {
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.my_favourite));
        hideButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        vAddShopTitle.setVisibility(View.GONE);
        rl_toolbar.setVisibility(View.GONE);
        setupTabMenu();

    }

    private void setupTabMenu() {
        tvFavorite.setVisibility(View.VISIBLE);
        tvFavorite.setSelected(true);

        tvRecent.setVisibility(View.VISIBLE);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(VendorFavoriteScreen.getInstance(), "");
        viewPagerAdapter.addFragment(VendorRecentScreen.getInstance(), "");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvFavorite.setSelected(position == 0);
                tvRecent.setSelected(!(position == 0));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setPagingEnabled(false);
    }

}
