package com.teecoin.feature.couponSystem.user.discover;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

import butterknife.BindView;

import static core.base.BaseApplication.getActiveActivity;

public class SelectCountryForDiscoverDialog extends TCBaseDialog {

    @BindView(R.id.dialog_select_country_rcv_country)
    TCRecyclerView rcv_country;
    private ArrayList<CountryCodeModel> listCountry;
    private CountryCodeModel currentCountry;
    private CountryListener countryListener;
    private CountryAdapter adapter;

    SelectCountryForDiscoverDialog(Context context, ArrayList<CountryCodeModel> listCountry, CountryCodeModel currentCountry, CountryListener countryListener) {
        super(context);
        this.listCountry = listCountry;
        this.currentCountry = currentCountry;
        this.countryListener = countryListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_country_for_discover);
    }

    @Override
    protected void initContentView() {
        if (listCountry != null) {
            adapter = new CountryAdapter(LayoutInflater.from(getActiveActivity()), listCountry, (view, item, position, clickType) -> {
                if (item != null) {
                    if (clickType == EnumMgr.ClickType.Country) {
                        adapter.unSelectedItem();
                        item.setSelected(true);
                        currentCountry = item;
                    }
                }
            });
            rcv_country.setAdapter(adapter);
        }
    }

    @Override
    protected void onViewClick() {
        registerSingleClick(R.id.dialog_select_country_iv_close, R.id.dialog_select_country_tv_apply);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.dialog_select_country_iv_close:
                dismiss();
                break;
            case R.id.dialog_select_country_tv_apply:
                countryListener.onCountry(currentCountry);
                dismiss();
                break;
        }
    }
}
